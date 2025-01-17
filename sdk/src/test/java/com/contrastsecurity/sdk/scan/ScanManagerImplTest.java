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

import static org.assertj.core.api.Assertions.assertThat;

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.ContrastSDK.Builder;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link ScanManagerImpl} */
final class ScanManagerImplTest {

  /**
   * {@code ScanManagerImpl} simply initializes resource collections and provides access to them.
   * This test verifies that this happens without errors.
   */
  @Test
  void initialization_smoke_test() {
    final ContrastSDK contrast = new Builder("username", "service-key", "api-key").build();
    final Gson gson = GsonFactory.create();
    final ScanManagerImpl manager = new ScanManagerImpl(contrast, gson, "organization-id");
    final Scans scans = manager.scans("project-id");
    assertThat(scans).isNotNull();
    final CodeArtifacts codeArtifacts = manager.codeArtifacts("project-id");
    assertThat(codeArtifacts).isNotNull();
  }
}
