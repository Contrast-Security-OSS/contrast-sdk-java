package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
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
   * @throws IOException when an IO error occurs making the request
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   */
  Optional<Project> findByName(final String name) throws IOException;
}
