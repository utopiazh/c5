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

package c5db.log;

import c5db.generated.OLogContentType;
import c5db.interfaces.log.SequentialEntryCodec;
import c5db.interfaces.replication.QuorumConfiguration;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static c5db.log.EntryEncodingUtil.sumRemaining;
import static c5db.log.LogTestUtil.makeSingleEntryList;
import static c5db.log.LogTestUtil.someConsecutiveEntries;
import static c5db.log.ReplicatorLogGenericTestUtil.someData;
import static c5db.replication.ReplicatorTestUtil.makeConfigurationEntry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class OLogEntryDescriptionTest {
  private final ByteArrayPersistence persistence = new ByteArrayPersistence();
  private final SequentialEntryCodec<OLogEntry> codec = new OLogEntry.Codec();
  private final SequentialEntryCodec<OLogEntryDescription> descriptionCodec = new OLogEntryDescription.Codec();
  private final SequentialLog<OLogEntry> log = new EncodedSequentialLog<>(
      persistence,
      codec,
      new InMemoryPersistenceNavigator<>(persistence, codec));
  private final SequentialLog<OLogEntryDescription> descriptionLog = new EncodedSequentialLog<>(
      persistence,
      descriptionCodec,
      new InMemoryPersistenceNavigator<>(persistence, descriptionCodec));

  private final ByteBuffer rawData = someData();

  @Test
  public void returnsADescriptionOfALoggedEntry() throws Exception {
    long arbitrarySeqNum = 11;
    long arbitraryTerm = 22;

    log.append(makeSingleEntryList(arbitrarySeqNum, arbitraryTerm, rawData));

    assertThat(descriptionLog.getLastEntry(), is(equalTo(
        new OLogEntryDescription(
            arbitrarySeqNum,
            arbitraryTerm,
            rawData.remaining(),
            OLogContentType.DATA,
            true,
            true,
            null)
    )));
  }

  @Test
  public void returnsTheCorrectQuorumConfigurationForSuchAnEntry() throws Exception {
    QuorumConfiguration config = aQuorumConfiguration();
    log.append(singleEntryListForConfiguration(config));

    assertThat(descriptionLog.getLastEntry().getQuorumConfiguration(), is(equalTo(config)));
  }

  @Test
  public void detectsThatLoggedContentHasBeenCorruptedWhenDescribingIt() throws Exception {
    long arbitrarySeqNum = 11;
    long arbitraryTerm = 22;

    log.append(makeSingleEntryList(arbitrarySeqNum, arbitraryTerm, rawData));

    // Hack to find the content within the logged data: assume 4-byte ending CRC, so subtract 5
    // Also assume that the existing byte value at that location is different than zero.
    int numberOfBytesFromEndOfPersistence = 5;
    int bytePositionToCorrupt = (int) persistence.size() - numberOfBytesFromEndOfPersistence;
    persistence.corrupt(bytePositionToCorrupt);

    assertThat(descriptionLog.getLastEntry(), is(equalTo(
        new OLogEntryDescription(
            arbitrarySeqNum,
            arbitraryTerm,
            rawData.remaining(),
            OLogContentType.DATA,
            true,
            false, // error
            null)
    )));
  }

  @Test
  public void worksWithForEachMethodToDescribeTheEntireContentsOfTheLog() throws Exception {
    List<OLogEntry> entries = someConsecutiveEntries(10, 20);
    log.append(entries);

    List<OLogEntryDescription> descriptions = new ArrayList<>();
    descriptionLog.forEach(descriptions::add);

    assertThat(descriptions, is(equalTo(descriptionsCorrespondingTo(entries))));
  }

  private List<OLogEntryDescription> descriptionsCorrespondingTo(List<OLogEntry> entries) {
    return Lists.transform(entries, (entry) ->
        new OLogEntryDescription(entry.getSeqNum(), entry.getElectionTerm(), entryDataContentLength(entry),
            entry.getContent().getType(), true, true, null));
  }

  private int entryDataContentLength(OLogEntry entry) {
    return sumRemaining(entry.getContent().serialize());
  }

  private QuorumConfiguration aQuorumConfiguration() {
    return QuorumConfiguration.of(Sets.newHashSet(1L, 2L, 3L))
        .getTransitionalConfiguration(Sets.newHashSet(4L, 5L, 6L));
  }

  private List<OLogEntry> singleEntryListForConfiguration(QuorumConfiguration configuration) {
    return Lists.newArrayList(OLogEntry.fromProtostuff(makeConfigurationEntry(0, 0, configuration)));
  }
}
