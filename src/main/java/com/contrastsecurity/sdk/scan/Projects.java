package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.ServerResponseException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.IOException;
import java.util.Optional;

/** Project resource collection. */
public interface Projects {

  /** Factory for {@link Projects} */
  interface Factory {
    Projects create();
  }

  /**
   * Starts the definition for a new project resource.
   *
   * @return new definition builder
   */
  Project.Definition define();

  /**
   * @param name project name
   * @return project, or empty if no such project exists
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  Optional<Project> findByName(final String name) throws IOException;
}
