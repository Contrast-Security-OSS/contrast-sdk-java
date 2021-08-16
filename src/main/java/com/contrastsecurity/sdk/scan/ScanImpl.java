package com.contrastsecurity.sdk.scan;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;

/** Implementation of the {@link Scan} resource. */
final class ScanImpl implements Scan {

  /** Implementation of the {@link Scan.Definition} definition */
  static final class Definition implements Scan.Definition {

    private final transient ScanClient client;
    private final transient String projectId;
    private String codeArtifactId;
    private String label;

    Definition(final ScanClient client, final String projectId) {
      this.client = client;
      this.projectId = Objects.requireNonNull(projectId);
    }

    @Override
    public Scan.Definition withExistingCodeArtifact(final String id) {
      this.codeArtifactId = Objects.requireNonNull(id);
      return this;
    }

    @Override
    public Scan.Definition withExistingCodeArtifact(final CodeArtifact codeArtifact) {
      return withExistingCodeArtifact(codeArtifact.id());
    }

    @Override
    public Scan.Definition withLabel(final String label) {
      this.label = Objects.requireNonNull(label);
      return this;
    }

    @Override
    public Scan create() throws IOException {
      final ScanCreate create = ScanCreate.of(codeArtifactId, label);
      final ScanInner inner = client.create(projectId, create);
      return new ScanImpl(client, inner);
    }
  }

  private final ScanClient client;
  private final ScanInner inner;

  ScanImpl(final ScanClient client, final ScanInner inner) {
    this.client = Objects.requireNonNull(client);
    this.inner = Objects.requireNonNull(inner);
  }

  @Override
  public String id() {
    return inner.id();
  }

  @Override
  public ScanStatus status() {
    return inner.status();
  }

  @Override
  public String errorMessage() {
    return inner.errorMessage();
  }

  @Override
  public boolean isFinished() {
    return inner.status() == ScanStatus.FAILED
        || inner.status() == ScanStatus.COMPLETED
        || inner.status() == ScanStatus.CANCELLED;
  }

  @Override
  public CompletionStage<Scan> await(final ScheduledExecutorService scheduler) {
    // TODO
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public InputStream sarif() throws IOException {
    return client.getSarif(inner.projectId(), inner.id());
  }

  @Override
  public void saveSarif(final Path file) throws IOException {
    try (InputStream is = sarif()) {
      Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  @Override
  public ScanSummary summary() {
    // TODO
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public Scan refresh() throws IOException {
    final ScanInner inner = client.get(this.inner.projectId(), this.inner.id());
    return inner.equals(this.inner) ? this : new ScanImpl(client, inner);
  }

  /** visible for testing */
  ScanInner toInner() {
    return inner;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ScanImpl scan = (ScanImpl) o;
    return inner.equals(scan.inner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inner);
  }

  @Override
  public String toString() {
    return inner.toString();
  }
}
