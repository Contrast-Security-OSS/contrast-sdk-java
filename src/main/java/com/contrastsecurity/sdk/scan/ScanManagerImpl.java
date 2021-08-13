package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.ContrastSDK;

public final class ScanManagerImpl implements ScanManager {

  private final ContrastSDK contrast;
  private final String organizationId;

  public ScanManagerImpl(final ContrastSDK contrast, final String organizationId) {
    this.contrast = contrast;
    this.organizationId = organizationId;
  }

  @Override
  public Projects projects() {
    return new ProjectsImpl(organizationId, contrast);
  }
}
