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

import c5db.AsyncChannelAsserts;
import c5db.C5ServerConstants;
import c5db.ConfigDirectory;
import c5db.client.generated.Condition;
import c5db.client.generated.MutationProto;
import c5db.discovery.generated.Availability;
import c5db.interfaces.C5Server;
import c5db.interfaces.DiscoveryModule;
import c5db.interfaces.ReplicationModule;
import c5db.interfaces.discovery.NewNodeVisible;
import c5db.interfaces.discovery.NodeInfo;
import c5db.interfaces.replication.Replicator;
import c5db.interfaces.tablet.Tablet;
import c5db.interfaces.tablet.TabletStateChange;
import c5db.messages.generated.ModuleType;
import c5db.util.ExceptionHandlingBatchExecutor;
import c5db.util.FiberSupplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.SettableFuture;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.util.Bytes;
import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.PoolFiberFactory;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static c5db.AsyncChannelAsserts.assertEventually;
import static c5db.AsyncChannelAsserts.listenTo;
import static c5db.TabletMatchers.hasMessageWithState;

public class TabletServiceCommandCheckTest {

  private static final String TEST_TABLE_NAME = "testTable";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery() {{
    setThreadingPolicy(new Synchroniser());
  }};
  private final Channel<NewNodeVisible> newNodeNotificationChannel = new MemoryChannel<>();
  private final SettableFuture<ImmutableMap<Long, NodeInfo>> stateFuture = SettableFuture.create();
  private final SettableFuture<Replicator> replicationFuture = SettableFuture.create();
  private final SettableFuture<DiscoveryModule> discoveryServiceFuture = SettableFuture.create();
  private final SettableFuture<ReplicationModule> replicationServiceFuture = SettableFuture.create();
  private C5Server c5Server;
  private TabletService tabletService;
  private DiscoveryModule discoveryModule;
  private ReplicationModule replicationModule;
  private ConfigDirectory config;
  private Path configDirectory;
  private Replicator replicator;

  private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  private final PoolFiberFactory fiberPool = new PoolFiberFactory(executorService);
  private final FiberSupplier fiberSupplier = (throwableConsumer) ->
      fiberPool.create(new ExceptionHandlingBatchExecutor(throwableConsumer));


  @After
  public void tearDown() {
    fiberPool.dispose();
    executorService.shutdownNow();
  }

  @Before
  public void before() throws Exception {

    config = context.mock(ConfigDirectory.class);
    configDirectory = Files.createTempDirectory(null);

    c5Server = context.mock(C5Server.class, "mockC5Server");
    discoveryModule = context.mock(DiscoveryModule.class);
    replicationModule = context.mock(ReplicationModule.class);
    replicator = context.mock(Replicator.class);
    // Reset the underlying fiber


    // Begin to initialize TabletService
    context.checking(new Expectations() {{
      allowing(config).getBaseConfigPath();
      will(returnValue(configDirectory));

      allowing(config).writeBinaryData(with(any(String.class)), with(any(String.class)), with(any(byte[].class)));
      allowing(config).writePeersToFile(with(any(String.class)), with(any(List.class)));
      allowing(config).configuredQuorums();
      will(returnValue(Arrays.asList("testTable,\\x00,1.064e3eb1da827b1dc753e03a797dba37.")));

      allowing(c5Server).getFiberSupplier();
      will(returnValue(fiberSupplier));

    }});

    tabletService = new TabletService(c5Server);

  }

  private String createTableString() {
    TableName tableName = TableName.valueOf(Bytes.toBytes(TEST_TABLE_NAME));
    HTableDescriptor testDesc = new HTableDescriptor(tableName);
    testDesc.addFamily(new HColumnDescriptor("testFamily"));
    HRegionInfo testRegion = new HRegionInfo(tableName, new byte[]{0}, new byte[]{}, false, 1);
    String peerString = "1";
    BASE64Encoder encoder = new BASE64Encoder();
    byte[] tabletDescBytes = testDesc.toByteArray();
    String hTableDesc = encoder.encodeBuffer(tabletDescBytes);

    byte[] testRegionBytes = testRegion.toByteArray();
    String hRegionInfo = encoder.encodeBuffer(testRegionBytes);

    return C5ServerConstants.CREATE_TABLE + ":" + hTableDesc + "," + hRegionInfo + "," + peerString;
  }

