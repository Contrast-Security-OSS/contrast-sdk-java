package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.MediaType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

final class ProjectImpl implements Project {

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
        return gson.fromJson(reader, ProjectImpl.class);
      }
    }
  }

  private final transient ContrastSDK contrast;
  private String id;
  private String organizationId;
  private String name;
  private boolean archived;
  private String language;
  private int critical;
  private int high;
  private int medium;
  private int low;
  private int note;
  private Instant lastScanTime;
  private int completedScans;
  private String lastScanId;
  private Collection<String> includeNamespaceFilters;
  private Collection<String> excludeNamespaceFilters;

  ProjectImpl(final ContrastSDK contrast) {
    this.contrast = contrast;
  }

  @Override
  public String id() {
    return id;
  }

  ProjectImpl setId(final String id) {
    this.id = id;
    return this;
  }

  @Override
  public String organizationId() {
    return organizationId;
  }

  ProjectImpl setOrganizationId(final String organizationId) {
    this.organizationId = organizationId;
    return this;
  }

  @Override
  public String name() {
    return name;
  }

  ProjectImpl setName(final String name) {
    this.name = name;
    return this;
  }

  @Override
  public boolean archived() {
    return archived;
  }

  ProjectImpl setArchived(final boolean archived) {
    this.archived = archived;
    return this;
  }

  @Override
  public String language() {
    return language;
  }

  ProjectImpl setLanguage(final String language) {
    this.language = language;
    return this;
  }

  @Override
  public int critical() {
    return critical;
  }

  public ProjectImpl setCritical(final int critical) {
    this.critical = critical;
    return this;
  }

  @Override
  public int high() {
    return high;
  }

  ProjectImpl setHigh(final int high) {
    this.high = high;
    return this;
  }

  @Override
  public int medium() {
    return medium;
  }

  ProjectImpl setMedium(final int medium) {
    this.medium = medium;
    return this;
  }

  @Override
  public int low() {
    return low;
  }

  ProjectImpl setLow(final int low) {
    this.low = low;
    return this;
  }

  @Override
  public int note() {
    return note;
  }

  public ProjectImpl setNote(final int note) {
    this.note = note;
    return this;
  }

  @Override
  public Instant lastScanTime() {
    return lastScanTime;
  }

  ProjectImpl setLastScanTime(final Instant lastScanTime) {
    this.lastScanTime = lastScanTime;
    return this;
  }

  @Override
  public int completedScans() {
    return completedScans;
  }

  ProjectImpl setCompletedScans(final int completedScans) {
    this.completedScans = completedScans;
    return this;
  }

  @Override
  public String lastScanId() {
    return lastScanId;
  }

  ProjectImpl setLastScanId(final String lastScanId) {
    this.lastScanId = lastScanId;
    return this;
  }

  @Override
  public Collection<String> includeNamespaceFilters() {
    return includeNamespaceFilters;
  }

  ProjectImpl setIncludeNamespaceFilters(final Collection<String> includeNamespaceFilters) {
    this.includeNamespaceFilters = includeNamespaceFilters;
    return this;
  }

  @Override
  public Collection<String> excludeNamespaceFilters() {
    return excludeNamespaceFilters;
  }

  ProjectImpl setExcludeNamespaceFilters(final Collection<String> excludeNamespaceFilters) {
    this.excludeNamespaceFilters = excludeNamespaceFilters;
    return this;
  }

  @Override
  public CodeArtifacts codeArtifacts() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public Scans scans() {
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
    final ProjectImpl project = (ProjectImpl) o;
    return archived == project.archived
        && critical == project.critical
        && high == project.high
        && medium == project.medium
        && low == project.low
        && note == project.note
        && completedScans == project.completedScans
        && id.equals(project.id)
        && organizationId.equals(project.organizationId)
        && name.equals(project.name)
        && language.equals(project.language)
        && Objects.equals(lastScanTime, project.lastScanTime)
        && Objects.equals(lastScanId, project.lastScanId)
        && Objects.equals(includeNamespaceFilters, project.includeNamespaceFilters)
        && Objects.equals(excludeNamespaceFilters, project.excludeNamespaceFilters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        organizationId,
        name,
        archived,
        language,
        critical,
        high,
        medium,
        low,
        note,
        lastScanTime,
        completedScans,
        lastScanId,
        includeNamespaceFilters,
        excludeNamespaceFilters);
  }
}
