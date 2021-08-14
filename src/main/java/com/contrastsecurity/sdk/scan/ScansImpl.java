package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.contrastsecurity.sdk.scan.Scan.Definition;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;

/** Implementation of the {@link Scans} resource collection. */
final class ScansImpl implements Scans {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String organizationId;
  private final String projectId;

  ScansImpl(final ContrastSDK contrast, final String organizationId, final String projectId) {
    this.contrast = contrast;
    this.gson =
        GsonFactory.builder()
            .registerTypeAdapter(
                ScanImpl.class, (InstanceCreator<ScanImpl>) type -> new ScanImpl(contrast))
            .create();
    this.organizationId = organizationId;
    this.projectId = projectId;
  }

  @Override
  public Definition define() {
    return new ScanImpl.Definition(contrast, gson, organizationId, projectId);
  }

  @Override
  public Scan get(final String id) {
    // TODO
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
