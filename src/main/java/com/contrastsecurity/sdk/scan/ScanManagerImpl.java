package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.ContrastSDK;
import com.google.gson.Gson;

/** Implementation of {@link ScanManager}. */
public final class ScanManagerImpl implements ScanManager {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String organizationId;
  private final ScanClientImpl client;

  /**
   * Constructor. For internal use only.
   *
   * @param contrast for making outgoing requests
   * @param gson for deserializing JSON responses
   * @param organizationId the ID of the organization in which to manage Contrast Scan resources
   */
  public ScanManagerImpl(final ContrastSDK contrast, final Gson gson, final String organizationId) {
    this.contrast = contrast;
    this.gson = gson;
    this.organizationId = organizationId;
    client = new ScanClientImpl(this.contrast, this.gson, this.organizationId);
  }

  @Override
  public Projects projects() {
    return new ProjectsImpl(organizationId, contrast, gson);
  }

  @Override
  public Scans scans(final String projectId) {
    return new ScansImpl(client, projectId);
  }

  @Override
  public CodeArtifacts codeArtifacts(final String projectId) {
    return new CodeArtifactsImpl(contrast, organizationId, projectId);
  }
}
