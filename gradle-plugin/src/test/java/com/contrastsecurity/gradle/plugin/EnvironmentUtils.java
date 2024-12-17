package com.contrastsecurity.gradle.plugin;

/** Utility class for retrieving Contrast API credentials for testing from Environment vars */
public class EnvironmentUtils {

  public static String getUsername() {
    return System.getenv("CONTRAST__API__USER_NAME");
  }

  public static String getApiUrl() {
    return System.getenv("CONTRAST__API__URL");
  }

  public static String getServiceKey() {
    return System.getenv("CONTRAST__API__SERVICE_KEY");
  }

  public static String getApiKey() {
    return System.getenv("CONTRAST__API__API_KEY");
  }

  public static String getOrgUuid() {
    return System.getenv("CONTRAST__API__ORGANIZATION_ID");
  }
}
