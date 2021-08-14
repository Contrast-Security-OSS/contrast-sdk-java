package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.scan.Scan.Status;

/** Unit tests for {@link ScanImpl}. */
final class ScanImplTest implements EqualsContract<ScanImpl> {

  @Override
  public ScanImpl createValue() {
    final ScanImpl.Value scanValue = builder().build();
    return new ScanImpl(contrast(), scanValue);
  }

  @Override
  public ScanImpl createNotEqualValue() {
    final ScanImpl.Value scanValue = builder().status(Status.FAILED).errorMessage("failed").build();
    return new ScanImpl(contrast(), scanValue);
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
