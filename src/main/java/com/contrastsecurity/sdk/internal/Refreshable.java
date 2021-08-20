package com.contrastsecurity.sdk.internal;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
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
   * @return new, refreshed copy of this resource
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   */
  T refresh() throws IOException;
}
