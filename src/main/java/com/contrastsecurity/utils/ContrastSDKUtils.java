package com.contrastsecurity.utils;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
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

import static com.contrastsecurity.http.RequestConstants.COMMA_DELIMITER;
import static com.contrastsecurity.http.RequestConstants.EQUALS_SEPARATOR;
import static com.contrastsecurity.http.RequestConstants.EXPAND_PARAM;
import static com.contrastsecurity.http.RequestConstants.QUERY_SEPARATOR;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ContrastSDKUtils {

  public static String makeAuthorizationToken(String username, String serviceKey) {
    String token = username.trim() + ":" + serviceKey.trim();

    return Base64.getEncoder().encodeToString(token.trim().getBytes(StandardCharsets.UTF_8)).trim();
  }

  public static void validateUrl(String url) throws IllegalArgumentException {
    URL u;
    try {
      u = new URL(url);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Invalid URL");
    }
    if (!u.getProtocol().startsWith("http")) {
      throw new IllegalArgumentException("Invalid protocol");
    }
  }

  public static String ensureApi(String url) {
    if (url != null) {
      if (url.endsWith("/Contrast")) {
        url = url + "/api";
      } else if (url.endsWith("/Contrast/")) {
        url = url + "api";
      }
    }
    return url;
  }

  public static String buildExpand(String... values) {
    if (values == null || values.length == 0) {
      return "";
    }

    return QUERY_SEPARATOR + EXPAND_PARAM + EQUALS_SEPARATOR + String.join(COMMA_DELIMITER, values);
  }

  public static String buildExpand(EnumSet<?> values) {
    if (values == null || values.isEmpty()) {
      return "";
    }

    return QUERY_SEPARATOR
        + EXPAND_PARAM
        + EQUALS_SEPARATOR
        + String.join(
            COMMA_DELIMITER, values.stream().map(Object::toString).collect(Collectors.toSet()));
  }

  public static List<String> getSeverityList(String severity) {
    return SEVERITIES.subList(SEVERITIES.indexOf(severity), SEVERITIES.size());
  }

  public static final List<String> SEVERITIES =
      Arrays.asList("Note", "Low", "Medium", "High", "Critical");
}
