package com.contrastsecurity.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Gradle plugin for contrast utilities. The goals for this plugin are defined here <a
 * href=https://contrast.atlassian.net/browse/JAVA-8252>JAVA-8252</a>
 */
public class ContrastGradlePlugin implements Plugin<Project> {

  public void apply(final Project target) {

    ContrastConfigurationExtension extension =
        target.getExtensions().create(EXTENSION_NAME, ContrastConfigurationExtension.class);

    target.getTasks().register("installAgent", InstallAgentTask.class);
  }

  public static final String EXTENSION_NAME = "contrastConfiguration";
}
