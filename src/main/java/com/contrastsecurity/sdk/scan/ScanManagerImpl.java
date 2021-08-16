package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.ContrastSDK;
import com.google.gson.Gson;
import java.util.Objects;

/** Implementation of {@link ScanManager}. */
public final class ScanManagerImpl implements ScanManager {

  private final CodeArtifacts.Factory codeArtifactsFactory;
  private final Scans.Factory scansFactory;
  private final Projects projects;

  /**
   * Constructor. For internal use only.
   *
   * @param contrast for making outgoing requests
   * @param gson for deserializing JSON responses
   * @param organizationId the ID of the organization in which to manage Contrast Scan resources
   */
  public ScanManagerImpl(final ContrastSDK contrast, final Gson gson, final String organizationId) {
    Objects.requireNonNull(contrast);
    Objects.requireNonNull(gson);
    Objects.requireNonNull(organizationId);

    final ProjectClient projectClient = new ProjectClientImpl(contrast, gson, organizationId);
    final CodeArtifactClient codeArtifactClient =
        new CodeArtifactClientImpl(contrast, gson, organizationId);
    codeArtifactsFactory = new CodeArtifactsImpl.Factory(codeArtifactClient);
    final ScanClient scanClient = new ScanClientImpl(contrast, gson, organizationId);
    scansFactory = new ScansImpl.Factory(scanClient);
    projects = new ProjectsImpl(codeArtifactsFactory, scansFactory, projectClient);
  }

  @Override
  public Projects projects() {
    return projects;
  }

  @Override
  public Scans scans(final String projectId) {
    return scansFactory.create(projectId);
  }

  @Override
  public CodeArtifacts codeArtifacts(final String projectId) {
    return codeArtifactsFactory.create(projectId);
  }
}
