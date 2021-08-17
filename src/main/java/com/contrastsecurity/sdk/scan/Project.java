package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.ServerResponseException;
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

    /**
     * @param filters collection of namespaces to include in scans
     * @return this
     */
    Definition withIncludeNamespaceFilters(Collection<String> filters);

    /**
     * @param filters collection of namespaces to exclude from scans
     * @return this
     */
    Definition withExcludeNamespaceFilters(Collection<String> filters);

    /**
     * @return new project resource
     * @throws IOException when an IO error occurs while making the request to the Contrast API
     * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
     * @throws ResourceNotFoundException when the requested resource does not exist
     * @throws HttpResponseException when Contrast rejects this request with an error code
     * @throws ServerResponseException when Contrast API returns a response that cannot be
     *     understood
     */
    Project create() throws IOException;
  }

  /** @return project ID */
  String id();

  /** @return organization ID */
  String organizationId();

  /** @return project name */
  String name();

  /** @return true when the project has been archived */
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

  /** @return entrypoint to the code artifact resource collection for this project */
  CodeArtifacts codeArtifacts();

  /** @return entrypoint to the scan resource collection for this project */
  Scans scans();
}
