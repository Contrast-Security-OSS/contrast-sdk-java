package com.contrastsecurity.sdk.scan;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

/** Custom assertions for {@link Scan} */
final class ScanAssert extends AbstractAssert<ScanAssert, Scan> {

  /**
   * @param scan object to make assertions on
   * @return new {@link ScanAssert}
   */
  static ScanAssert assertThat(final Scan scan) {
    return new ScanAssert(scan);
  }

  private ScanAssert(final Scan scan) {
    super(scan, ScanAssert.class);
  }

  /**
   * Verifies that this scan has the same values as its internal representation.
   *
   * @param inner internal representation of a scan
   * @return this
   */
  public ScanAssert hasSameValuesAsInner(final ScanInner inner) {
    Assertions.assertThat(actual.id()).isEqualTo(inner.id());
    Assertions.assertThat(actual.projectId()).isEqualTo(inner.projectId());
    Assertions.assertThat(actual.organizationId()).isEqualTo(inner.organizationId());
    Assertions.assertThat(actual.status()).isEqualTo(inner.status());
    Assertions.assertThat(actual.errorMessage()).isEqualTo(inner.errorMessage());
    return this;
  }
}
