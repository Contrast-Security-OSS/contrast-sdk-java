package com.contrastsecurity.maven.plugin;

/*-
 * #%L
 * Contrast Maven Plugin
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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

import com.contrastsecurity.sdk.scan.ScanSummary;
import com.google.auto.value.AutoValue;
import java.time.Duration;
import java.time.Instant;

/** Fake implementation of {@link ScanSummary} for testing. */
@AutoValue
abstract class FakeScanSummary implements ScanSummary {

  /** new {@link Builder} */
  static Builder builder() {
    return new AutoValue_FakeScanSummary.Builder();
  }

  /** Builder for {@link ScanSummary}. */
  @AutoValue.Builder
  abstract static class Builder {

    /**
     * @see ScanSummary#id()
     */
    abstract Builder id(String value);

    /**
     * @see ScanSummary#scanId()
     */
    abstract Builder scanId(String value);

    /**
     * @see ScanSummary#projectId()
     */
    abstract Builder projectId(String value);

    /**
     * @see ScanSummary#organizationId()
     */
    abstract Builder organizationId(String value);

    /**
     * @see ScanSummary#duration()
     */
    abstract Builder duration(Duration value);

    /**
     * @see ScanSummary#totalResults()
     */
    abstract Builder totalResults(int value);

    /**
     * @see ScanSummary#totalNewResults() ()
     */
    abstract Builder totalNewResults(int value);

    /**
     * @see ScanSummary#totalFixedResults() ()
     */
    abstract Builder totalFixedResults(int value);

    /**
     * @see ScanSummary#createdDate()
     */
    abstract Builder createdDate(Instant value);

    /**
     * @return new {@link ScanSummary}
     */
    abstract FakeScanSummary build();
  }
}
