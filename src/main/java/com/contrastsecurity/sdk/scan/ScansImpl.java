package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.scan.Scan.Definition;
import java.io.IOException;
import java.util.Objects;

/** Implementation of the {@link Scans} resource collection. */
final class ScansImpl implements Scans {

  static final class Factory implements Scans.Factory {

    private final ScanClient client;

    Factory(final ScanClient client) {
      this.client = Objects.requireNonNull(client);
    }

    @Override
    public Scans create(final String projectId) {
      return new ScansImpl(client, projectId);
    }
  }

  private final ScanClient client;
  private final String projectId;

  ScansImpl(final ScanClient client, final String projectId) {
    this.client = Objects.requireNonNull(client);
    this.projectId = Objects.requireNonNull(projectId);
  }

  @Override
  public Definition define() {
    return new ScanImpl.Definition(client, projectId);
  }

  @Override
  public Scan get(final String id) throws IOException {
    final ScanInner inner = client.get(projectId, id);
    return new ScanImpl(client, inner);
  }

  @Override
  public ScanSummary summary(final String id) throws IOException {
    final ScanSummaryInner inner = client.getSummary(projectId, id);
    return new ScanSummaryImpl(inner);
  }
}
