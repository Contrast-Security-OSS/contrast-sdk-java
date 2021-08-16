package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.scan.Project.Definition;
import java.io.IOException;
import java.util.Optional;

/** Implementation of the {@link Projects} resource collection. */
final class ProjectsImpl implements Projects {

  private final CodeArtifactsFactory codeArtifactsFactory;
  private final ScansFactory scansFactory;
  private final ProjectClient client;

  ProjectsImpl(
      final CodeArtifactsFactory codeArtifactsFactory,
      final ScansFactory scansFactory,
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
