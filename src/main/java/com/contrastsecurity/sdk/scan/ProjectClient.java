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
