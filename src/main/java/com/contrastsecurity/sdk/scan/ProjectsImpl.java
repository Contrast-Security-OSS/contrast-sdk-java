package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.scan.Project.Definition;
import com.contrastsecurity.sdk.scan.Scans.Factory;
import java.io.IOException;
import java.util.Optional;

/** Implementation of the {@link Projects} resource collection. */
final class ProjectsImpl implements Projects {

  private final CodeArtifacts.Factory codeArtifactsFactory;
  private final Factory scansFactory;
  private final ProjectClient client;

  ProjectsImpl(
      final CodeArtifacts.Factory codeArtifactsFactory,
      final Factory scansFactory,
      final ProjectClient client) {
    this.codeArtifactsFactory = codeArtifactsFactory;
    this.scansFactory = scansFactory;
    this.client = client;
  }

  @Override
  public Definition define() {
    return new ProjectImpl.Definition(client, codeArtifactsFactory, scansFactory);
  }

  @Override
  public Optional<Project> findByName(final String name) throws IOException {
    return client
        .findByName(name)
        .map(inner -> new ProjectImpl(codeArtifactsFactory, scansFactory, inner));
  }
}
