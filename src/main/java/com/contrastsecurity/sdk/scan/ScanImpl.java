package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.MediaType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;

/** Implementation of the {@link Scan} resource. */
final class ScanImpl implements Scan {

  static final class Definition implements Scan.Definition {

    private final transient ContrastSDK contrast;
    private final transient Gson gson;
    private final transient String organizationId;
    private final transient String projectId;
    private String codeArtifactId;
    private String label;

    Definition(
        final ContrastSDK contrast,
        final Gson gson,
        final String organizationId,
        final String projectId) {
      this.contrast = Objects.requireNonNull(contrast);
      this.gson = gson;
      this.organizationId = Objects.requireNonNull(organizationId);
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
      final String uri =
          new URIBuilder()
              .appendPathSegments(
                  "sast", "organizations", organizationId, "projects", projectId, "scans")
              .toURIString();
      // TODO add a makeRequestWithBody method that gives callers access to the OutputStream
      // directly
      final String json = gson.toJson(this);
      try (Reader reader =
          new InputStreamReader(
              contrast.makeRequestWithBody(HttpMethod.POST, uri, json, MediaType.JSON))) {
        return gson.fromJson(reader, ScanImpl.class);
      }
    }
  }

  private final transient ContrastSDK contrast;
  private String id;
  private Status status;
  private String errorMessage;

  ScanImpl(final ContrastSDK contrast) {
    this.contrast = Objects.requireNonNull(contrast);
  }

  /** visible for testing */
  ScanImpl(
      final ContrastSDK contrast, final String id, final Status status, final String errorMessage) {
    this.contrast = contrast;
    this.id = Objects.requireNonNull(id);
    this.status = Objects.requireNonNull(status);
    this.errorMessage = errorMessage;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public Status status() {
    return status;
  }

  @Override
  public String errorMessage() {
    return errorMessage;
  }

  @Override
  public boolean isFinished() {
    return status == Status.FAILED || status == Status.COMPLETED || status == Status.CANCELLED;
  }

  @Override
  public CompletionStage<Scan> await(final ScheduledExecutorService scheduler) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public InputStream sarif() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void saveSarif(final Path file) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public ScanSummary summary() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public Scan refresh() {
    throw new UnsupportedOperationException("Not yet implemented");
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
    return id.equals(scan.id)
        && status == scan.status
        && Objects.equals(errorMessage, scan.errorMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, status, errorMessage);
  }

  @Override
  public String toString() {
    return "ScanImpl{"
        + "id='"
        + id
        + '\''
        + ", status="
        + status
        + ", errorMessage='"
        + errorMessage
        + '\''
        + '}';
  }
}
