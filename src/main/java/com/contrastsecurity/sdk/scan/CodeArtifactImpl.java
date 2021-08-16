package com.contrastsecurity.sdk.scan;

import java.time.Instant;
import java.util.Objects;

/** Value type that describes a code artifact that has been uploaded to Contrast Scan. */
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

  /** visible for testing */
  CodeArtifactInner toInner() {
    return inner;
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
