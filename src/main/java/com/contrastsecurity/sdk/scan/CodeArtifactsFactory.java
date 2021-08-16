package com.contrastsecurity.sdk.scan;

/** Factory for {@link CodeArtifacts} */
interface CodeArtifactsFactory {

  /**
   * @param projectId ID of the project in which to manage code artifacts
   * @return new {@link CodeArtifacts}
   */
  CodeArtifacts create(String projectId);
}
