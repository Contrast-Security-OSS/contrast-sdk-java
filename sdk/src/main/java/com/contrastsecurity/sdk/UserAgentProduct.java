package com.contrastsecurity.sdk;

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

import java.util.Objects;

/**
 * Value object that describes a product in a <a
 * href="https://httpwg.org/specs/rfc7231.html#header.user-agent">user-agent string</a>.
 */
public final class UserAgentProduct {

  /**
   * @param name product name
   * @return new {@code UserAgentProduct}
   * @throws NullPointerException when {@code name} is {@code null}
   */
  public static UserAgentProduct of(final String name) {
    return of(name, null, null);
  }

  /**
   * @param name product name
   * @param version product version
   * @return new {@code UserAgentProduct}
   * @throws NullPointerException when {@code name} is {@code null}
   */
  public static UserAgentProduct of(final String name, final String version) {
    return of(name, version, null);
  }

  /**
   * @param name product name
   * @param version product version
   * @param comment comment
   * @return new {@code UserAgentProduct}
   * @throws NullPointerException when {@code name} is {@code null}
   */
  public static UserAgentProduct of(final String name, final String version, final String comment) {
    return new UserAgentProduct(name, version, comment);
  }

  private final String name;
  private final String version;
  private final String comment;

  private UserAgentProduct(final String name, final String version, final String comment) {
    this.name = Objects.requireNonNull(name);
    this.version = version;
    this.comment = comment;
  }

  /**
   * @return user-agent product name
   */
  public String name() {
    return name;
  }

  /**
   * @return user-agent product version, or {@code null} if not set
   */
  public String version() {
    return version;
  }

  /**
   * @return user-agent product comment, or {@code null} if not set
   */
  public String comment() {
    return comment;
  }

  /**
   * @return user-agent encoded string i.e. "name/version (comment)"
   */
  public String toEncodedString() {
    final StringBuilder sb = new StringBuilder(name);
    if (version != null) {
      sb.append("/").append(version);
    }
    if (comment != null) {
      sb.append(" (").append(comment).append(")");
    }
    return sb.toString();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final UserAgentProduct that = (UserAgentProduct) o;
    return name.equals(that.name)
        && Objects.equals(version, that.version)
        && Objects.equals(comment, that.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, version, comment);
  }

  @Override
  public String toString() {
    return toEncodedString();
  }
}
