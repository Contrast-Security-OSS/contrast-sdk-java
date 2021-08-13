package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;

public final class ScanManagerImpl implements ScanManager {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String organizationId;

  public ScanManagerImpl(final ContrastSDK contrast, final String organizationId) {
    this.contrast = contrast;
    this.gson =
        GsonFactory.builder()
            .registerTypeAdapter(
                ProjectImpl.class, (InstanceCreator<ProjectImpl>) type -> new ProjectImpl(contrast))
            .create();
    this.organizationId = organizationId;
  }

  @Override
  public Projects projects() {
    return new ProjectsImpl(organizationId, contrast, gson);
  }
}
