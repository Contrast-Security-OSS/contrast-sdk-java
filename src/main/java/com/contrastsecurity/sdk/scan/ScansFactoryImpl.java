package com.contrastsecurity.sdk.scan;

import java.util.Objects;

final class ScansFactoryImpl implements ScansFactory {

  private final ScanClient client;

  ScansFactoryImpl(final ScanClient client) {
    this.client = Objects.requireNonNull(client);
  }

  @Override
  public Scans create(final String projectId) {
    return new ScansImpl(client, projectId);
  }
}
