package com.contrastsecurity.sdk.scan;

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

import com.contrastsecurity.sdk.internal.Nullable;
import com.google.auto.value.AutoValue;

/** Value type that describes the scan structure returned by the Contrast API. */
@AutoValue
abstract class ScanInner {

  /** @return new {@link Builder} */
  static Builder builder() {
    return new AutoValue_ScanInner.Builder();
  }

  /** @return ID of this scan */
  abstract String id();

  /** @return ID of the project to which this scan belongs */
  abstract String projectId();

  /** @return ID of the organization to which this scan belongs */
  abstract String organizationId();

  /** @return scan status */
  abstract ScanStatus status();

  /** @return error message for failed scan, or {@code null} if the scan has not failed */
  @Nullable
  abstract String errorMessage();

  /** Builder for {@link ScanInner}. */
  @AutoValue.Builder
  abstract static class Builder {

    /** @see ScanInner#id() */
    abstract Builder id(String value);

    /** @see ScanInner#projectId() */
    abstract Builder projectId(String value);

    /** @see ScanInner#organizationId() */
    abstract Builder organizationId(String value);

    /** @see ScanInner#status() */
    abstract Builder status(ScanStatus value);

    /** @see ScanInner#errorMessage() */
    abstract Builder errorMessage(String value);

    /** @return new {@link ScanInner} */
    abstract ScanInner build();
  }
}
