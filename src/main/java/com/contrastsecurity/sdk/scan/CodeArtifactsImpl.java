package com.contrastsecurity.sdk.scan;

import java.io.IOException;
import java.nio.file.Path;

final class CodeArtifactsImpl implements CodeArtifacts {

  static final class Factory implements CodeArtifacts.Factory {

    private final CodeArtifactClient client;

    Factory(final CodeArtifactClient client) {
      this.client = client;
    }

    @Override
    public CodeArtifacts create(final String projectId) {
      return new CodeArtifactsImpl(client, projectId);
    }
  }

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
