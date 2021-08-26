package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
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

import static com.contrastsecurity.sdk.scan.ScanSummaryAssert.assertThat;

import com.contrastsecurity.EqualsAndHashcodeContract;
import com.contrastsecurity.TestDataConstants;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link ScanSummaryImpl} */
final class ScanSummaryImplTest implements EqualsAndHashcodeContract<ScanSummaryImpl> {

  @Test
  void delegates_to_inner() {
    final ScanSummaryInner inner = builder().build();
    final ScanSummaryImpl summary = new ScanSummaryImpl(inner);

    assertThat(summary).hasSameValuesAsInner(inner);
  }

  @Override
  public ScanSummaryImpl createValue() {
    final ScanSummaryInner inner = builder().build();
    return new ScanSummaryImpl(inner);
  }

  @Override
  public ScanSummaryImpl createNotEqualValue() {
    final ScanSummaryInner inner = builder().scanId("other-scan-id").build();
    return new ScanSummaryImpl(inner);
  }

  private static ScanSummaryInner.Builder builder() {
    return ScanSummaryInner.builder()
        .id("summary-id")
        .scanId("scan-id")
        .projectId("project-id")
        .organizationId("organization-id")
        .duration(100)
        .createdDate(TestDataConstants.TIMESTAMP_EXAMPLE)
        .lastModifiedDate(TestDataConstants.TIMESTAMP_EXAMPLE)
        .totalFixedResults(0)
        .totalNewResults(0)
        .totalResults(0);
  }
}
