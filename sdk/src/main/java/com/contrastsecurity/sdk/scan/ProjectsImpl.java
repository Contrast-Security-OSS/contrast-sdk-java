package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2025 Contrast Security, Inc.
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
import java.util.Objects;
import java.util.Optional;

/** Implementation of {@link Projects} */
final class ProjectsImpl implements Projects {

  /** Implementation of {@link Projects.Factory} */
  static final class Factory implements Projects.Factory {

    private final CodeArtifacts.Factory codeArtifactsFactory;
    private final Scans.Factory scansFactory;
    private final ProjectClient client;

    Factory(
        final CodeArtifacts.Factory codeArtifactsFactory,
        final Scans.Factory scansFactory,
        final ProjectClient client) {
      this.codeArtifactsFactory = Objects.requireNonNull(codeArtifactsFactory);
      this.scansFactory = Objects.requireNonNull(scansFactory);
      this.client = Objects.requireNonNull(client);
    }

    @Override
    public Projects create() {
      return new ProjectsImpl(codeArtifactsFactory, scansFactory, client);
    }
  }

  private final CodeArtifacts.Factory codeArtifactsFactory;
  private final Scans.Factory scansFactory;
  private final ProjectClient client;

  ProjectsImpl(
      final CodeArtifacts.Factory codeArtifactsFactory,
      final Scans.Factory scansFactory,
      final ProjectClient client) {
    this.codeArtifactsFactory = Objects.requireNonNull(codeArtifactsFactory);
    this.scansFactory = Objects.requireNonNull(scansFactory);
    this.client = Objects.requireNonNull(client);
  }

  @Override
  public Project.Definition define() {
    return new ProjectImpl.Definition(client, codeArtifactsFactory, scansFactory);
  }

  @Override
  public Optional<Project> findByName(final String name) throws IOException {
    return client
        .findByName(name)
        .map(inner -> new ProjectImpl(codeArtifactsFactory, scansFactory, inner));
  }
}
