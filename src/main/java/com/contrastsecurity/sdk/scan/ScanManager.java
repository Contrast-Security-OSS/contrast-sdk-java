package com.contrastsecurity.sdk.scan;

/** Manager for Contrast Scan resource collections. */
public interface ScanManager {

  /** @return {@link Projects} resource collection */
  Projects projects();

  /**
   * @param projectId project ID in which to manage code artifacts
   * @return {@link CodeArtifacts} resource collection
   */
  CodeArtifacts codeArtifacts(String projectId);

  /**
   * @param projectId project ID in which to manage code artifacts
   * @return {@link Scans} resource collection
   */
  Scans scans(String projectId);
}
