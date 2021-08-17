package com.contrastsecurity.sdk.scan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Unit tests for {@link ScanImpl}. */
final class ScanImplTest implements EqualsContract<ScanImpl> {

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
            .organizationId("organization-id")
            .status(ScanStatus.COMPLETED)
            .build();
    final Scan scan = new ScanImpl(client, inner);

    // WHEN save sarif to file
    final Path file = tmp.resolve("contrast-scan-results.sarif.json");
    scan.saveSarif(file);

    // THEN file has expected contents
    assertThat(file).exists().hasContent("sarif");
  }

  @Nested
  final class AwaitTest {

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
      return ScanInner.builder()
          .id(SCAN_ID)
          .projectId(PROJECT_ID)
          .organizationId("organization-id");
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
        .organizationId("organization-id")
        .status(ScanStatus.COMPLETED);
  }

  private static final String PROJECT_ID = "project-id";
  private static final String SCAN_ID = "scan-id";
  private static final Duration TEST_INTERVAL = Duration.ofMillis(1);
  private static final Duration TEST_TIMEOUT = Duration.ofMillis(100);
}
