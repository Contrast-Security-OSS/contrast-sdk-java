package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.scan.Scan.Status;

/** Unit tests for {@link ScanImpl}. */
final class ScanImplTest implements EqualsContract<ScanImpl> {

  @Override
  public ScanImpl createValue() {
    return new ScanImpl(contrast(), "scan-id", Status.COMPLETED, null);
  }

  @Override
  public ScanImpl createNotEqualValue() {
    return new ScanImpl(contrast(), "scan-id", Status.FAILED, "failed");
  }

  private static ContrastSDK contrast() {
    return new ContrastSDK.Builder("test-user", "test-service-key", "test-api-key").build();
  }
}
