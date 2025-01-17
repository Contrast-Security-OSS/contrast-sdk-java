package com.contrastsecurity.sdk.scan;

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

/** Manager for Contrast Scan resource collections. */
public interface ScanManager {

  /**
   * @return {@link Projects} resource collection
   */
  Projects projects();

  /**
   * @param projectId project ID in which to manage code artifacts
   * @return {@link CodeArtifacts} resource collection
   */
  CodeArtifacts codeArtifacts(String projectId);

  /**
   * @param projectId project ID in which to manage code artifacts
   * @return {@link Scans} resource collection
   */
  Scans scans(String projectId);
}
