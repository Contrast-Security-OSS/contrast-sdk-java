package com.contrastsecurity.maven.plugin;

/*-
 * #%L
 * Contrast Maven Plugin
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

import static org.assertj.core.api.Assertions.assertThat;

import com.contrastsecurity.sdk.UserAgentProduct;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link AbstractContrastMojo}. */
final class AbstractContrastMojoTest {

  @Test
  void creates_user_agent_product_with_expected_values() {
    // GIVEN some AbstractContrastMojo with the mavenVersion property injected
    final AbstractContrastMojo mojo =
        new AbstractContrastMojo() {
          @Override
          public void execute() {}
        };
    mojo.setMavenVersion("3.8.1");

    // WHEN build User-Agent product
    final UserAgentProduct ua = mojo.getUserAgentProduct();

    // THEN has expected values
    assertThat(ua.name()).isEqualTo("contrast-maven-plugin");
    assertThat(ua.version()).matches("\\d+\\.\\d+(\\.\\d+)?(-SNAPSHOT)?");
    assertThat(ua.comment()).isEqualTo("Apache Maven 3.8.1");
  }
}
