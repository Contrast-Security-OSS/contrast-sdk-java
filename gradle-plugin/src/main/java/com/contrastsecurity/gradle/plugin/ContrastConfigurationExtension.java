package com.contrastsecurity.gradle.plugin;

/** Extension for configuring TeamServer API Credentials for downloading agent */
public class ContrastConfigurationExtension {
  final String username;
  final String apiKey;
  final String serviceKey;
  final String apiUrl;
  final String orgUuid;
  final String appName;
  final String serverName;
  final String jarPath;
  final String appVersion;

  // default constructor with null values
  // this is shit figure out what gradle wants
  public ContrastConfigurationExtension() {
    this.username = null;
    this.apiKey = null;
    this.serviceKey = null;
    this.apiUrl = null;
    this.orgUuid = null;
    this.appName = null;
    this.serverName = null;
    this.jarPath = null;
    this.appVersion = null;
  }

  public ContrastConfigurationExtension(
      final String username,
      final String apiKey,
      final String serviceKey,
      final String apiUrl,
      final String orgUuid,
      final String appName,
      final String serverName,
      final String jarPath,
      final String appVersion) {
    this.username = username;
    this.apiKey = apiKey;
    this.serviceKey = serviceKey;
    this.apiUrl = apiUrl;
    this.orgUuid = orgUuid;
    this.appName = appName;
    this.serverName = serverName;
    this.jarPath = jarPath;
    this.appVersion = appVersion;
  }

  public String getUsername() {
    return username;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public String getOrgUuid() {
    return orgUuid;
  }

  public String getAppName() {
    return appName;
  }

  public String getServerName() {
    return serverName;
  }

  public String getJarPath() {
    return jarPath;
  }

  public String getAppVersion() {
    return appVersion;
  }
}
