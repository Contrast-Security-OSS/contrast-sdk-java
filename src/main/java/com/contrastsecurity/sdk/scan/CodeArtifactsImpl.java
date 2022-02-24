package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/** Implementation of {@link CodeArtifacts} */
final class CodeArtifactsImpl implements CodeArtifacts {

  /** Implementation of {@link CodeArtifacts.Factory */
  static final class Factory implements CodeArtifacts.Factory {

    private final CodeArtifactClient client;

    Factory(final CodeArtifactClient client) {
      this.client = Objects.requireNonNull(client);
    }

    @Override
    public CodeArtifacts create(final String projectId) {
      return new CodeArtifactsImpl(client, projectId);
    }
  }

  private final CodeArtifactClient client;
  private final String projectId;

  CodeArtifactsImpl(final CodeArtifactClient client, final String projectId) {
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
