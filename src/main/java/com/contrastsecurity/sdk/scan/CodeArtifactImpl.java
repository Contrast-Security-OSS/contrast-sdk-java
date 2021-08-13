package com.contrastsecurity.sdk.scan;

import com.google.auto.value.AutoValue;
import java.time.Instant;

/** Value type that describes a code artifact that has been uploaded to Contrast Scan. */
@AutoValue
abstract class CodeArtifactImpl implements CodeArtifact {

  /** @return new {@code Builder} */
  static CodeArtifactImpl.Builder builder() {
    return new AutoValue_CodeArtifactImpl.Builder();
  }

  /** Builder for {@code CodeArtifactImpl}. */
  @AutoValue.Builder
  abstract static class Builder {

    abstract Builder id(final String value);

    abstract Builder projectId(final String value);

    abstract Builder organizationId(final String value);

    abstract Builder filename(final String value);

    abstract Builder createdTime(final Instant value);

    abstract CodeArtifactImpl build();
  }
}
