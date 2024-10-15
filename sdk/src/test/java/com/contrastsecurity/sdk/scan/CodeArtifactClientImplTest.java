package com.contrastsecurity.sdk.scan;

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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Unit tests for {@link CodeArtifactClientImpl} */
final class CodeArtifactClientImplTest {
  /**
   * Verifies that all well-known Java archive types are identified as a Java archive mime type
   *
   * <ul>
   *   <li>https://en.wikipedia.org/wiki/JAR_(file_format)
   *   <li>https://en.wikipedia.org/wiki/WAR_(file_format)
   *   <li>https://en.wikipedia.org/wiki/EAR_(file_format)
   * </ul>
   *
   * @param name file name
   */
  @ValueSource(strings = {"foo.jar", "foo.war", "foo.ear"})
  @ParameterizedTest
  void determine_content_type_java_archive(final String name) throws IOException {
    final Path file = Paths.get(name);
    final String mime = CodeArtifactClientImpl.determineMime(file);
    assertThat(mime).isEqualTo("application/java-archive");
  }

  /** Verifies that unknown file extensions use the generic application/octet-stream mime type */
  @Test
  void determine_content_type_unknown() throws IOException {
    final Path file = Paths.get("foo");
    final String mime = CodeArtifactClientImpl.determineMime(file);
    assertThat(mime).isEqualTo("application/octet-stream");
  }
}
