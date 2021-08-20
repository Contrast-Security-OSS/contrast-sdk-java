package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.ServerResponseException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.IOException;
import java.util.Optional;

/**
 * Internal, low-level client for making requests for project resources.
 *
 * @apiNote every request to the Contrast API requires an organization ID. The organization ID
 *     parameter has been left off of the method signatures in this class, because providing it for
 *     every request is tedious. Instead, it is expected that implementations will provide an
 *     organization ID.
 */
interface ProjectClient {

  /**
   * Creates a new Scan project.
   *
   * @param create new project request
   * @return the new {@link ProjectInner}
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  ProjectInner create(final ProjectCreate create) throws IOException;

  /**
   * Scan project lookup.
   *
   * @param name project name to find
   * @return project, or empty if no such project exists
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  Optional<ProjectInner> findByName(final String name) throws IOException;
}
