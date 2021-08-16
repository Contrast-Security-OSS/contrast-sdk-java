package com.contrastsecurity.sdk.scan;

import java.io.IOException;
import java.nio.file.Path;

final class CodeArtifactsImpl implements CodeArtifacts {

  private final CodeArtifactClient client;
  private final String projectId;

  public CodeArtifactsImpl(final CodeArtifactClient client, final String projectId) {
    this.client = client;
    this.projectId = projectId;
  }

  @Override
  public CodeArtifact upload(final Path file, final String name) throws IOException {
    final CodeArtifactInner inner = client.upload(projectId, file);
    return new CodeArtifactImpl(inner);
  }

  @Override
  public CodeArtifact upload(final Path file) throws IOException {
    return upload(file, file.getFileName().toString());
  }
}
