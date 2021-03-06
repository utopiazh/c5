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

package c5db.replication;

import c5db.C5CommonTestUtil;
import c5db.MiscMatchers;
import c5db.ReplicatorConstants;
import c5db.SimpleModuleInformationProvider;
import c5db.discovery.BeaconService;
import c5db.interfaces.DiscoveryModule;
import c5db.interfaces.LogModule;
import c5db.interfaces.ReplicationModule;
import c5db.interfaces.replication.GeneralizedReplicator;
import c5db.interfaces.replication.ReplicateSubmissionInfo;
import c5db.log.LogService;
import c5db.log.ReplicatorLogGenericTestUtil;
import c5db.util.C5Futures;
import c5db.util.ExceptionHandlingBatchExecutor;
import c5db.util.FiberSupplier;
import c5db.util.JUnitRuleFiberExceptions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.hamcrest.Matcher;
import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.PoolFiberFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static c5db.CollectionMatchers.isStrictlyIncreasing;
import static c5db.FutureMatchers.resultsIn;
import static c5db.FutureMatchers.returnsAFutureWhoseResult;
import static com.google.common.util.concurrent.Futures.allAsList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class C5GeneralizedReplicationServiceTest {
  @Rule
  public JUnitRuleFiberExceptions jUnitFiberExceptionHandler = new JUnitRuleFiberExceptions();

  private static final int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();
  private static final int DISCOVERY_PORT = 54333;

  private final Path baseTestPath = new C5CommonTestUtil().getDataTestDir("general-replicator-test");

  private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_PROCESSORS);

  private final PoolFiberFactory fiberFactory = new PoolFiberFactory(executorService);
  private final Set<Fiber> fibers = new HashSet<>();
  private final Fiber mainTestFiber = newFiber(jUnitFiberExceptionHandler);

  @Before
  public void setupConfigDirectory() throws Exception {
    mainTestFiber.start();
  }

  @After
  public void disposeOfResources() throws Exception {
    fiberFactory.dispose();
    executorService.shutdownNow();
    fibers.forEach(Fiber::dispose);
  }

  @Test(timeout = 9000)
  public void logsToASingleQuorumReplicator() throws Exception {
    List<Long> nodeIds = Lists.newArrayList(1L);

    try (QuorumOfReplicatorsController controller = newQuorum(nodeIds)) {

      GeneralizedReplicator replicator = controller.waitUntilAReplicatorIsReady();

      ListenableFuture<List<ReplicateSubmissionInfo>> resultListFuture =
          resultFutureForNReplicateRequests(replicator, 3);

      assertThat(sequenceNumberListFuture(resultListFuture), resultsInAListOfLongsThat(hasSize(3)));
      assertThat(sequenceNumberListFuture(resultListFuture), resultsInAListOfLongsThat(isStrictlyIncreasing()));
      assertThat(resultListFuture, allRequestsWillComplete());
    }
  }

  @Test(timeout = 9000)
  public void replicatesAcrossAQuorumComposedOfThreeReplicators() throws Exception {
    List<Long> nodeIds = Lists.newArrayList(1L, 2L, 3L);

    try (QuorumOfReplicatorsController controller = newQuorum(nodeIds)) {

      GeneralizedReplicator replicator = controller.waitUntilAReplicatorIsReady();

      ListenableFuture<List<ReplicateSubmissionInfo>> resultListFuture =
          resultFutureForNReplicateRequests(replicator, 3);

      assertThat(sequenceNumberListFuture(resultListFuture), resultsInAListOfLongsThat(hasSize(3)));
      assertThat(sequenceNumberListFuture(resultListFuture), resultsInAListOfLongsThat(isStrictlyIncreasing()));
      assertThat(resultListFuture, allRequestsWillComplete());
    }
  }


  private Fiber newFiber(Consumer<Throwable> throwableHandler) {
    Fiber newFiber = fiberFactory.create(new ExceptionHandlingBatchExecutor(throwableHandler));
    fibers.add(newFiber);
    return newFiber;
  }

  private List<ByteBuffer> someData() {
    return Lists.newArrayList(ReplicatorLogGenericTestUtil.someData());
  }

  private QuorumOfReplicatorsController newQuorum(Collection<Long> nodeIds) throws Exception {
    return new QuorumOfReplicatorsController(nodeIds, baseTestPath, mainTestFiber, this::newFiber, jUnitFiberExceptionHandler);
  }

  private ListenableFuture<List<ReplicateSubmissionInfo>> resultFutureForNReplicateRequests(
      GeneralizedReplicator replicator, int numberOfReplicateRequests) throws Exception {

    List<ListenableFuture<ReplicateSubmissionInfo>> replicateFutures =
        new ArrayList<ListenableFuture<ReplicateSubmissionInfo>>() {{
          for (int i = 0; i < numberOfReplicateRequests; i++) {
            add(replicator.replicate(someData()));
          }
        }};

    return allAsList(replicateFutures);
  }

  private ListenableFuture<List<Long>> sequenceNumberListFuture(ListenableFuture<List<ReplicateSubmissionInfo>>
                                                                    resultListFuture) {
    return Futures.transform(resultListFuture,
        (List<ReplicateSubmissionInfo> resultList) ->
            resultList.stream()
                .map((result) -> result.sequenceNumber)
                .collect(Collectors.toList()));
  }

  private static Matcher<? super ListenableFuture<List<Long>>> resultsInAListOfLongsThat(
      Matcher<? super List<Long>> longsMatcher) {
    return resultsIn(longsMatcher);
  }

  private static Matcher<? super ListenableFuture<List<ReplicateSubmissionInfo>>> allRequestsWillComplete() {
    return returnsAFutureWhoseResult(
        MiscMatchers.simpleMatcherForPredicate(
            (List<ReplicateSubmissionInfo> resultList) -> {
              for (ReplicateSubmissionInfo result : resultList) {
                if (!resultsIn(equalTo(null)).matches(result.completedFuture)) {
                  return false;
                }
              }
              return true;
            },
            (description) -> description.appendText("a list of ReplicateSubmissionInfo each indicating the " +
                "requests have completed")
        ));
  }

  /**
   * Runs a C5GeneralizedReplicationService and handles startup and disposal,
   * for the purpose of making tests more readable
   */
  private static class SingleReplicatorController implements AutoCloseable {
    private static final String QUORUM_ID = "quorumId";

    public final C5GeneralizedReplicationService service;
    public final GeneralizedReplicator replicator;

    private final SimpleModuleInformationProvider moduleInfo;
    private final ReplicationModule replicationModule;
    private final LogModule logModule;
    private final DiscoveryModule nodeInfoModule;

    public SingleReplicatorController(int port,
                                      long nodeId,
                                      Collection<Long> peerIds,
                                      Path baseTestPath,
                                      Fiber testFiber,
                                      FiberSupplier fiberSupplier,
                                      Consumer<Throwable> exceptionHandler,
                                      EventLoopGroup bossGroup,
                                      EventLoopGroup workerGroup) throws Exception {

      moduleInfo = new SimpleModuleInformationProvider(testFiber, exceptionHandler);

      replicationModule =
          new ReplicatorService(bossGroup, workerGroup, nodeId, port, moduleInfo, fiberSupplier,
              new NioQuorumFileReaderWriter(baseTestPath));
      logModule = new LogService(baseTestPath, fiberSupplier);
      nodeInfoModule = new BeaconService(nodeId, DISCOVERY_PORT, workerGroup, moduleInfo, fiberSupplier);

      startAll();

      service = new C5GeneralizedReplicationService(replicationModule, logModule, fiberSupplier);
      replicator = service.createReplicator(QUORUM_ID, peerIds).get();
    }

    @Override
    public void close() {
      service.dispose();
      stopAll();
    }

    private void startAll() throws Exception {
      List<ListenableFuture<Service.State>> startFutures = new ArrayList<>();

      startFutures.add(moduleInfo.startModule(logModule));
      startFutures.add(moduleInfo.startModule(nodeInfoModule));
      startFutures.add(moduleInfo.startModule(replicationModule));

      ListenableFuture<List<Service.State>> allFutures = allAsList(startFutures);

      // Block waiting for everything to start.
      allFutures.get();
    }

    private void stopAll() {
      replicationModule.stopAndWait();
      nodeInfoModule.stopAndWait();
      logModule.stopAndWait();
    }
  }

  /**
   * Runs a complete quorum of C5GeneralizedReplicationService and handles startup and disposal,
   * for the purpose of making tests more readable
   */
  private static class QuorumOfReplicatorsController implements AutoCloseable {
    private final Collection<Long> peerIds;
    private final Path baseTestPath;
    private final Fiber testFiber;
    private final FiberSupplier fiberSupplier;
    private final Consumer<Throwable> exceptionHandler;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(NUMBER_OF_PROCESSORS / 3);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(NUMBER_OF_PROCESSORS / 3);

    private final Map<Long, SingleReplicatorController> controllers = new HashMap<>();
    private final Map<Long, GeneralizedReplicator> replicators = new HashMap<>();

    public QuorumOfReplicatorsController(Collection<Long> peerIds,
                                         Path baseTestPath,
                                         Fiber testFiber,
                                         FiberSupplier fiberSupplier,
                                         Consumer<Throwable> exceptionHandler) throws Exception {

      this.peerIds = peerIds;
      this.baseTestPath = baseTestPath;
      this.testFiber = testFiber;
      this.fiberSupplier = fiberSupplier;
      this.exceptionHandler = exceptionHandler;

      createControllersForEachPeerId();
    }

    @Override
    public void close() {
      controllers.values().forEach(SingleReplicatorController::close);

      // Initiate shut down but don't wait for termination, for the sake of test speed.
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }

    public GeneralizedReplicator waitUntilAReplicatorIsReady() throws Exception {
      SettableFuture<GeneralizedReplicator> readyReplicatorFuture = SettableFuture.create();

      for (GeneralizedReplicator replicator : replicators.values()) {
        final ListenableFuture<Void> isAvailableFuture = replicator.isAvailableFuture();

        C5Futures.addCallback(isAvailableFuture,
            (ignore) -> readyReplicatorFuture.set(replicator),
            readyReplicatorFuture::setException,
            testFiber);
      }

      return readyReplicatorFuture.get();
    }

    private void createControllersForEachPeerId() throws Exception {
      int port = ReplicatorConstants.REPLICATOR_PORT_MIN;

      for (long nodeId : peerIds) {
        SingleReplicatorController controller = new SingleReplicatorController(port, nodeId, peerIds,
            baseTestPath, testFiber, fiberSupplier, exceptionHandler, bossGroup, workerGroup);
        controllers.put(nodeId, controller);

        port++;
        replicators.put(nodeId, controller.replicator);
      }
    }
  }
}
