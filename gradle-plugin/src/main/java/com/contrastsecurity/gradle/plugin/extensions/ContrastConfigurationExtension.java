package com.contrastsecurity.gradle.plugin.extensions;

import com.contrastsecurity.gradle.plugin.ContrastGradlePlugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

/** Extension for configuring TeamServer API Credentials for downloading agent */
public class ContrastConfigurationExtension {
  private final Property<String> username;
  private final Property<String> apiKey;
  private final Property<String> serviceKey;
  private final Property<String> apiUrl;
  private final Property<String> orgUuid;
  private final Property<String> appName;
  private final Property<String> serverName;
  private final Property<String> jarPath;
  private final Property<String> appVersion;
  private final Property<Boolean> attachToTests;

  public ContrastConfigurationExtension(final Project project) {
    username = project.getObjects().property(String.class);
    apiKey = project.getObjects().property(String.class);
    serviceKey = project.getObjects().property(String.class);
    apiUrl =
        project.getObjects().property(String.class).convention(ContrastGradlePlugin.DEFAULT_URL);
    orgUuid = project.getObjects().property(String.class);
    appName = project.getObjects().property(String.class).convention(project.getName());
    serverName = project.getObjects().property(String.class);
    jarPath = project.getObjects().property(String.class);
    appVersion =
        project
            .getObjects()
            .property(String.class)
            .convention(ContrastGradlePlugin.computeAppVersion(appName.get()));
    attachToTests = project.getObjects().property(Boolean.class);
  }

  public Property<String> getUsername() {
    return username;
  }

  public Property<String> getApiKey() {
    return apiKey;
  }

  public Property<String> getServiceKey() {
    return serviceKey;
  }

  public Property<String> getApiUrl() {
    return apiUrl;
  }

  public Property<String> getOrgUuid() {
    return orgUuid;
  }

  public Property<String> getAppName() {
    return appName;
  }

  public Property<String> getServerName() {
    return serverName;
  }

  public Property<String> getJarPath() {
    return jarPath;
  }

  public Property<String> getAppVersion() {
    return appVersion;
  }

  public Property<Boolean> getAttachToTests() {
    return attachToTests;
  }
}
