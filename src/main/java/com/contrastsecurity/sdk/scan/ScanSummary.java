package com.contrastsecurity.sdk.scan;

import java.time.Instant;

/** Summary of a Scan and its results. */
public interface ScanSummary {

  /** @return ID of this summary */
  String id();

  /** @return ID of the scan */
  String scanId();

  /** @return ID of the scan project */
  String projectId();

  /** @return ID of the Contrast organization */
  String organizationId();

  /** @return duration of the scan in milliseconds */
  long duration();

  /** @return number of vulnerabilities detected in this scan */
  int totalResults();

  /**
   * @return number of vulnerabilities detected in this scan that have not been previously detected
   *     in an earlier scan
   */
  int totalNewResults();

  /**
   * @return number of vulnerabilities that are no longer detected but were detected in previous
   *     scans
   */
  int totalFixedResults();

  /** @return time at which this scan summary was created */
  Instant createdDate();
}
