package com.contrastsecurity.scan;

import java.time.Instant;

/** Describes a scan results summary. */
public interface ScanSummary {

  /** @return unique ID of this summary */
  String id();

  /** @return unique ID of the scan */
  String scanId();

  /** @return unique ID of the scan project */
  String projectId();

  /** @return unique ID of the Contrast organization */
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
