package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.internal.Nullable;
import com.google.auto.value.AutoValue;

/** Value type that describes the scan structure returned by the Contrast API. */
@AutoValue
abstract class ScanInner {

  /** @return new {@link Builder} */
  static Builder builder() {
    return new AutoValue_ScanInner.Builder();
  }

  /** @return ID of this scan */
  abstract String id();

  /** @return ID of the project to which this scan belongs */
  abstract String projectId();

  /** @return ID of the organization to which this scan belongs */
  abstract String organizationId();

  /** @return scan status */
  abstract ScanStatus status();

  /** @return error message for failed scan, or {@code null} if the scan has not failed */
  @Nullable
  abstract String errorMessage();

  /** Builder for {@link ScanInner}. */
  @AutoValue.Builder
  abstract static class Builder {

    /** @see ScanInner#id() */
    abstract Builder id(String value);

    /** @see ScanInner#projectId() */
    abstract Builder projectId(String value);

    /** @see ScanInner#organizationId() */
    abstract Builder organizationId(String value);

    /** @see ScanInner#status() */
    abstract Builder status(ScanStatus value);

    /** @see ScanInner#errorMessage() */
    abstract Builder errorMessage(String value);

    /** @return new {@link ScanInner} */
    abstract ScanInner build();
  }
}
