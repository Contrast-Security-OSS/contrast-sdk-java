package com.contrastsecurity;

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

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/** Constants to reuse in Pact tests. */
public final class TestDataConstants {

  /**
   * Timestamp to use in Pact testing examples. Uses a date far in the past to easily distinguish
   * this from production data.
   */
  public static final Instant TIMESTAMP_EXAMPLE =
      OffsetDateTime.of(1955, 11, 12, 22, 4, 0, 0, ZoneOffset.UTC).toInstant();

  private TestDataConstants() {}
}
