/*
 * Copyright 2014 WANdisco
 *
 *  WANdisco licenses this file to you under the Apache License,
 *  version 2.0 (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package c5db.tablet;

import c5db.C5ServerConstants;
import c5db.client.generated.RegionSpecifier;
import c5db.interfaces.C5Server;
import c5db.interfaces.ReplicationModule;
import c5db.interfaces.replication.Replicator;
import c5db.interfaces.replication.ReplicatorInstanceEvent;
import c5db.interfaces.tablet.TabletStateChange;
import c5db.log.OLogShim;
import c5db.replication.C5GeneralizedReplicator;
import c5db.tablet.tabletCreationBehaviors.MetaTabletLeaderBehavior;
import c5db.tablet.tabletCreationBehaviors.RootTabletLeaderBehavior;
import c5db.tablet.tabletCreationBehaviors.StartableTabletBehavior;
import c5db.util.C5Futures;
import c5db.util.FiberOnly;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.channels.Subscriber;
import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.ThreadFiber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;

/**
 * A tablet, backed by a replicator that keeps values replicated across multiple servers.
 */
public class ReplicatedTablet implements c5db.interfaces.tablet.Tablet {
  private static final Logger LOG = LoggerFactory.getLogger(ReplicatedTablet.class);
  private final C5Server server;
  private long leader;

  private Channel<TabletStateChange> stateChangeChannel = new MemoryChannel<>();


  void setTabletState(State tabletState) {
    this.tabletState = tabletState;
    publishEvent(tabletState);
  }

  private void setTabletStateFailed(Throwable t) {
    this.tabletState = State.Failed;
    publishEvent(t);
  }


  // Config type info:
  private final HRegionInfo regionInfo;
  private final HTableDescriptor tableDescriptor;
  private final StartableTabletBehavior userTabletLeaderBehavior;
  private final ImmutableList<Long> peers;
  private final Configuration conf;
  private final Path basePath;

  // Finals
  private final Fiber tabletFiber;
  private final Fiber shimFiber = new ThreadFiber();
  private final ReplicationModule replicationModule;
  private final Region.Creator regionCreator;

  // State
  private State tabletState;

  private Region region;

  public void setStateChangeChannel(Channel<TabletStateChange> stateChangeChannel) {
    this.stateChangeChannel = stateChangeChannel;
  }

  @Override
  public RegionSpecifier getRegionSpecifier() {
    ByteBuffer value = ByteBuffer.wrap(regionInfo.getEncodedNameAsBytes());
    return new RegionSpecifier(RegionSpecifier.RegionSpecifierType.REGION_NAME, value);
  }

  public ReplicatedTablet(C5Server server,
                          HRegionInfo regionInfo,
                          HTableDescriptor tableDescriptor,
                          List<Long> peers,
                          Path basePath,
                          Configuration conf,
                          ReplicationModule replicationModule,
                          Region.Creator regionCreator,
                          StartableTabletBehavior userTabletLeaderBehavior) {
    this.server = server;
    this.regionInfo = regionInfo;
    this.tableDescriptor = tableDescriptor;
    this.userTabletLeaderBehavior = userTabletLeaderBehavior;
    this.peers = ImmutableList.copyOf(peers);
    this.conf = conf;
    this.basePath = basePath;

    this.tabletFiber = server.getFiberSupplier().getNewFiber(this::handleFail);
    this.replicationModule = replicationModule;
    this.regionCreator = regionCreator;

    this.tabletState = State.Initialized;
  }

  @Override
  public void start() {
    this.tabletFiber.start();
    this.tabletFiber.execute(this::createReplicator);
    shimFiber.start();
  }

  @FiberOnly
  private void createReplicator() {
    assert tabletState == State.Initialized;

    ListenableFuture<Replicator> future =
        replicationModule.createReplicator(regionInfo.getRegionNameAsString(), peers);

    C5Futures.addCallback(future, this::replicatorCreated, this::handleFail, tabletFiber);

    setTabletState(State.CreatingReplicator);
  }

