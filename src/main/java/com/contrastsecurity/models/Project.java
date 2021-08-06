package com.contrastsecurity.models;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/** Value object that describes a Contrast Scan project */
public final class Project {

  /** @return new {@link Builder} */
  public static Builder builder() {
    return new Builder();
  }

  private final String id;
  private final String organizationId;
  private final String name;
  private final boolean archived;
  private final String language;
  private final int critical;
  private final int high;
  private final int medium;
  private final int low;
  private final int note;
  private final OffsetDateTime lastScanTime;
  private final int completedScans;
  private final String lastScanId;
  private final Collection<String> includeNamespaceFilters;
  private final Collection<String> excludeNamespaceFilters;

  /** Visible for GSON */
  Project(
      final String id,
      final String organizationId,
      final String name,
      final boolean archived,
      final String language,
      final int critical,
      final int high,
      final int medium,
      final int low,
      final int note,
      final OffsetDateTime lastScanTime,
      final int completedScans,
      final String lastScanId,
      final Collection<String> includeNamespaceFilters,
      final Collection<String> excludeNamespaceFilters) {
    this.id = Objects.requireNonNull(id);
    this.organizationId = Objects.requireNonNull(organizationId);
    this.name = Objects.requireNonNull(name);
    this.archived = archived;
    this.language = Objects.requireNonNull(language);
    this.critical = critical;
    this.high = high;
    this.medium = medium;
    this.low = low;
    this.note = note;
    this.lastScanTime = lastScanTime;
    this.completedScans = completedScans;
    this.lastScanId = lastScanId;
    this.includeNamespaceFilters =
        Collections.unmodifiableList(new ArrayList<>(includeNamespaceFilters));
    this.excludeNamespaceFilters =
        Collections.unmodifiableList(new ArrayList<>(excludeNamespaceFilters));
  }

  /** @return unique project ID */
  public String getId() {
    return id;
  }

  /** @return unique organization ID */
  public String getOrganizationId() {
    return organizationId;
  }

  /** @return project name */
  public String getName() {
    return name;
  }

  /** @return if true, the project has been archived */
  public boolean isArchived() {
    return archived;
  }

  /** @return programming language used by this project */
  public String getLanguage() {
    return language;
  }

  /**
   * @return count of critical severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  public int getCritical() {
    return critical;
  }

  /**
   * @return count of high severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  public int getHigh() {
    return high;
  }

  /**
   * @return count of medium severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  public int getMedium() {
    return medium;
  }

  /**
   * @return count of low severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  public int getLow() {
    return low;
  }

  /**
   * @return count of note severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  public int getNote() {
    return note;
  }

  /**
   * @return the time at which the last successfully completed scan finished, or {@code null} if no
   *     such scan exists
   */
  public OffsetDateTime getLastScanTime() {
    return lastScanTime;
  }

  /** @return count of completed scans in this project */
  public int getCompletedScans() {
    return completedScans;
  }

  /**
   * @return ID of the last scan to complete successfully, or {@code null} if no such scan exists
   */
  public String getLastScanId() {
    return lastScanId;
  }

  /** @return collection of code namespaces to include in the scan */
  public Collection<String> getIncludeNamespaceFilters() {
    return includeNamespaceFilters;
  }

  /** @return collection of code namespaces to exclude from the scan */
  public Collection<String> getExcludeNamespaceFilters() {
    return excludeNamespaceFilters;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Project project = (Project) o;
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
        && includeNamespaceFilters.equals(project.includeNamespaceFilters)
        && excludeNamespaceFilters.equals(project.excludeNamespaceFilters);
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

  @Override
  public String toString() {
    return "Project{"
        + "id='"
        + id
        + '\''
        + ", organizationId='"
        + organizationId
        + '\''
        + ", name='"
        + name
        + '\''
        + ", archived="
        + archived
        + ", language='"
        + language
        + '\''
        + ", critical="
        + critical
        + ", high="
        + high
        + ", medium="
        + medium
        + ", low="
        + low
        + ", note="
        + note
        + ", lastScanTime="
        + lastScanTime
        + ", completedScans="
        + completedScans
        + ", lastScanId='"
        + lastScanId
        + '\''
        + ", includeNamespaceFilters="
        + includeNamespaceFilters
        + ", excludeNamespaceFilters="
        + excludeNamespaceFilters
        + '}';
  }

  /** Builder for {@link Project} */
  public static final class Builder {

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
    private OffsetDateTime lastScanTime;
    private int completedScans;
    private String lastScanId;
    private Collection<String> includeNamespaceFilters;
    private Collection<String> excludeNamespaceFilters;

    public Builder id(final String id) {
      this.id = id;
      return this;
    }

    public Builder organizationId(final String organizationId) {
      this.organizationId = organizationId;
      return this;
    }

    public Builder name(final String name) {
      this.name = name;
      return this;
    }

    public Builder archived(final boolean archived) {
      this.archived = archived;
      return this;
    }

    public Builder language(final String language) {
      this.language = language;
      return this;
    }

    public Builder critical(final int critical) {
      this.critical = critical;
      return this;
    }

    public Builder high(final int high) {
      this.high = high;
      return this;
    }

    public Builder medium(final int medium) {
      this.medium = medium;
      return this;
    }

    public Builder low(final int low) {
      this.low = low;
      return this;
    }

    public Builder note(final int note) {
      this.note = note;
      return this;
    }

    public Builder lastScanTime(final OffsetDateTime lastScanTime) {
      this.lastScanTime = lastScanTime;
      return this;
    }

    public Builder completedScans(final int completedScans) {
      this.completedScans = completedScans;
      return this;
    }

    public Builder lastScanId(final String lastScanId) {
      this.lastScanId = lastScanId;
      return this;
    }

    public Builder includeNamespaceFilters(final Collection<String> includeNamespaceFilters) {
      this.includeNamespaceFilters = includeNamespaceFilters;
      return this;
    }

    public Builder excludeNamespaceFilters(final Collection<String> excludeNamespaceFilters) {
      this.excludeNamespaceFilters = excludeNamespaceFilters;
      return this;
    }

    /** @return new {@code Project} */
    public Project build() {
      return new Project(
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
}
