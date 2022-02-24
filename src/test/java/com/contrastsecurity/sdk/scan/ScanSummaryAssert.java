package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
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
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

/** Custom assertions for {@link ScanSummary} */
final class ScanSummaryAssert extends AbstractAssert<ScanSummaryAssert, ScanSummary> {

  /**
   * @param summary object to make assertions on
   * @return new {@link ScanSummaryAssert}
   */
  static ScanSummaryAssert assertThat(final ScanSummary summary) {
    return new ScanSummaryAssert(summary);
  }

  private ScanSummaryAssert(final ScanSummary summary) {
    super(summary, ScanSummaryAssert.class);
  }

  /**
   * Verifies that this scan summary has the same values as its internal representation.
   *
   * @param inner internal representation of a scan summary
   * @return this
   */
  public ScanSummaryAssert hasSameValuesAsInner(final ScanSummaryInner inner) {
    Assertions.assertThat(actual.id()).isEqualTo(inner.id());
    Assertions.assertThat(actual.scanId()).isEqualTo(inner.scanId());
    Assertions.assertThat(actual.projectId()).isEqualTo(inner.projectId());
    Assertions.assertThat(actual.organizationId()).isEqualTo(inner.organizationId());
    Assertions.assertThat(actual.createdDate()).isEqualTo(inner.createdDate());
    Assertions.assertThat(actual.duration()).isEqualTo(Duration.ofMillis(inner.duration()));
    Assertions.assertThat(actual.totalNewResults()).isEqualTo(inner.totalNewResults());
    Assertions.assertThat(actual.totalFixedResults()).isEqualTo(inner.totalFixedResults());
    Assertions.assertThat(actual.totalResults()).isEqualTo(inner.totalResults());
    return this;
  }
}
