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

import com.contrastsecurity.sdk.internal.Lists;
import com.google.auto.value.AutoValue;
import java.util.Collection;
import java.util.Collections;

/**
 * Value type that represents the body of the request to the Contrast Scan API for creating a new
 * project.
 */
@AutoValue
abstract class ProjectCreate {

  /**
   * @return new {@link ScanInner.Builder}
   */
  static ProjectCreate.Builder builder() {
    return new AutoValue_ProjectCreate.Builder()
        .includeNamespaceFilters(Collections.emptyList())
        .excludeNamespaceFilters(Collections.emptyList());
  }

  /**
   * @return project name
   */
  abstract String name();

  /**
   * @return programming language used by this project
   */
  abstract String language();

  /**
   * @return collection of code namespaces to include in scans
   */
  abstract Collection<String> includeNamespaceFilters();

  /**
   * @return collection of code namespaces to exclude from scans
   */
  abstract Collection<String> excludeNamespaceFilters();

  /** Builder for {@link ProjectInner}. */
  @AutoValue.Builder
  abstract static class Builder {

    /**
     * @see ProjectInner#name()
     */
    abstract Builder name(String value);

    /**
     * @see ProjectInner#language()
     */
    abstract Builder language(String value);

    /**
     * @see ProjectInner#language()
     */
    abstract Builder includeNamespaceFilters(Collection<String> value);

    /**
     * Accessor for making defensive copies.
     *
     * @return user provided value
     */
    abstract Collection<String> includeNamespaceFilters();

    /**
     * @see ProjectInner#language()
     */
    abstract Builder excludeNamespaceFilters(Collection<String> value);

    /**
     * Accessor for making defensive copies.
     *
     * @return user provided value
     */
    abstract Collection<String> excludeNamespaceFilters();

    /** AutoValue implemented build method. Use {@code build()} instead. */
    abstract ProjectCreate autoBuild();

    /**
     * @return new {@link ProjectInner}
     */
    final ProjectCreate build() {
      // make defensive copies
      includeNamespaceFilters(Lists.copy(includeNamespaceFilters()));
      excludeNamespaceFilters(Lists.copy(excludeNamespaceFilters()));
      return autoBuild();
    }
  }
}
