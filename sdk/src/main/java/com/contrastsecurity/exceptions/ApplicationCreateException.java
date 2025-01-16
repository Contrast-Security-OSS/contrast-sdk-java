package com.contrastsecurity.exceptions;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2025 Contrast Security, Inc.
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

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class ApplicationCreateException extends Exception {
  private final int rc;
  private final String responseMessage;

  public ApplicationCreateException(int rc, String responseMessage) {
    super("Recieved Response code: " + rc + " with message: " + responseMessage);
    this.rc = rc;
    this.responseMessage = responseMessage;
  }

  @Getter(AccessLevel.NONE)
  private static final long serialVersionUID = -9049287248312255189L;
}
