package com.contrastsecurity.gradle.plugin.util;

/**
 * POJO for creating {@link
 * com.contrastsecurity.gradle.plugin.extensions.ContrastConfigurationExtension} values for Testing
 */
public class ConfigurationExtensionValues {
  private final String username;
  private final String apiKey;
  private final String serviceKey;
  private final String apiUrl;
  private final String orgUuid;
  private final String appName;
  private final String serverName;
  private final String jarPath;
  private final String appVersion;
  private final String minSeverity;
  private final String attachToTests;

  private ConfigurationExtensionValues(final Builder builder) {
    this.username = builder.username;
    this.apiKey = builder.apiKey;
    this.serviceKey = builder.serviceKey;
    this.apiUrl = builder.apiUrl;
    this.orgUuid = builder.orgUuid;
    this.appName = builder.appName;
    this.serverName = builder.serverName;
    this.jarPath = builder.jarPath;
    this.appVersion = builder.appVersion;
    this.minSeverity = builder.minSeverity;
    this.attachToTests = builder.attachToTests;
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

  public String getMinSeverity() {
    return minSeverity;
  }

  public String getAttachToTests() {
    return attachToTests;
  }

  public static class Builder {
    private String username;
    private String apiKey;
    private String serviceKey;
    private String apiUrl;
    private String orgUuid;
    private String appName;
    private String serverName;
    private String jarPath;
    private String appVersion;
    private String minSeverity;
    private String attachToTests;

    public Builder setUsername(String username) {
      this.username = username;
      return this;
    }

    public Builder setApiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public Builder setServiceKey(String serviceKey) {
      this.serviceKey = serviceKey;
      return this;
    }

    public Builder setApiUrl(String apiUrl) {
      this.apiUrl = apiUrl;
      return this;
    }

    public Builder setOrgUuid(String orgUuid) {
      this.orgUuid = orgUuid;
      return this;
    }

    public Builder setAppName(String appName) {
      this.appName = appName;
      return this;
    }

    public Builder setServerName(String serverName) {
      this.serverName = serverName;
      return this;
    }

    public Builder setJarPath(String jarPath) {
      this.jarPath = jarPath;
      return this;
    }

    public Builder setAppVersion(String appVersion) {
      this.appVersion = appVersion;
      return this;
    }

    public Builder setMinSeverity(String minSeverity) {
      this.minSeverity = minSeverity;
      return this;
    }

    public Builder setAttachToTests(String attachToTests) {
      this.attachToTests = attachToTests;
      return this;
    }

    public ConfigurationExtensionValues build() {
      return new ConfigurationExtensionValues(this);
    }
  }

  public String buildContrastBuildFile() {
    return "plugins {  id('com.contrastsecurity.java') \n id 'java' }\n"
        + "contrastConfiguration {\n"
        + "  username = "
        + "'"
        + this.getUsername()
        + "'"
        + "\n"
        + "  apiKey = "
        + "'"
        + this.getApiKey()
        + "'"
        + "\n"
        + "  serviceKey = "
        + "'"
        + this.getServiceKey()
        + "'"
        + "\n"
        + "  apiUrl = "
        + "'"
        + this.getApiUrl()
        + "'"
        + "\n"
        + "  orgUuid = "
        + "'"
        + this.getOrgUuid()
        + "'"
        + "\n"
        + "  appName = ' "
        + this.getAppName()
        + "'\n"
        + "  serverName = ' "
        + this.getServerName()
        + "'\n"
        + "  appVersion = '"
        + this.getAppVersion()
        + "'\n"
        + "  attachToTests = "
        + this.getAttachToTests()
        + "\n"
        + "  jarPath = ' "
        + this.getJarPath()
        + "'\n"
        + "  minSeverity = '"
        + this.getMinSeverity()
        + "'\n"
        + "}\n";
  }
}
