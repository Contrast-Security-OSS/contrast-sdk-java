package com.contrastsecurity.utils;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2024 Contrast Security, Inc.
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

import org.junit.jupiter.api.Test;

/** Unit tests for {@link ContrastSDKUtils} */
final class ContrastSDKUtilsTest {

  @Test
  public void ensure_api_conditionally_transforms_input_to_match_api_uri() {
    final String expectedApi = "http://localhost:19080/Contrast/api";
    final String ensureUrlOne = ContrastSDKUtils.ensureApi("http://localhost:19080/Contrast/");
    final String ensureUrlTwo = ContrastSDKUtils.ensureApi("http://localhost:19080/Contrast");

    assertThat(ensureUrlOne).isEqualTo(expectedApi);
    assertThat(ensureUrlTwo).isEqualTo(expectedApi);

    final String unchangedApi = "http://localhost:19080/";
    final String ensureUnchanged = ContrastSDKUtils.ensureApi(unchangedApi);

    assertThat(ensureUnchanged).isEqualTo(unchangedApi);
  }

  @Test
  public void ensure_api_handles_urls_with_incorrect_schemes() {
    final String expectedUrl = "htp:/localhost:19080/Contrast/api";
    final String badUrl = "htp:/localhost:19080/Contrast/";
    final String actualUrl = ContrastSDKUtils.ensureApi(badUrl);
    assertThat(expectedUrl).isEqualTo(actualUrl);
  }

  @Test
  public void ensure_api_ignores_null() {
    final String nullUrl = ContrastSDKUtils.ensureApi(null);
    assertThat(nullUrl).isNull();
  }

  @Test
  public void ensure_api_handles_blank_string() {
    final String blankUrl = ContrastSDKUtils.ensureApi("");
    final String ensureBlank = "";
    assertThat(blankUrl).isEqualTo(ensureBlank);
  }
}
