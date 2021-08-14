package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.MediaType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.Nullable;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

/** Implementation of the {@link Project} resource. */
final class ProjectImpl implements Project {

  /** Implementation of the {@link Project.Definition} definition */
  static final class Definition implements Project.Definition {

    private final transient ContrastSDK contrast;
    private final transient Gson gson;
    private final transient String organizationId;
    private String name;
    private String language;

    Definition(final ContrastSDK contrast, final Gson gson, final String organizationId) {
      this.contrast = contrast;
      this.gson = gson;
      this.organizationId = organizationId;
    }

    @Override
    public Project.Definition withName(final String name) {
      this.name = name;
      return this;
    }

    @Override
    public Project.Definition withLanguage(final String language) {
      this.language = language;
      return this;
    }

    @Override
    public Project create() throws IOException {
      // requests made with ContrastSDK.makeRequest must have their path prepended with "/"
      final String path =
          new URIBuilder()
              .appendPathSegments("sast", "organizations", organizationId, "projects")
              .toURIString();
      final String json = gson.toJson(this);
      try (Reader reader =
          new InputStreamReader(
              contrast.makeRequestWithBody(HttpMethod.POST, path, json, MediaType.JSON))) {
        final ProjectImpl.Value value = gson.fromJson(reader, AutoValue_ProjectImpl_Value.class);
        return new ProjectImpl(contrast, gson, value);
      }
    }
  }

  /** Value type that represents the project as returned by the Contrast API. */
  @AutoValue
  abstract static class Value {

    static Builder builder() {
      return new AutoValue_ProjectImpl_Value.Builder();
    }

    abstract String id();

    abstract String organizationId();

    abstract String name();

    abstract boolean archived();

    abstract String language();

    abstract int critical();

    abstract int high();

    abstract int medium();

    abstract int low();

    abstract int note();

    @Nullable
    abstract Instant lastScanTime();

    abstract int completedScans();

    @Nullable
    abstract String lastScanId();

    @Nullable
    abstract Collection<String> includeNamespaceFilters();

    @Nullable
    abstract Collection<String> excludeNamespaceFilters();

    @AutoValue.Builder
    abstract static class Builder {
      abstract Builder id(String value);

      abstract Builder organizationId(String value);

      abstract Builder name(String value);

      abstract Builder archived(boolean value);

      abstract Builder language(String value);

      abstract Builder critical(int value);

      abstract Builder high(int value);

      abstract Builder medium(int value);

      abstract Builder low(int value);

      abstract Builder note(int value);

      abstract Builder lastScanTime(Instant value);

      abstract Builder completedScans(int value);

      abstract Builder lastScanId(String value);

      abstract Builder includeNamespaceFilters(Collection<String> value);

      abstract Builder excludeNamespaceFilters(Collection<String> value);

      abstract Value build();
    }
  }

  private final ContrastSDK contrast;
  private final Gson gson;
  private final ProjectImpl.Value value;

  /**
   * Constructor.
   *
   * @param contrast for making outgoing requests
   * @param gson for deserializing JSON responses
   * @param value value object to which to delegate accessors
   */
  ProjectImpl(final ContrastSDK contrast, final Gson gson, final Value value) {
    this.contrast = contrast;
    this.gson = gson;
    this.value = value;
  }

  @Override
  public String id() {
    return value.id();
  }

  @Override
  public String organizationId() {
    return value.organizationId();
  }

  @Override
  public String name() {
    return value.name();
  }

  @Override
  public boolean archived() {
    return value.archived();
  }

  @Override
  public String language() {
    return value.language();
  }

  @Override
  public int critical() {
    return value.critical();
  }

  @Override
  public int high() {
    return value.high();
  }

  @Override
  public int medium() {
    return value.medium();
  }

  @Override
  public int low() {
    return value.low();
  }

  @Override
  public int note() {
    return value.note();
  }

  @Override
  @Nullable
  public Instant lastScanTime() {
    return value.lastScanTime();
  }

  @Override
  public int completedScans() {
    return value.completedScans();
  }

  @Override
  @Nullable
  public String lastScanId() {
    return value.lastScanId();
  }

  @Override
  @Nullable
  public Collection<String> includeNamespaceFilters() {
    return value.includeNamespaceFilters();
  }

  @Override
  @Nullable
  public Collection<String> excludeNamespaceFilters() {
    return value.excludeNamespaceFilters();
  }

  @Override
  public CodeArtifacts codeArtifacts() {
    return new CodeArtifactsImpl(contrast, value.organizationId(), value.id());
  }

  @Override
  public Scans scans() {
    return new ScansImpl(contrast, gson, value.organizationId(), value.id());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ProjectImpl project = (ProjectImpl) o;
    return value.equals(project.value);
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
