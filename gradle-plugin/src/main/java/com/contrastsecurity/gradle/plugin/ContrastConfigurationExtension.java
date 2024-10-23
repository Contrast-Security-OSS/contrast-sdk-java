package com.contrastsecurity.gradle.plugin;

/** Extension for configuring TeamServer API Credentials for downloading agent */
public class ContrastConfigurationExtension {
  private String username;
  private String apiKey;
  private String serviceKey;
  private String apiUrl;
  private String orgUuid;
  private String appName;
  private String serverName;
  private String jarPath;
  private String appVersion;
  private boolean attachToTests;

  public void setUsername(final String username) {
    this.username = username;
  }

  public void setApiKey(final String apiKey) {
    this.apiKey = apiKey;
  }

  public void setServiceKey(final String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public void setApiUrl(final String apiUrl) {
    this.apiUrl = apiUrl;
  }

  public void setOrgUuid(final String orgUuid) {
    this.orgUuid = orgUuid;
  }

  public void setAppName(final String appName) {
    this.appName = appName;
  }

  public void setServerName(final String serverName) {
    this.serverName = serverName;
  }

  public void setJarPath(final String jarPath) {
    this.jarPath = jarPath;
  }

  public void setAppVersion(final String appVersion) {
    this.appVersion = appVersion;
  }

  public void setAttachToTests(final boolean attachToTests) {
    this.attachToTests = attachToTests;
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

  public boolean getAttachToTests() {
    return attachToTests;
  }
}
