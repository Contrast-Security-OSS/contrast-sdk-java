package com.contrastsecurity.maven.plugin.it.stub;

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

import java.util.Objects;

/**
 * {@link ContrastAPI} implementation that represents an external system. Methods that affect the
 * system such as {@code start()} and {@code stop()} are no-ops.
 */
final class ExternalContrastAPI implements ContrastAPI {

  private final ConnectionParameters connection;

  /**
   * @param connection the connection parameters constant to provide to users
   */
  public ExternalContrastAPI(final ConnectionParameters connection) {
    this.connection = Objects.requireNonNull(connection);
  }

  /** nop */
  @Override
  public void start() {}

  @Override
  public ConnectionParameters connection() {
    return connection;
  }

  /** nop */
  @Override
  public void stop() {}
}
