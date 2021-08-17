package com.contrastsecurity.sdk.scan;

import java.time.Instant;

/**
 * Describes the Contrast Scan code artifact resource. Code artifacts are applications uploaded to
 * Contrast Scan for analysis.
 */
public interface CodeArtifact {

  /** @return ID of this code artifact */
  String id();

  /** @return ID of the project to which this code artifact belongs */
  String projectId();

  /** @return ID of the organization to which this code artifact belongs */
  String organizationId();

  /** @return filename */
  String filename();

  /** @return time at which the code artifact was uploaded to Contrast Scan */
  Instant createdTime();
}
