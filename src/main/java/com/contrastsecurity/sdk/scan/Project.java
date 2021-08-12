package com.contrastsecurity.sdk.scan;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;

/** Describes the Contrast Scan project top-level resource. */
public interface Project extends Refreshable<Project> {

  /** Builder for defining a new project resource. */
  interface Definition {

    /**
     * @param name project name
     * @return this
     */
    Definition withName(String name);

    /**
     * @param language project programming language
     * @return this
     */
    Definition withLanguage(String language);

    /** @return new project resource */
    Project create() throws IOException;
  }

  /** @return unique project ID */
  String id();

  /** @return unique organization ID */
  String organizationId();

  /** @return project name */
  String name();

  /** @return if true, the project has been archived */
  boolean archived();

  /** @return programming language used by this project */
  String language();

  /**
   * @return count of critical severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  int critical();

  /**
   * @return count of high severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  int high();

  /**
   * @return count of medium severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  int medium();

  /**
   * @return count of low severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  int low();

  /**
   * @return count of note severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  int note();

  /**
   * @return the time at which the last successfully completed scan finished, or {@code null} if no
   *     such scan exists
   */
  Instant lastScanTime();

  /** @return count of completed scans in this project */
  int completedScans();

  /**
   * @return ID of the last scan to complete successfully, or {@code null} if no such scan exists
   */
  String lastScanId();

  /** @return collection of code namespaces to include in the scan */
  Collection<String> includeNamespaceFilters();

  /** @return collection of code namespaces to exclude from the scan */
  Collection<String> excludeNamespaceFilters();

  /**
   * Uploads the given file as a new code artifact for scanning.
   *
   * @param file the file to upload
   * @return new {@link CodeArtifact}
   */
  CodeArtifact uploadCodeArtifact(Path file);
}
