package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2026 Contrast Security, Inc.
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

/**
 * Describes the Contrast Scan code artifact resource. Code artifacts are applications uploaded to
 * Contrast Scan for analysis.
 */
public interface CodeArtifact {

  /**
   * @return ID of this code artifact
   */
  String id();

  /**
   * @return ID of the project to which this code artifact belongs
   */
  String projectId();

  /**
   * @return ID of the organization to which this code artifact belongs
   */
  String organizationId();

  /**
   * @return filename
   */
  String filename();

  /**
   * @return time at which the code artifact was uploaded to Contrast Scan
   */
  Instant createdTime();
}
