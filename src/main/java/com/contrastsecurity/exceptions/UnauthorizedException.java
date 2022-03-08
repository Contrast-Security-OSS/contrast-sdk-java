package com.contrastsecurity.exceptions;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 Contrast Security, Inc.
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

import java.net.HttpURLConnection;

/** An {@link HttpResponseException} throw when Contrast API returns a 401 or 403 response. */
public class UnauthorizedException extends HttpResponseException {

  /**
   * Constructor. Package-private because the code should always be 404.
   *
   * @param message error message provided by the caller
   * @param code code from the status line e.g. 401
   * @param status message from the status line e.g. Unauthorized
   * @throws IllegalArgumentException when code is not 401 or 403
   */
  public UnauthorizedException(
      final String message,
      final String method,
      final String path,
      final int code,
      final String status) {
    this(message, method, path, code, status, null);
  }

  /**
   * Constructor. Package-private because the code should always be 404.
   *
   * @param message error message provided by the caller
   * @param code code from the status line e.g. 401
   * @param status message from the status line e.g. Unauthorized
   * @param body the body of the response, or {@code null} if there is no such body
   * @throws IllegalArgumentException when code is not 401 or 403
   */
  public UnauthorizedException(
      final String message,
      final String method,
      final String path,
      final int code,
      final String status,
      final String body) {
    super(message, method, path, code, status, body);
    if (code != HttpURLConnection.HTTP_UNAUTHORIZED && code != HttpURLConnection.HTTP_FORBIDDEN) {
      throw new IllegalArgumentException("This exception is only used for statuses 401 and 403");
    }
  }

  /**
   * Constructor.
   *
   * @param code code from the status line e.g. 401
   * @deprecated Used by legacy code for any HTTP 4XX response. Use {@link
   *     #UnauthorizedException(String, String, String, int, String)} instead.
   */
  @Deprecated
  public UnauthorizedException(final int code) {
    super("", "<unknown>", "<unknown>", code, "");
  }
}
