package com.contrastsecurity.scan;

import java.nio.file.Path;

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
    Project create();
  }

  /** @return project name */
  String name();

  /** @return project programming language */
  String language();

  /**
   * Uploads the given file as a new code artifact for scanning.
   *
   * @param file the file to upload
   * @return new {@link CodeArtifact}
   */
  CodeArtifact uploadCodeArtifact(Path file);
}
