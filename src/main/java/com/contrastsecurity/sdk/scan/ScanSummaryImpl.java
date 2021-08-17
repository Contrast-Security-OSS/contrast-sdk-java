package com.contrastsecurity.sdk.scan;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

final class ScanSummaryImpl implements ScanSummary {

  private final ScanClient client;
  private final ScanSummaryInner inner;

  public ScanSummaryImpl(final ScanClient client, final ScanSummaryInner inner) {
    this.client = Objects.requireNonNull(client);
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
  public ScanSummary refresh() throws IOException {
    final ScanSummaryInner inner = client.getSummary(projectId(), scanId());
    return inner.equals(this.inner) ? this : new ScanSummaryImpl(client, inner);
  }

  ScanSummaryInner toInner() {
    return inner;
  }
}
