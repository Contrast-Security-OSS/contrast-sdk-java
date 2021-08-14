package com.contrastsecurity.sdk.internal;

import java.io.IOException;

/**
 * Describes a function that refreshes a resource by its unique ID.
 *
 * @param <T> resource type
 */
public interface RefreshById<T> {

  /**
   * @param <T> resource type
   * @return new {@code RefreshById} implementation that always throws {@code
   *     UnsupportedOperationException}.
   */
  static <T> RefreshById<T> unsupported() {
    return id -> {
      throw new UnsupportedOperationException();
    };
  }

  /**
   * Retrieve a new copy of a resource by its ID.
   *
   * @param id resource ID
   * @return new copy of the resource
   * @throws IOException when IO error occurs while making the request to the Contrast Scan API
   */
  T refresh(final String id) throws IOException;
}
