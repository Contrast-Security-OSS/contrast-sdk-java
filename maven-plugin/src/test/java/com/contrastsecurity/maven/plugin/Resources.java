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

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/** static utilities for retrieving test resources */
public final class Resources {

  /**
   * Retrieves the given test resources as a {@link File}. Fails if the resource does not exist
   *
   * @param name resource name
   * @return {@link File} which refers to the resource
   * @throws NullPointerException when resource does not exist
   */
  public static Path file(final String name) {
    final URL resource = Resources.class.getResource(name);
    if (resource == null) {
      throw new NullPointerException(name + " resource not found");
    }
    try {
      return Paths.get(resource.toURI());
    } catch (final URISyntaxException e) {
      throw new AssertionError("This should never happen", e);
    }
  }

  /**
   * Retrieves the given test resource as an {@link InputStream}. Fails if the resource does not
   * exist
   *
   * @param name resource name
   * @return {@link InputStream} for reading the resource
   * @throws NullPointerException when resource does not exist
   */
  public static InputStream stream(final String name) {
    final InputStream stream = Resources.class.getResourceAsStream(name);
    if (stream == null) {
      throw new NullPointerException(name + " resource not found");
    }
    return stream;
  }
}
