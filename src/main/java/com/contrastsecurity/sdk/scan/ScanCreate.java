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

import com.google.auto.value.AutoValue;

/** Models the JSON body of a "create scan" request. */
@AutoValue
abstract class ScanCreate {

  /**
   * @param codeArtifactId ID of the code artifact to scan
   * @param label label that distinguishes this scan from others in the project
   * @return new {@link ScanCreate}
   */
  static ScanCreate of(final String codeArtifactId, final String label) {
    return new AutoValue_ScanCreate(codeArtifactId, label);
  }

  /** @return ID of the code artifact to scan */
  abstract String codeArtifactId();

  /** @return label that distinguishes this scan from others in the project */
  abstract String label();
}
