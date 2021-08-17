package com.contrastsecurity.sdk.scan;

import java.time.Instant;
import java.util.Objects;

/** Implementation of the {@link ScanSummary} resource. */
final class ScanSummaryImpl implements ScanSummary {

  private final ScanSummaryInner inner;

  public ScanSummaryImpl(final ScanSummaryInner inner) {
    this.inner = Objects.requireNonNull(inner);
  }

  @Override
  public String id() {
    return inner.id();
  }

  @Override
  public String scanId() {
    return inner.scanId();
  }

  @Override
  public String projectId() {
    return inner.projectId();
  }

  @Override
  public String organizationId() {
    return inner.organizationId();
  }

  @Override
  public long duration() {
    return inner.duration();
  }

  @Override
  public int totalResults() {
    return inner.totalResults();
  }

  @Override
  public int totalNewResults() {
    return inner.totalNewResults();
  }

  @Override
  public int totalFixedResults() {
    return inner.totalFixedResults();
  }

  @Override
  public Instant createdDate() {
    return inner.createdDate();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ScanSummaryImpl that = (ScanSummaryImpl) o;
    return inner.equals(that.inner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inner);
  }
}