  @Ignore
  @Test
  public void testCreateTable() throws Throwable {
    context.checking(new Expectations() {
      {
        // Prepare for the TabletService.doStart
        oneOf(c5Server).getModule(with(ModuleType.Discovery));
        will(returnValue(discoveryServiceFuture));

        // Prepare to set the regionModule for the TabletService
        discoveryServiceFuture.set(discoveryModule);

        oneOf(c5Server).getModule(with(ModuleType.Replication));
        will(returnValue(replicationServiceFuture));

        replicationServiceFuture.set(replicationModule);

        // Begin bootstrap
        oneOf(c5Server).isSingleNodeMode();
        will(returnValue(true));

        oneOf(discoveryModule).getNewNodeNotifications();
        will(returnValue(newNodeNotificationChannel));

        oneOf(discoveryModule).getState();
        will(returnValue(stateFuture));

        oneOf(c5Server).getConfigDirectory();
        will(returnValue(config));
      }
    });

    // Prepare the config directory
    ListenableFuture<Service.State> future = tabletService.start();
    future.get();
    Channel channel = new MemoryChannel();
    Channel eventChannel = new MemoryChannel();
    context.checking(new Expectations() {
      {
        oneOf(replicationModule).createReplicator(with(any(String.class)), with(any(List.class)));
        will(returnValue(replicationFuture));

        oneOf(replicator).getStateChannel();
        will(returnValue(channel));

        allowing(replicator).getEventChannel();
        will(returnValue(eventChannel));

        allowing(replicator).getCommitNoticeChannel();

        allowing(replicator).getId();

        allowing(replicator).getQuorumId();
        will(returnValue("1"));

        allowing(c5Server).isSingleNodeMode();
        will(returnValue(true));
      }
    });

    Map<Long, NodeInfo> nodeStates = new HashMap<>();
    nodeStates.put(1l, new NodeInfo(new Availability()));
    stateFuture.set(ImmutableMap.copyOf(nodeStates));
    replicationFuture.set(replicator);

    Tablet metaTablet = context.mock(Tablet.class);
    Region metaRegion = context.mock(Region.class);
    context.checking(new Expectations() {
      {
        oneOf(metaTablet).getRegion();
        will(returnValue(metaRegion));

      }
    });
    tabletService.tabletRegistry.getTablets("hbase:meta").put(Bytes.toBytes("hbase:meta,fake"), metaTablet);
    context.checking(new Expectations() {
      {
        oneOf(metaRegion).mutate(with(any(MutationProto.class)), with(any(Condition.class)));
        oneOf(replicationModule).createReplicator(with(any(String.class)), with(any(List.class)));
        will(returnValue(true));

        will(returnValue(replicationFuture));

        allowing(replicator).getStateChannel();
        will(returnValue(channel));

        allowing(replicator).getEventChannel();
        will(returnValue(eventChannel));

        allowing(replicator).getQuorumId();
        will(returnValue("1"));

        allowing(config).writeBinaryData(with(any(String.class)), with(any(String.class)), with(any(byte[].class)));

      }
    });
    tabletService.acceptCommand(createTableString());
    AsyncChannelAsserts.ChannelListener<TabletStateChange> listener = listenTo(tabletService.getTabletStateChanges());
    assertEventually(listener, hasMessageWithState(c5db.interfaces.tablet.Tablet.State.Open));

  }

  private String addMETALeaderToRootString() {
    return C5ServerConstants.SET_META_LEADER + ":1";
  }


  private String addMetaEntryToRoot() {
    return C5ServerConstants.START_META + ":1,2,3";
  }


  @Ignore
  @Test
  public void shouldSetMetaLeader() throws Throwable {
    context.checking(new Expectations() {
      {

        // Prepare for the TabletService.doStart
        oneOf(c5Server).getModule(with(ModuleType.Discovery));
        will(returnValue(discoveryServiceFuture));

        // Prepare to set the regionModule for the TabletService
        discoveryServiceFuture.set(discoveryModule);

        oneOf(c5Server).getModule(with(ModuleType.Replication));
        will(returnValue(replicationServiceFuture));

        replicationServiceFuture.set(replicationModule);

        // Begin bootstrap
        oneOf(c5Server).isSingleNodeMode();
        will(returnValue(true));

        oneOf(discoveryModule).getNewNodeNotifications();
        will(returnValue(newNodeNotificationChannel));

        oneOf(discoveryModule).getState();
        will(returnValue(stateFuture));

        oneOf(c5Server).getConfigDirectory();
        will(returnValue(config));
      }
    });


    // Prepare the config directory
    ListenableFuture<Service.State> future = tabletService.start();
    future.get();
    SettableFuture<Replicator> replicationFuture = SettableFuture.create();

    Channel channel = new MemoryChannel();
    Channel eventChannel = new MemoryChannel();

    context.checking(new Expectations() {{
      oneOf(replicationModule).createReplicator(with(any(String.class)), with(any(List.class)));
      will(returnValue(replicationFuture));

      allowing(replicator).getStateChannel();
      will(returnValue(channel));

      allowing(replicator).getEventChannel();
      will(returnValue(eventChannel));

      allowing(replicator).getCommitNoticeChannel();

      allowing(replicator).getId();

      allowing(replicator).getQuorumId();
      will(returnValue("1"));

      allowing(config).writeBinaryData(with(any(String.class)), with(any(String.class)), with(any(byte[].class)));

    }});
    AsyncChannelAsserts.ChannelListener<TabletStateChange> listener = listenTo(tabletService.getTabletStateChanges());
    replicationFuture.set(replicator);

    tabletService.acceptCommand(addMetaEntryToRoot());
    assertEventually(listener, hasMessageWithState(c5db.interfaces.tablet.Tablet.State.Open));

    Tablet tablet = context.mock(Tablet.class);
    Region region = context.mock(Region.class);
    tabletService.tabletRegistry.getTablets("hbase:root").put(Bytes.toBytes("hbase:root,fake"), tablet);


    // We have to use allowing because we have no way of waiting for the meta to update currently
    context.checking(new Expectations() {{
      allowing(tablet).getRegion();
      will(returnValue(region));

      // This is where we update meta leader
      allowing(region).mutate(with(any(MutationProto.class)), with(any(Condition.class)));
      will(returnValue(true));
    }});
    tabletService.acceptCommand(addMETALeaderToRootString());
  }
}
