package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2024 Contrast Security, Inc.
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

import com.contrastsecurity.sdk.internal.Nullable;
import com.contrastsecurity.sdk.scan.Scans.Factory;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

/** Implementation of the {@link Project} resource. */
final class ProjectImpl implements Project {

  /** Implementation of the {@link Project.Definition} definition */
  static final class Definition implements Project.Definition {

    private final ProjectClient client;
    private final CodeArtifacts.Factory codeArtifactsFactory;
    private final Factory scansFactory;
    private final ProjectCreate.Builder builder = ProjectCreate.builder();

    Definition(
        final ProjectClient client,
        final CodeArtifacts.Factory codeArtifactsFactory,
        final Factory scansFactory) {
      this.client = Objects.requireNonNull(client);
      this.codeArtifactsFactory = Objects.requireNonNull(codeArtifactsFactory);
      this.scansFactory = Objects.requireNonNull(scansFactory);
    }

    @Override
    public Project.Definition withName(final String name) {
      builder.name(name);
      return this;
    }

    @Override
    public Project.Definition withLanguage(final String language) {
      builder.language(language);
      return this;
    }

    @Override
    public Project.Definition withIncludeNamespaceFilters(final Collection<String> filters) {
      builder.includeNamespaceFilters(filters);
      return this;
    }

    @Override
    public Project.Definition withExcludeNamespaceFilters(final Collection<String> filters) {
      builder.excludeNamespaceFilters(filters);
      return this;
    }

    @Override
    public Project create() throws IOException {
      final ProjectCreate create = builder.build();
      final ProjectInner inner = client.create(create);
      return new ProjectImpl(codeArtifactsFactory, scansFactory, inner);
    }
  }

  private final CodeArtifacts.Factory codeArtifactsFactory;
  private final Factory scansFactory;
  private final ProjectInner inner;

  ProjectImpl(
      final CodeArtifacts.Factory codeArtifactsFactory,
      final Factory scansFactory,
      final ProjectInner inner) {
    this.codeArtifactsFactory = codeArtifactsFactory;
    this.scansFactory = scansFactory;
    this.inner = Objects.requireNonNull(inner);
  }

  @Override
  public String id() {
    return inner.id();
  }

  @Override
  public String organizationId() {
    return inner.organizationId();
  }

  @Override
  public String name() {
    return inner.name();
  }

  @Override
  public boolean archived() {
    return inner.archived();
  }

  @Override
  public String language() {
    return inner.language();
  }

  @Override
  public int critical() {
    return inner.critical();
  }

  @Override
  public int high() {
    return inner.high();
  }

  @Override
  public int medium() {
    return inner.medium();
  }

  @Override
  public int low() {
    return inner.low();
  }

  @Override
  public int note() {
    return inner.note();
  }

  @Override
  @Nullable
  public Instant lastScanTime() {
    return inner.lastScanTime();
  }

  @Override
  public int completedScans() {
    return inner.completedScans();
  }

  @Override
  @Nullable
  public String lastScanId() {
    return inner.lastScanId();
  }

  @Override
  @Nullable
  public Collection<String> includeNamespaceFilters() {
    return inner.includeNamespaceFilters();
  }

  @Override
  @Nullable
  public Collection<String> excludeNamespaceFilters() {
    return inner.excludeNamespaceFilters();
  }

  @Override
  public CodeArtifacts codeArtifacts() {
    return codeArtifactsFactory.create(id());
  }

  @Override
  public Scans scans() {
    return scansFactory.create(id());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ProjectImpl project = (ProjectImpl) o;
    return inner.equals(project.inner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inner);
  }

  @Override
  public String toString() {
    return inner.toString();
  }
}
