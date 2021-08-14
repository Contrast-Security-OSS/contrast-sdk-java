package com.contrastsecurity.sdk.scan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.RefreshById;
import com.contrastsecurity.sdk.scan.Scan.Status;
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
    // GIVEN a scan resource and a stubbed ContrastSDK that returns SARIF for the given resource
    final ContrastSDK contrast = mock(ContrastSDK.class);
    when(contrast.makeRequest(
            HttpMethod.GET,
            "/sast/organizations/organization-id/projects/project-id/scans/scan-id/raw-output"))
        .thenReturn(new ByteArrayInputStream("sarif".getBytes(StandardCharsets.UTF_8)));
    final ScanImpl.Value value =
        ScanImpl.Value.builder()
            .id("scan-id")
            .projectId("project-id")
            .organizationId("organization-id")
            .status(Status.COMPLETED)
            .build();
    final Scan scan = new ScanImpl(contrast, RefreshById.unsupported(), value);

    // WHEN save sarif to file
    final Path file = tmp.resolve("contrast-scan-results.sarif.json");
    scan.saveSarif(file);

    // THEN file has expected contents
    assertThat(file).exists().hasContent("sarif");
  }

  @Override
  public ScanImpl createValue() {
    final ScanImpl.Value scanValue = builder().build();
    return new ScanImpl(contrast(), RefreshById.unsupported(), scanValue);
  }

  @Override
  public ScanImpl createNotEqualValue() {
    final ScanImpl.Value scanValue = builder().status(Status.FAILED).errorMessage("failed").build();
    return new ScanImpl(contrast(), RefreshById.unsupported(), scanValue);
  }

  private static ScanImpl.Value.Builder builder() {
    return ScanImpl.Value.builder()
        .id("scan-id")
        .projectId("project-id")
        .organizationId("organization-id")
        .status(Status.COMPLETED);
  }

  private static ContrastSDK contrast() {
    return new ContrastSDK.Builder("test-user", "test-service-key", "test-api-key").build();
  }
}
