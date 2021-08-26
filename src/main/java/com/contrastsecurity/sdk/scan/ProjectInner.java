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

import com.contrastsecurity.sdk.internal.Nullable;
import com.google.auto.value.AutoValue;
import java.time.Instant;
import java.util.Collection;

/** Value type that represents the project as returned by the Contrast API. */
@AutoValue
abstract class ProjectInner {

  /** @return new builder */
  static Builder builder() {
    return new AutoValue_ProjectInner.Builder()
        .critical(0)
        .high(0)
        .medium(0)
        .low(0)
        .note(0)
        .archived(false)
        .completedScans(0);
  }

  /** @return project ID */
  abstract String id();

  /** @return organization ID */
  abstract String organizationId();

  /** @return project name */
  abstract String name();

  /** @return true, if the project has been archived */
  abstract boolean archived();

  /** @return programming language used by this project */
  abstract String language();

  /**
   * @return count of critical severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  abstract int critical();

  /**
   * @return count of high severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  abstract int high();

  /**
   * @return count of medium severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  abstract int medium();

  /**
   * @return count of low severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  abstract int low();

  /**
   * @return count of note severity vulnerabilities detected in the last scan to complete
   *     successfully
   */
  abstract int note();

  /**
   * @return ID of the last scan to complete successfully, or {@code null} if no such scan exists
   */
  @Nullable
  abstract String lastScanId();

  /**
   * @return the time at which the last successfully completed scan finished, or {@code null} if no
   *     such scan exists
   */
  @Nullable
  abstract Instant lastScanTime();

  /** @return count of completed scans in this project */
  abstract int completedScans();

  /** @return collection of code namespaces to include in the scan */
  abstract Collection<String> includeNamespaceFilters();

  /** @return collection of code namespaces to exclude from the scan */
  abstract Collection<String> excludeNamespaceFilters();

  /** Builder for {@link ProjectInner} */
  @AutoValue.Builder
  abstract static class Builder {

    /** @see ProjectInner#id() */
    abstract Builder id(String value);

    /** @see ProjectInner#organizationId() */
    abstract Builder organizationId(String value);

    /** @see ProjectInner#name() */
    abstract Builder name(String value);

    /** @see ProjectInner#archived() ) */
    abstract Builder archived(boolean value);

    /** @see ProjectInner#language() */
    abstract Builder language(String value);

    /** @see ProjectInner#critical() ) */
    abstract Builder critical(int value);

    /** @see ProjectInner#high() */
    abstract Builder high(int value);

    /** @see ProjectInner#medium() */
    abstract Builder medium(int value);

    /** @see ProjectInner#low() */
    abstract Builder low(int value);

    /** @see ProjectInner#note() */
    abstract Builder note(int value);

    /** @see ProjectInner#lastScanId() */
    abstract Builder lastScanId(String value);

    /** @see ProjectInner#lastScanTime() () () */
    abstract Builder lastScanTime(Instant value);

    /** @see ProjectInner#completedScans() */
    abstract Builder completedScans(int value);

    /** @see ProjectInner#includeNamespaceFilters() */
    abstract Builder includeNamespaceFilters(Collection<String> value);

    /** @see ProjectInner#excludeNamespaceFilters() */
    abstract Builder excludeNamespaceFilters(Collection<String> value);

    /** new {@link ProjectInner} */
    abstract ProjectInner build();
  }
}
