package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;

/** Describes the Contrast Scan project resource. */
public interface Project {

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

    Definition withIncludeNamespaceFilters(Collection<String> value);

    Definition withExcludeNamespaceFilters(Collection<String> value);

    /**
     * @return new project resource
     * @throws IOException when an IO error occurs making the request
     * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
     * @throws ResourceNotFoundException when the requested resource does not exist
     * @throws HttpResponseException when Contrast rejects this request with an error code
     */
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
   * @return ID of the last scan to complete successfully, or {@code null} if no such scan exists
   */
  String lastScanId();

  /**
   * @return the time at which the last successfully completed scan finished, or {@code null} if no
   *     such scan exists
   */
  Instant lastScanTime();

  /** @return count of completed scans in this project */
  int completedScans();

  /** @return collection of code namespaces to include in scans */
  Collection<String> includeNamespaceFilters();

  /** @return collection of code namespaces to exclude from scans */
  Collection<String> excludeNamespaceFilters();

  /** @return entry point to the code artifacts management API */
  CodeArtifacts codeArtifacts();

  /** @return entry point to the scans management API */
  Scans scans();
}
