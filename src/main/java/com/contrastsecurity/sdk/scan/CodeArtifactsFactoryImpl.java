package com.contrastsecurity.sdk.scan;

final class CodeArtifactsFactoryImpl implements CodeArtifactsFactory {

  private final CodeArtifactClient client;

  CodeArtifactsFactoryImpl(final CodeArtifactClient client) {
    this.client = client;
  }

  @Override
  public CodeArtifacts create(final String projectId) {
    return new CodeArtifactsImpl(client, projectId);
  }
}
