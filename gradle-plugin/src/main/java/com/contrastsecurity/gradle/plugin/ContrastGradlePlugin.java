package com.contrastsecurity.gradle.plugin;

import com.contrastsecurity.gradle.plugin.extensions.ContrastConfigurationExtension;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.UserAgentProduct;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;

/**
 * Gradle plugin for contrast utilities. The goals for this plugin are defined here <a
 * href=https://contrast.atlassian.net/browse/JAVA-8252>JAVA-8252</a>
 */
public class ContrastGradlePlugin implements Plugin<Project> {

    ContrastConfigurationExtension extension;

  public void apply(final Project target) {

     extension = target.getExtensions().create(EXTENSION_NAME, ContrastConfigurationExtension.class);

     //initialize instance of the ContrastSDK
     ContrastSDKService.initializeSdk(extension.getUsername(), extension.getServiceKey(), extension.getApiKey(), extension.getApiUrl());

    target.getTasks().register("installAgent", InstallAgentTask.class);
    target.getTasks().register("resolveAgent", ResolveAgentTask.class);

  }

  public static final String EXTENSION_NAME = "contrastConfiguration";
}
