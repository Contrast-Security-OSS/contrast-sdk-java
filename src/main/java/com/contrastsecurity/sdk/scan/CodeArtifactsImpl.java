package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

final class CodeArtifactsImpl implements CodeArtifacts {

  private final CodeArtifactClient client;
  private final String projectId;

  CodeArtifactsImpl(
      final ContrastSDK contrast, final String organizationId, final String projectId) {
    final Gson gson = GsonFactory.builder().create();
    this.client = new CodeArtifactClientImpl(contrast, gson, organizationId);
    this.projectId = Objects.requireNonNull(projectId);
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
