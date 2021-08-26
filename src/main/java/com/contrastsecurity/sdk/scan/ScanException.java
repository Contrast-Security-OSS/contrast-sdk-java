package com.contrastsecurity.sdk.scan;

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

import com.contrastsecurity.exceptions.ContrastException;
import java.util.Objects;

/** Indicates a condition that prevents Contrast Scan from completing an analysis */
public class ScanException extends ContrastException {

  private final Scan scan;

  /**
   * @param scan the scan that cannot be completed
   * @param message exception message
   */
  public ScanException(final Scan scan, final String message) {
    super(message);
    this.scan = Objects.requireNonNull(scan);
  }

  /** @return the scan that cannot be completed */
  public final Scan scan() {
    return scan;
  }
}
