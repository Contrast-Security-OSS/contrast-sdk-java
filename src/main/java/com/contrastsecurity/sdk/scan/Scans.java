package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.IOException;

/** Scan resource collection API. */
public interface Scans {

  /**
   * Starts the definition for a new scan resource.
   *
   * @return new definition builder
   */
  Scan.Definition define();

  /**
   * Retrieves a scan by ID
   *
   * @param id unique scan ID
   * @return scan resource
   * @throws IOException when an IO error occurs while making the request to the Contrast Scan API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   */
  Scan get(String id) throws IOException;
}
