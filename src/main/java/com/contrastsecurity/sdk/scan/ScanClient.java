package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.ServerResponseException;
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
   * Retrieves a scan.
   *
   * @param projectId ID of the project to which the scan belongs
   * @param scanId ID of the scan
   * @return new {@link ScanInner} structure as returned by the API
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  ScanInner get(String projectId, String scanId) throws IOException;

  /**
   * Starts a new scan.
   *
   * @param projectId ID of the project in which to create the scan
   * @param create body of the create scan request
   * @return new {@link ScanInner} structure as returned by the API
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  ScanInner create(String projectId, ScanCreate create) throws IOException;

  /**
   * Retrieves scan results in SARIF.
   *
   * @param projectId ID of the project to which the scan belongs
   * @param scanId ID of the scan
   * @return {@link InputStream} for reading the SARIF file. The caller is expected to close the
   *     {@code InputStream}
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  InputStream getSarif(String projectId, String scanId) throws IOException;

  /**
   * Retrieves scan summary.
   *
   * @param projectId ID of the project to which the scan belongs
   * @param scanId ID of the scan
   * @return {@link InputStream} for reading the SARIF file. The caller is expected to close the
   *     {@code InputStream}
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  ScanSummaryInner getSummary(String projectId, String scanId) throws IOException;
}
