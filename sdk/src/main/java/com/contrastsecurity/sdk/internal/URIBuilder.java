package com.contrastsecurity.sdk.internal;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Helper for building URI strings.
 *
 * <p>High-level HTTP clients typically have this functionality built-in, but we are using {@code
 * java.net.HttpURLConnection} and have no such feature available to us, so we added this basic one.
 */
public final class URIBuilder {

  private final List<String> segments = new ArrayList<>();
  private final Map<String, String> query = new HashMap<>();

  /**
   * Appends the given path segments to the URI.
   *
   * @param segments one or more path segments to append
   * @return this
   * @throws IllegalArgumentException when empty or any path segment is empty
   * @throws NullPointerException if any segment is {@code null}
   */
  public URIBuilder appendPathSegments(final String... segments) {
    for (final String segment : segments) {
      Objects.requireNonNull(segment);
    }
    this.segments.addAll(Arrays.asList(segments));
    return this;
  }

  /**
   * Appends the given query parameter to the URI. Does not support duplicate query parameters,
   * because we do not need this functionality yet.
   *
   * @param name query parameter name
   * @param value query parameter value
   * @return this
   * @throws NullPointerException if any argument is {@code null}
   */
  public URIBuilder appendQueryParam(final String name, final String value) {
    if (Objects.requireNonNull(name).isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    }
    query.put(name, value);
    return this;
  }

  /**
   * Appends the given query parameter to the URI. Does not support duplicate query parameters,
   * because we do not need this functionality yet.
   *
   * @param name query parameter name
   * @param value query parameter value
   * @return this
   * @throws NullPointerException if any argument is {@code null}
   */
  public URIBuilder appendQueryParam(final String name, final boolean value) {
    return appendQueryParam(name, String.valueOf(value));
  }

  /**
   * @return the URI as a {@code String}
   */
  public String toURIString() {
    final String path =
        "/" + segments.stream().map(URIBuilder::encode).collect(Collectors.joining("/"));
    final String query =
        this.query.entrySet().stream()
            .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
            .collect(Collectors.joining("&"));
    return query.isEmpty() ? path : path + "?" + query;
  }

  @Override
  public String toString() {
    return toURIString();
  }

  private static String encode(final String s) {
    try {
      return URLEncoder.encode(s, StandardCharsets.UTF_8.name());
    } catch (final UnsupportedEncodingException e) {
      throw new AssertionError("This should never happen", e);
    }
  }
}
