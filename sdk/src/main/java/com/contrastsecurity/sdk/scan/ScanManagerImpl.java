package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2026 Contrast Security, Inc.
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

import com.contrastsecurity.sdk.ContrastSDK;
import com.google.gson.Gson;
import java.util.Objects;

/** Implementation of {@link ScanManager}. */
public final class ScanManagerImpl implements ScanManager {

  private final CodeArtifacts.Factory codeArtifactsFactory;
  private final Scans.Factory scansFactory;
  private final Projects projects;

  /**
   * Constructor.
   *
   * <p>For internal use only. Users should obtain a reference to the {@link ScanManager} via the
   * {@link ContrastSDK#scan} entrypoint.
   *
   * @param contrast for making outgoing requests
   * @param gson for deserializing JSON responses
   * @param organizationId the ID of the organization in which to manage Contrast Scan resources
   */
  public ScanManagerImpl(final ContrastSDK contrast, final Gson gson, final String organizationId) {
    Objects.requireNonNull(contrast);
    Objects.requireNonNull(gson);
    Objects.requireNonNull(organizationId);

    final CodeArtifactClient codeArtifactClient =
        new CodeArtifactClientImpl(contrast, gson, organizationId);
    this.codeArtifactsFactory = new CodeArtifactsImpl.Factory(codeArtifactClient);

    final ScanClient scanClient = new ScanClientImpl(contrast, gson, organizationId);
    this.scansFactory = new ScansImpl.Factory(scanClient);

    final ProjectClient projectClient = new ProjectClientImpl(contrast, gson, organizationId);
    this.projects =
        new ProjectsImpl.Factory(codeArtifactsFactory, scansFactory, projectClient).create();
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
