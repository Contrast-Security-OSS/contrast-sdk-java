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

import java.time.Instant;
import java.util.Objects;

/** Implementation of the {@link CodeArtifact} resource. */
final class CodeArtifactImpl implements CodeArtifact {

  private final CodeArtifactInner inner;

  CodeArtifactImpl(final CodeArtifactInner inner) {
    this.inner = Objects.requireNonNull(inner);
  }

  @Override
  public String id() {
    return inner.id();
  }

  @Override
  public String projectId() {
    return inner.projectId();
  }

  @Override
  public String organizationId() {
    return inner.organizationId();
  }

  @Override
  public String filename() {
    return inner.filename();
  }

  @Override
  public Instant createdTime() {
    return inner.createdTime();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final CodeArtifactImpl that = (CodeArtifactImpl) o;
    return inner.equals(that.inner);
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
