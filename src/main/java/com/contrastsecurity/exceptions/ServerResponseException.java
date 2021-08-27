package com.contrastsecurity.exceptions;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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

/** Thrown when the Contrast API returns a response that the SDK does not understand. */
public class ServerResponseException extends ContrastException {

  /** @see RuntimeException#RuntimeException(String) */
  public ServerResponseException(final String message) {
    super(message);
  }

  /** @see RuntimeException#RuntimeException(String, Throwable) */
  public ServerResponseException(final String message, final Throwable inner) {
    super(message, inner);
  }
}