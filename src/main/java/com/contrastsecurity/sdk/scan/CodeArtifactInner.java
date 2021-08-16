package com.contrastsecurity.sdk.scan;

import com.google.auto.value.AutoValue;
import java.time.Instant;

/** Value type that describes the code artifact structure returned by the Contrast API. */
@AutoValue
abstract class CodeArtifactInner {

  /** @return new {@link Builder} */
  static Builder builder() {
    return new AutoValue_CodeArtifactInner.Builder();
  }

  /** @return ID of this code artifact */
  abstract String id();

  /** @return ID of the project to which this code artifact belongs */
  abstract String projectId();

  /** @return ID of the organization to which this code artifact belongs */
  abstract String organizationId();

  /** @return filename */
  abstract String filename();

  /** @return time at which the code artifact was uploaded to Contrast Scan */
  abstract Instant createdTime();

  /** Builder for {@link CodeArtifactInner}. */
  @AutoValue.Builder
  abstract static class Builder {

    /** @see CodeArtifactInner#id() */
    abstract Builder id(String value);

    /** @see CodeArtifactInner#projectId() */
    abstract Builder projectId(String value);

    /** @see CodeArtifactInner#organizationId() */
    abstract Builder organizationId(String value);

    /** @see CodeArtifactInner#filename() */
    abstract Builder filename(String value);

    /** @see CodeArtifactInner#createdTime() */
    abstract Builder createdTime(Instant value);

    /** @return new {@link CodeArtifactInner} */
    abstract CodeArtifactInner build();
  }
}
