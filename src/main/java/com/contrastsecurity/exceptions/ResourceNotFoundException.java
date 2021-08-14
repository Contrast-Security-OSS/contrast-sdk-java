/*
 * Copyright (c) 2014, Contrast Security, LLC.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * Neither the name of the Contrast Security, LLC. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.contrastsecurity.exceptions;

/** An {@link HttpResponseException} throw when Contrast API returns a 404 Not Found response. */
public class ResourceNotFoundException extends HttpResponseException {

  /**
   * Constructor.
   *
   * @param status message from the status line e.g. Bad Request
   * @param message error message provided by the caller
   */
  public ResourceNotFoundException(String status, final String message) {
    this(status, message, null);
  }

  /**
   * Constructor.
   *
   * @param status message from the status line e.g. Bad Request
   * @param message error message provided by the caller
   * @param body the body of the response, or {@code null} if there is no such body
   */
  public ResourceNotFoundException(String status, final String message, final String body) {
    this(404, status, message, body);
  }

  /**
   * Constructor. Package-private because the code should always be 404.
   *
   * @param code code from the status line e.g. 400
   * @param status message from the status line e.g. Bad Request
   * @param message error message provided by the caller
   * @param body the body of the response, or {@code null} if there is no such body
   * @throws IllegalArgumentException when code is not 404
   */
  ResourceNotFoundException(
      final int code, final String status, final String message, final String body) {
    super(code, status, message, body);
    if (code != 404) {
      throw new IllegalArgumentException("Only intended for 404 response codes but was " + code);
    }
  }
}
