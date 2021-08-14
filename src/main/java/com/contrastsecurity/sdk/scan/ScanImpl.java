package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.MediaType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.Nullable;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.google.auto.value.AutoValue;
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

  /** Implementation of the {@link Scan.Definition} definition */
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
        final ScanImpl.Value value = gson.fromJson(reader, AutoValue_ScanImpl_Value.class);
        return new ScanImpl(contrast, value);
      }
    }
  }

  /** Value type that describes the scan structure returned by the API. */
  @AutoValue
  abstract static class Value {

    static Builder builder() {
      return new AutoValue_ScanImpl_Value.Builder();
    }

    abstract String id();

    abstract String projectId();

    abstract String organizationId();

    abstract Status status();

    @Nullable
    abstract String errorMessage();

    @AutoValue.Builder
    abstract static class Builder {
      abstract Builder id(String value);

      abstract Builder projectId(String value);

      abstract Builder organizationId(String value);

      abstract Builder status(Status value);

      abstract Builder errorMessage(String value);

      abstract Value build();
    }
  }

  private final ContrastSDK contrast;
  private final Value value;

  ScanImpl(final ContrastSDK contrast, final Value value) {
    this.contrast = Objects.requireNonNull(contrast);
    this.value = value;
  }

  @Override
  public String id() {
    return value.id();
  }

  @Override
  public Status status() {
    return value.status();
  }

  @Override
  public String errorMessage() {
    return value.errorMessage();
  }

  @Override
  public boolean isFinished() {
    return value.status() == Status.FAILED
        || value.status() == Status.COMPLETED
        || value.status() == Status.CANCELLED;
  }

  @Override
  public CompletionStage<Scan> await(final ScheduledExecutorService scheduler) {
    // TODO
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public InputStream sarif() {
    // TODO
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void saveSarif(final Path file) {
    // TODO
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public ScanSummary summary() {
    // TODO
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public Scan refresh() {
    // TODO
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
    return value.equals(scan.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
