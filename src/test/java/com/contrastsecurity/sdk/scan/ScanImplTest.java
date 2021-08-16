package com.contrastsecurity.sdk.scan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.sdk.ContrastSDK;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Unit tests for {@link ScanImpl}. */
final class ScanImplTest implements EqualsContract<ScanImpl> {

  @Test
  void save_sarif_to_file(@TempDir final Path tmp) throws IOException {
    // GIVEN a scan resource and a stubbed scan client that returns SARIF for the given resource
    final ScanClient client = mock(ScanClient.class);
    when(client.getSarif("project-id", "scan-id"))
        .thenReturn(new ByteArrayInputStream("sarif".getBytes(StandardCharsets.UTF_8)));
    final ScanInner inner =
        ScanInner.builder()
            .id("scan-id")
            .projectId("project-id")
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
        .id("scan-id")
        .projectId("project-id")
        .organizationId("organization-id")
        .status(ScanStatus.COMPLETED);
  }

  private static ContrastSDK contrast() {
    return new ContrastSDK.Builder("test-user", "test-service-key", "test-api-key").build();
  }
}
