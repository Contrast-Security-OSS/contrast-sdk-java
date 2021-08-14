package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.scan.Scan.Definition;
import com.google.gson.Gson;
import java.util.Objects;

/** Implementation of the {@link Scans} resource collection. */
final class ScansImpl implements Scans {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String organizationId;
  private final String projectId;

  ScansImpl(
      final ContrastSDK contrast,
      final Gson gson,
      final String organizationId,
      final String projectId) {
    this.contrast = Objects.requireNonNull(contrast);
    this.gson = Objects.requireNonNull(gson);
    this.organizationId = Objects.requireNonNull(organizationId);
    this.projectId = Objects.requireNonNull(projectId);
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
