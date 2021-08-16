package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.scan.Scan.Definition;
import java.io.IOException;
import java.util.Objects;

/** Implementation of the {@link Scans} resource collection. */
final class ScansImpl implements Scans {

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
}
