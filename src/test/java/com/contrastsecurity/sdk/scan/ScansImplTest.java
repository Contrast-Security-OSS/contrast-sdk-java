package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static com.contrastsecurity.sdk.scan.ScanAssert.assertThat;
import static com.contrastsecurity.sdk.scan.ScanSummaryAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.contrastsecurity.EqualsAndHashcodeContract;
import com.contrastsecurity.TestDataConstants;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.EnumSet;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link ScansImpl} and {@link ScanImpl}. Tested together because these classes are
 * tightly coupled.
 */
final class ScansImplTest implements EqualsAndHashcodeContract<ScanImpl> {

  /** Unit tests for {@link Scans#define()} */
  @Test
  void define_scan() throws IOException {
    // GIVEN stubbed scan client
    final ScanClient client = mock(ScanClient.class);
    final ScanInner inner = builder().status(ScanStatus.WAITING).build();
    final ScanCreate create = ScanCreate.of("code-artifact-id", "main");
    when(client.create(inner.projectId(), create)).thenReturn(inner);

    // WHEN define new scan
    final Scans scans = new ScansImpl(client, inner.projectId());
    final Scan scan =
        scans
            .define()
            .withExistingCodeArtifact(create.codeArtifactId())
            .withLabel(create.label())
            .create();

    // THEN scan has expected values
    assertThat(scan).hasSameValuesAsInner(inner);
  }

  /** Unit tests for {@link Scans#get(String)} */
  @Test
  void get_scan() throws IOException {
    // GIVEN stubbed scan client
    final ScanClient client = mock(ScanClient.class);
    final ScanInner inner = builder().status(ScanStatus.WAITING).build();
    when(client.get(inner.projectId(), inner.id())).thenReturn(inner);

    // WHEN retrieve scan
    final Scans scans = new ScansImpl(client, inner.projectId());
    final Scan scan = scans.get(inner.id());

    // THEN scan has expected values
    assertThat(scan).hasSameValuesAsInner(inner);
  }

  @Nested
  final class Summary {

    private ScanClient client;
    private ScanSummaryInner summaryInner;

    @BeforeEach
    void before() throws IOException {
      summaryInner =
          ScanSummaryInner.builder()
              .id("summary-id")
              .scanId(SCAN_ID)
              .projectId(PROJECT_ID)
              .organizationId(ORGANIZATION_ID)
              .duration(100)
              .createdDate(TestDataConstants.TIMESTAMP_EXAMPLE)
              .lastModifiedDate(TestDataConstants.TIMESTAMP_EXAMPLE)
              .totalFixedResults(0)
              .totalNewResults(0)
              .totalResults(0)
              .build();
      client = mock(ScanClient.class);
      when(client.getSummary(summaryInner.projectId(), summaryInner.scanId()))
          .thenReturn(summaryInner);
    }

    @Test
    void retrieve_summary() throws IOException {
      // WHEN retrieve summary by scan ID
      final ScansImpl scans = new ScansImpl(client, PROJECT_ID);
      final ScanSummary summary = scans.summary(SCAN_ID);

      // THEN returns expected summary
      assertThat(summary).hasSameValuesAsInner(summaryInner);
    }

    @Test
    void traverse_summary() throws IOException {
      // WHEN traverse summary relationship from a scan
      final ScanInner inner = builder().status(ScanStatus.COMPLETED).build();
      final Scan scan = new ScanImpl(client, inner);
      final ScanSummary summary = scan.summary();

      // THEN returns expected summary
      assertThat(summary).hasSameValuesAsInner(summaryInner);
    }
  }

  @Test
  void save_sarif_to_file(@TempDir final Path tmp) throws IOException {
    // GIVEN a scan resource and a stubbed scan client that returns SARIF for the given resource
    final ScanClient client = mock(ScanClient.class);
    when(client.getSarif(PROJECT_ID, SCAN_ID))
        .thenReturn(new ByteArrayInputStream("sarif".getBytes(StandardCharsets.UTF_8)));
    final ScanInner inner =
        ScanInner.builder()
            .id(SCAN_ID)
            .projectId(PROJECT_ID)
            .organizationId(ORGANIZATION_ID)
            .status(ScanStatus.COMPLETED)
            .build();
    final Scan scan = new ScanImpl(client, inner);

    // WHEN save sarif to file
    final Path file = tmp.resolve("contrast-scan-results.sarif.json");
    scan.saveSarif(file);

    // THEN file has expected contents
    assertThat(file).exists().hasContent("sarif");
  }

