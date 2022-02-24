package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.ServerResponseException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import java.io.IOException;

/** Scan resource collection API. */
public interface Scans {

  /** Factory for {@link Scans} */
  interface Factory {

    /**
     * @param projectId ID of the project in which to manage scans
     * @return new {@link Scans}
     */
    Scans create(String projectId);
  }

  /**
   * Starts the definition for a new scan resource.
   *
   * @return new definition builder
   */
  Scan.Definition define();

  /**
   * Retrieves a scan by ID.
   *
   * @param id scan ID
   * @return scan resource
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  Scan get(String id) throws IOException;

  /**
   * Retrieves a scan's summary by scan ID.
   *
   * @param id scan ID
   * @return summary of the scan identified by the given ID
   * @throws IOException when an IO error occurs while making the request to the Contrast API
   * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
   * @throws ResourceNotFoundException when the requested resource does not exist
   * @throws HttpResponseException when Contrast rejects this request with an error code
   * @throws ServerResponseException when Contrast API returns a response that cannot be understood
   */
  ScanSummary summary(String id) throws IOException;
}
