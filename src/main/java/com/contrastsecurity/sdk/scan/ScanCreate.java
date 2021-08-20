package com.contrastsecurity.sdk.scan;

import com.google.auto.value.AutoValue;

/** Models the JSON body of a "create scan" request. */
@AutoValue
abstract class ScanCreate {

  /**
   * @param codeArtifactId ID of the code artifact to scan
   * @param label label that distinguishes this scan from others in the project
   * @return new {@link ScanCreate}
   */
  static ScanCreate of(final String codeArtifactId, final String label) {
    return new AutoValue_ScanCreate(codeArtifactId, label);
  }

  /** @return ID of the code artifact to scan */
  abstract String codeArtifactId();

  /** @return label that distinguishes this scan from others in the project */
  abstract String label();
}