  /** @see #is_finished(ScanStatus) */
  static Collection<ScanStatus> finished() {
    return EnumSet.of(ScanStatus.CANCELLED, ScanStatus.COMPLETED, ScanStatus.FAILED);
  }

  /** Verifies that completed scans and failed scans are finished */
  @MethodSource("finished")
  @ParameterizedTest
  void is_finished(final ScanStatus status) {
    final ScanInner inner = builder().status(status).build();
    final ScanClient client = mock(ScanClient.class);
    final Scan scan = new ScanImpl(client, inner);
    assertThat(scan.isFinished()).isTrue();
  }

  /** @see #is_not_yet_finished(ScanStatus) */
  static Collection<ScanStatus> notFinished() {
    return EnumSet.of(ScanStatus.WAITING, ScanStatus.RUNNING);
  }

  /** Verifies that waiting scans and running scans are not yet finished */
  @MethodSource("notFinished")
  @ParameterizedTest
  void is_not_yet_finished(final ScanStatus status) {
    final ScanInner inner = builder().status(status).build();
    final ScanClient client = mock(ScanClient.class);
    final Scan scan = new ScanImpl(client, inner);
    assertThat(scan.isFinished()).isFalse();
  }

  @Nested
  final class UnfinishedScanInvariants {

    private Scan unfinished;

    @BeforeEach
    void before() {
      final ScanClient client = mock(ScanClient.class);
      final ScanInner inner = builder().status(ScanStatus.WAITING).build();
      unfinished = new ScanImpl(client, inner);
    }

