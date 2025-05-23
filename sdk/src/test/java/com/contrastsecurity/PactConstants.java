package com.contrastsecurity;

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

/** Constants to reuse in Pact tests. */
public final class PactConstants {

  /**
   * Datetime format string to use for Pact datetime matchers. Matches both ISO8601 datetime formats
   * returned by the scan API.
   */
  public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX";

  /** static members only */
  private PactConstants() {}
}
