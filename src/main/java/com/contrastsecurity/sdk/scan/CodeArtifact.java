package com.contrastsecurity.sdk.scan;

/**
 * Describes a code artifact uploaded to Contrast Scan
 *
 * <p>TODO[JG] JAVA-3298 move this to the Contrast Java SDK and flesh it out
 */
public interface CodeArtifact {

  /** @return unique ID of this code artifact */
  String id();

  /**
   * Starts a scan for this code artifact.
   *
   * @return new scan
   */
  Scan startScan();
}
