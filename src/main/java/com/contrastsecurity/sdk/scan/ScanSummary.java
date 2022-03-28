package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 Contrast Security, Inc.
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

import java.time.Duration;
import java.time.Instant;

/** Summary of a Scan and its results. */
public interface ScanSummary {

  /**
   * @return ID of this summary
   */
  String id();

  /**
   * @return ID of the scan
   */
  String scanId();

  /**
   * @return ID of the scan project
   */
  String projectId();

  /**
   * @return ID of the Contrast organization
   */
  String organizationId();

  /**
   * @return duration of the scan
   */
  Duration duration();

  /**
   * @return number of vulnerabilities detected in this scan
   */
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

  /**
   * @return time at which this scan summary was created
   */
  Instant createdDate();
}
