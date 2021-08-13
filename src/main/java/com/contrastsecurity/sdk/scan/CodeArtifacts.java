package com.contrastsecurity.sdk.scan;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Entry point to the Contrast Scan code artifacts management API.
 *
 * <p>This resource API entry point does not use the {@code define()} pattern for creating new code
 * artifact resources, because in practice users always want to "upload" a file as a new code
 * artifact; therefore, this type exposes "upload" methods for transferring local files to Contrast
 * Scan as code artifacts.
 */
public interface CodeArtifacts {

  /**
   * Uploads the given file to Contrast Scan to create a new code artifact.
   *
   * @param file the file to upload
   * @param name the name of the code artifact
   * @return new code artifact
   */
  CodeArtifact upload(Path file, String name) throws IOException;

  /**
   * Uploads the given file to Contrast Scan to create a new code artifact.
   *
   * @param file the file to upload
   * @return new code artifact
   */
  CodeArtifact upload(Path file) throws IOException;
}
