package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.ServerResponseException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Code artifact resource collection.
 *
 * <p>This resource API entry point does not use the {@code define()} pattern for creating new code
 * artifact resources, because in practice users always want to "upload" a file as a new code
 * artifact; therefore, this type exposes "upload" methods for transferring local files to Contrast
 * Scan as code artifacts.
 */
public interface CodeArtifacts {

  /** Factory for {@link CodeArtifacts} */
  interface Factory {

    /**
     * @param projectId ID of the project in which to manage code artifacts
     * @return new {@link CodeArtifacts}
     */
    CodeArtifacts create(String projectId);
  }

  /**
   * Transfers a file from the file system to Contrast Scan to create a new code artifact for static
   * analysis.
   *
   * @param file the code artifact to upload
   * @param name the name of the code artifact
   * @return new {@link CodeArtifact} from Contrast
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  CodeArtifact upload(Path file, String name) throws IOException;

  /**
   * Transfers a file from the file system to Contrast Scan to create a new code artifact for static
   * analysis.
   *
   * @param file the code artifact to upload
   * @return new {@link CodeArtifact} from Contrast
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  CodeArtifact upload(Path file) throws IOException;
}
