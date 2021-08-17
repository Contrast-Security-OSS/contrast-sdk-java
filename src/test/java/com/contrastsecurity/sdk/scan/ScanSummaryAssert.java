package com.contrastsecurity.sdk.scan;

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
    Assertions.assertThat(actual.duration()).isEqualTo(inner.duration());
    Assertions.assertThat(actual.totalNewResults()).isEqualTo(inner.totalNewResults());
    Assertions.assertThat(actual.totalFixedResults()).isEqualTo(inner.totalFixedResults());
    Assertions.assertThat(actual.totalResults()).isEqualTo(inner.totalResults());
    return this;
  }
}