  private void replicatorCreated(Replicator replicator) {
    assert tabletState == State.CreatingReplicator;

    Subscriber<Replicator.State> replicatorStateChannel = replicator.getStateChannel();
    replicatorStateChannel.subscribe(tabletFiber, this::tabletStateCallback);
    Subscriber<ReplicatorInstanceEvent> replicatorEventChannel = replicator.getEventChannel();
    replicatorEventChannel.subscribe(tabletFiber, this::tabletStateChangeCallback);

    // TODO this ThreadFiber is a workaround until issue 252 is fixed; at which point shim can use tabletFiber.
    OLogShim shim = new OLogShim(new C5GeneralizedReplicator(replicator, shimFiber));
    try {
      region = regionCreator.getHRegion(basePath, regionInfo, tableDescriptor, shim, conf);
      setTabletState(State.Open);
    } catch (IOException e) {
      setTabletState(State.Failed);
      LOG.error("Settings tablet state to failed, we got an IOError opening the region:" + e.toString());
    }


  }

  private void tabletStateChangeCallback(ReplicatorInstanceEvent replicatorInstanceEvent) {
    switch (replicatorInstanceEvent.eventType) {
      case QUORUM_START:
        break;
      case LEADER_ELECTED:
        this.leader = replicatorInstanceEvent.newLeader;
        break;
      case ELECTION_TIMEOUT:
        break;
      case QUORUM_FAILURE:
        break;
      case LEADER_DEPOSED:
        this.leader = replicatorInstanceEvent.newLeader;
        break;
    }
  }

  private void tabletStateCallback(Replicator.State state) {
    switch (state) {
      case INIT:
        break;
      case FOLLOWER:
        this.setTabletState(State.Open);
        break;
      case CANDIDATE:
        this.setTabletState(State.Open);
        break;
      case LEADER:
        this.setTabletState(State.Leader);
        try {
          if (this.getRegionInfo().getRegionNameAsString().startsWith("hbase:root,")) {

            long numberOfMetaPeers = server.isSingleNodeMode() ? 1 : C5ServerConstants.DEFAULT_QUORUM_SIZE;
            RootTabletLeaderBehavior rootTabletLeaderBehavior = new RootTabletLeaderBehavior(this,
                server,
                numberOfMetaPeers);
            rootTabletLeaderBehavior.start();

          } else if (this.getRegionInfo().getRegionNameAsString().startsWith("hbase:meta,")) {
            // Have the meta leader update the root region with it being marked as the leader

            //TODO SEND TO ROOT
            MetaTabletLeaderBehavior metaTabletLeaderBehavior = new MetaTabletLeaderBehavior(server.getNodeId(), server);
            metaTabletLeaderBehavior.start();

          } else {
            userTabletLeaderBehavior.start();
          }
        } catch (Exception e) {
          LOG.error("Error setting tablet state to leader", e);
        }
        break;
    }

  }

  private void publishEvent(State newState) {
    stateChangeChannel.publish(new TabletStateChange(this, newState, null));
  }

  private void publishEvent(Throwable t) {
    stateChangeChannel.publish(new TabletStateChange(this, State.Failed, t));
  }

  private void handleFail(Throwable t) {
    tabletFiber.dispose();
    shimFiber.dispose();
    setTabletStateFailed(t);
  }

  @Override
  public Subscriber<TabletStateChange> getStateChangeChannel() {
    return this.stateChangeChannel;
  }

  @Override
  public boolean isOpen() {
    return tabletState == State.Open;
  }

  @Override
  public State getTabletState() {
    return tabletState;
  }

  public void dispose() {
    this.tabletFiber.dispose();
    shimFiber.dispose();
  }

  @Override
  public HRegionInfo getRegionInfo() {
    return this.regionInfo;
  }

  @Override
  public HTableDescriptor getTableDescriptor() {
    return tableDescriptor;
  }

  @Override
  public long getLeader() {
    return leader;
  }

  @Override
  public List<Long> getPeers() {
    return peers;
  }

  @Override
  public Region getRegion() {
    return region;
  }

  @Override
  public boolean rowInRange(byte[] row) {
    return region.rowInRange(row);
  }
}
