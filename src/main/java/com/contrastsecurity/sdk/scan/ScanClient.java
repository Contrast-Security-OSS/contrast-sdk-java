package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Internal, low-level client for making requests for scan resources.
 *
 * @apiNote every request to the Contrast API requires an organization ID. The organization ID
 *     parameter has been left off of the method signatures in this class, because providing it for
 *     every request is tedious. Instead, it is expected that implementations will provide an
 *     organization ID.
 */
interface ScanClient {

  /**
   * @param projectId ID of the project to which the scan belongs
   * @param scanId ID of the scan
   * @return new {@link ScanInner} structure as returned by the API
   * @throws IOException when an IO error occurs making the request
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   */
  ScanInner get(String projectId, String scanId) throws IOException;

  /**
   * @param projectId ID of the project in which to create the scan
   * @param create body of the create scan request
   * @return new {@link ScanInner} structure as returned by the API
   * @throws IOException when an IO error occurs making the request
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   */
  ScanInner create(String projectId, ScanCreate create) throws IOException;

  /**
   * @param projectId ID of the project to which the scan belongs
   * @param scanId ID of the scan
   * @return {@link InputStream} for reading the SARIF file. The caller is expected to close the
   *     {@code InputStream}
   * @throws IOException when an IO error occurs making the request
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   */
  InputStream getSarif(String projectId, String scanId) throws IOException;
}
