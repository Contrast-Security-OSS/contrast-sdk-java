package com.contrastsecurity.sdk.scan;

import java.time.Instant;

/**
 * Describes a code artifact uploaded to Contrast Scan
 *
 * <p>TODO[JG] JAVA-3298 move this to the Contrast Java SDK and flesh it out
 */
public interface CodeArtifact {

  /** @return unique ID of this code artifact */
  String id();

  String projectId();

  String organizationId();

  String filename();

  Instant createdTime();
}
