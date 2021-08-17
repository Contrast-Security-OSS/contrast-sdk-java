package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Internal, low-level client for making requests for code artifact resources.
 *
 * @apiNote every request to the Contrast API requires an organization ID. The organization ID
 *     parameter has been left off of the method signatures in this class, because providing it for
 *     every request is tedious. Instead, it is expected that implementations will provide an
 *     organization ID.
 */
interface CodeArtifactClient {

  /**
   * Transfers a file from the file system to Contrast Scan to create a new code artifact for
   * analysis.
   *
   * @param projectId ID of the project to which the code artifact belongs
   * @param file the file to upload
   * @return new {@link CodeArtifact} from Contrast API
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   */
  CodeArtifactInner upload(String projectId, Path file) throws IOException;
}
