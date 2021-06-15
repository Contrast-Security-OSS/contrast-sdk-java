package com.contrastsecurity.utils;

import static com.contrastsecurity.http.RequestConstants.COMMA_DELIMITER;
import static com.contrastsecurity.http.RequestConstants.EQUALS_SEPARATOR;
import static com.contrastsecurity.http.RequestConstants.EXPAND_PARAM;
import static com.contrastsecurity.http.RequestConstants.QUERY_SEPARATOR;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class ContrastSDKUtils {

  public static String makeAuthorizationToken(String username, String serviceKey)
      throws IOException {
    String token = username.trim() + ":" + serviceKey.trim();

    return Base64.encodeBase64String(token.trim().getBytes("UTF-8")).trim();
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

    return QUERY_SEPARATOR
        + EXPAND_PARAM
        + EQUALS_SEPARATOR
        + StringUtils.join(values, COMMA_DELIMITER);
  }

  public static String buildExpand(EnumSet<?> values) {
    if (values == null || values.isEmpty()) {
      return "";
    }

    return QUERY_SEPARATOR
        + EXPAND_PARAM
        + EQUALS_SEPARATOR
        + StringUtils.join(values, COMMA_DELIMITER);
  }

  public static List<String> getSeverityList(String severity) {
    return SEVERITIES.subList(SEVERITIES.indexOf(severity), SEVERITIES.size());
  }

  public static final List<String> SEVERITIES =
      Arrays.asList("Note", "Low", "Medium", "High", "Critical");
}
