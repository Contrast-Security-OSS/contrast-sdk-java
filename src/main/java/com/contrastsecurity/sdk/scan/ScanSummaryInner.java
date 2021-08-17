package com.contrastsecurity.sdk.scan;

import com.google.auto.value.AutoValue;
import java.time.Instant;

/** Describes a scan results summary. */
@AutoValue
abstract class ScanSummaryInner {

  /** @return new {@link Builder} */
  static Builder builder() {
    return new AutoValue_ScanSummaryInner.Builder();
  }

  /** @return ID of this summary */
  abstract String id();

  /** @return ID of the scan */
  abstract String scanId();

  /** @return ID of the scan project */
  abstract String projectId();

  /** @return ID of the Contrast organization */
  abstract String organizationId();

  /** @return duration of the scan in milliseconds */
  abstract long duration();

  /** @return number of vulnerabilities detected in this scan */
  abstract int totalResults();

  /**
   * @return number of vulnerabilities detected in this scan that have not been previously detected
   *     in an earlier scan
   */
  abstract int totalNewResults();

  /**
   * @return number of vulnerabilities that are no longer detected but were detected in previous
   *     scans
   */
  abstract int totalFixedResults();

  /** @return time at which this scan summary was created */
  abstract Instant createdDate();

  /** @return time at which this scan summary was last updated */
  abstract Instant lastModifiedDate();

  /** Builder for {@link ScanSummaryInner}. */
  @AutoValue.Builder
  abstract static class Builder {

    /** @see ScanSummaryInner#id() */
    abstract Builder id(String value);

    /** @see ScanSummaryInner#scanId() */
    abstract Builder scanId(String value);

    /** @see ScanSummaryInner#projectId() */
    abstract Builder projectId(String value);

    /** @see ScanSummaryInner#organizationId() */
    abstract Builder organizationId(String value);

    /** @see ScanSummaryInner#duration() */
    abstract Builder duration(long value);

    /** @see ScanSummaryInner#totalResults() */
    abstract Builder totalResults(int value);

    /** @see ScanSummaryInner#totalNewResults() () */
    abstract Builder totalNewResults(int value);

    /** @see ScanSummaryInner#totalFixedResults() () */
    abstract Builder totalFixedResults(int value);

    /** @see ScanSummaryInner#createdDate() */
    abstract Builder createdDate(Instant value);

    /** @see ScanSummaryInner#lastModifiedDate() */
    abstract Builder lastModifiedDate(Instant value);

    /** @return new {@link ScanSummaryInner} */
    abstract ScanSummaryInner build();
  }
}
