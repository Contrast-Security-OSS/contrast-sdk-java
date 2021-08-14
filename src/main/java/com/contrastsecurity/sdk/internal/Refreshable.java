package com.contrastsecurity.sdk.internal;

import java.io.IOException;

/**
 * Describes a resource that may be refreshed by requesting a new representation.
 *
 * @param <T> resource type
 */
public interface Refreshable<T> {

  /**
   * Retrieves a fresh copy of this immutable resource.
   *
   * @return new, refreshed copy of this resource.
   */
  T refresh() throws IOException;
}