    @Test
    void get_sarif_throws_when_scan_not_complete(@TempDir final Path tmp) {
      final Path file = tmp.resolve("contrast-scan-results.sarif.json");
      assertThatThrownBy(() -> unfinished.saveSarif(file))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void save_sarif_throws_when_scan_not_complete() {
      assertThatThrownBy(() -> unfinished.sarif()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void get_summary_throws_when_scan_not_complete() {
      assertThatThrownBy(() -> unfinished.summary()).isInstanceOf(IllegalStateException.class);
    }
  }

  @Nested
  final class Await {

    private ScheduledExecutorService scheduler;
    private ScanClient client;

    @BeforeEach
    void before() {
      scheduler = Executors.newSingleThreadScheduledExecutor();
      client = mock(ScanClient.class);
    }

    @AfterEach
    void after() {
      scheduler.shutdownNow();
    }

    @Test
    void completes_successfully_when_scan_is_completed() throws IOException {
      // GIVEN the scan has already been completed
      final ScanInner completed = builder().status(ScanStatus.COMPLETED).build();
      when(client.get(PROJECT_ID, SCAN_ID)).thenReturn(completed);

      // WHEN await scan to finish
      final ScanImpl scan = new ScanImpl(client, completed);
      final CompletionStage<Scan> future = scan.await(scheduler, TEST_INTERVAL);

      // THEN succeeds
      assertThat(future).succeedsWithin(TEST_TIMEOUT).isEqualTo(scan);
    }

    @Test
    void completes_successfully_when_scan_eventually_completes()
        throws UnauthorizedException, IOException {
      // GIVEN the scan has not yet started, then starts, then completes
      final ScanInner waiting = builder().status(ScanStatus.WAITING).build();
      final ScanInner running = builder().status(ScanStatus.RUNNING).build();
      final ScanInner completed = builder().status(ScanStatus.COMPLETED).build();
      when(client.get(PROJECT_ID, SCAN_ID)).thenReturn(waiting, running, completed);

      // WHEN await scan to finish
      final ScanImpl scan = new ScanImpl(client, waiting);
      final CompletionStage<Scan> future = scan.await(scheduler, TEST_INTERVAL);

      // THEN completes successfully
      final ScanImpl expected = new ScanImpl(client, completed);
      assertThat(future).succeedsWithin(TEST_TIMEOUT).isEqualTo(expected);
    }

    @Test
    void completes_exceptionally_when_scan_has_failed() throws UnauthorizedException, IOException {
      // GIVEN the scan has already failed
      final ScanInner failed =
          builder().status(ScanStatus.FAILED).errorMessage("DNS exploded again").build();
      when(client.get(PROJECT_ID, SCAN_ID)).thenReturn(failed);

      // WHEN await scan to finish
      final ScanImpl scan = new ScanImpl(client, failed);
      final CompletionStage<Scan> future = scan.await(scheduler, TEST_INTERVAL);

      // THEN fails
      assertThat(future).failsWithin(TEST_TIMEOUT);
    }

    @Test
    void completes_exceptionally_when_scan_eventually_fails()
        throws UnauthorizedException, IOException {
      // GIVEN the scan has not yet started, then starts, then fails
      final ScanInner waiting = builder().status(ScanStatus.WAITING).build();
      final ScanInner running = builder().status(ScanStatus.RUNNING).build();
      final ScanInner failed =
          builder().status(ScanStatus.FAILED).errorMessage("DNS exploded again").build();
      when(client.get(PROJECT_ID, SCAN_ID)).thenReturn(waiting, running, failed);

      // WHEN await scan to finish
      final ScanImpl scan = new ScanImpl(client, waiting);
      final CompletionStage<Scan> future = scan.await(scheduler, TEST_INTERVAL);

      // THEN completes exceptionally
      assertThat(future)
          .failsWithin(TEST_TIMEOUT)
          .withThrowableOfType(ExecutionException.class)
          .withCauseExactlyInstanceOf(ScanException.class)
          .havingCause()
          .withMessage("DNS exploded again");
    }

    @Test
    void completes_exceptionally_when_get_scan_throws() throws UnauthorizedException, IOException {
      // GIVEN the scan has not yet started, then starts, then fails
      final ScanInner waiting = builder().status(ScanStatus.WAITING).build();
      final ScanInner running = builder().status(ScanStatus.RUNNING).build();
      when(client.get(PROJECT_ID, SCAN_ID))
          .thenReturn(waiting, running, running)
          .thenThrow(new IOException("NIC melted"));

      // WHEN await scan to finish
      final ScanImpl scan = new ScanImpl(client, waiting);
      final CompletionStage<Scan> future = scan.await(scheduler, TEST_INTERVAL);

      // THEN completes exceptionally
      assertThat(future)
          .failsWithin(TEST_TIMEOUT)
          .withThrowableOfType(ExecutionException.class)
          .withCauseExactlyInstanceOf(UncheckedIOException.class)
          .havingCause()
          .withCauseExactlyInstanceOf(IOException.class)
          .havingCause()
          .withMessage("NIC melted");
    }

    private ScanInner.Builder builder() {
      return ScanInner.builder().id(SCAN_ID).projectId(PROJECT_ID).organizationId(ORGANIZATION_ID);
    }
  }

  @Override
  public ScanImpl createValue() {
    final ScanInner scanValue = builder().build();
    return new ScanImpl(mock(ScanClient.class), scanValue);
  }

  @Override
  public ScanImpl createNotEqualValue() {
    final ScanInner scanValue = builder().status(ScanStatus.FAILED).errorMessage("failed").build();
    return new ScanImpl(mock(ScanClient.class), scanValue);
  }

  private static ScanInner.Builder builder() {
    return ScanInner.builder()
        .id(SCAN_ID)
        .projectId(PROJECT_ID)
        .organizationId(ORGANIZATION_ID)
        .status(ScanStatus.COMPLETED);
  }

  private static final String ORGANIZATION_ID = "organization-id";
  private static final String PROJECT_ID = "project-id";
  private static final String SCAN_ID = "scan-id";
  private static final Duration TEST_INTERVAL = Duration.ofMillis(1);
  private static final Duration TEST_TIMEOUT = Duration.ofMillis(100);
}
