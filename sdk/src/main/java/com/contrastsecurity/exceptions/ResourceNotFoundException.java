package com.contrastsecurity.exceptions;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2026 Contrast Security, Inc.
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

/** An {@link HttpResponseException} throw when Contrast API returns a 404 Not Found response. */
public class ResourceNotFoundException extends HttpResponseException {

  /**
   * Constructor.
   *
   * @param message error message provided by the caller
   * @param status message from the status line e.g. Bad Request
   */
  public ResourceNotFoundException(
      final String message, final String method, final String path, final String status) {
    this(message, method, path, status, null);
  }

  /**
   * Constructor.
   *
   * @param message error message provided by the caller
   * @param status message from the status line e.g. Bad Request
   * @param body the body of the response, or {@code null} if there is no such body
   */
  public ResourceNotFoundException(
      final String message,
      final String method,
      final String path,
      final String status,
      final String body) {
    this(message, method, path, 404, status, body);
  }

  /**
   * Constructor. Package-private because the code should always be 404.
   *
   * @param message error message provided by the caller
   * @param code code from the status line e.g. 400
   * @param status message from the status line e.g. Bad Request
   * @param body the body of the response, or {@code null} if there is no such body
   * @throws IllegalArgumentException when code is not 404
   */
  ResourceNotFoundException(
      final String message,
      final String method,
      final String path,
      final int code,
      final String status,
      final String body) {
    super(message, method, path, code, status, body);
    if (code != 404) {
      throw new IllegalArgumentException("Only intended for 404 response codes but was " + code);
    }
  }
}
