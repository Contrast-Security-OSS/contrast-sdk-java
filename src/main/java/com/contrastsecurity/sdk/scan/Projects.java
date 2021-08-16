package com.contrastsecurity.sdk.scan;

import java.io.IOException;
import java.util.Optional;

/** Projects resource collection API. */
public interface Projects {

  /**
   * Starts the definition for a new project resource.
   *
   * @return new definition builder
   */
  Project.Definition define();

  /**
   * @param name unique project name
   * @return project, or empty if no such project exists
   * @throws IOException when an IO error occurs while making the request to the Contrast Scan API
   */
  Optional<Project> findByName(final String name) throws IOException;
}
