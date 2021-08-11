package com.contrastsecurity.scan;

import com.contrastsecurity.sdk.ContrastSDK;
import java.util.Optional;

/** Entrypoint for the Contrast Scan projects management API. */
public interface Projects {

  static Projects projects(final ContrastSDK sdk, final String organizationId) {
    throw new UnsupportedOperationException("not yet implemented");
  }

  /**
   * Starts the definition for a new top-level project resource.
   *
   * @return new project definition builder
   */
  Project.Definition define();

  Project get(final String id);

  Optional<Project> findByName(final String name);
}
