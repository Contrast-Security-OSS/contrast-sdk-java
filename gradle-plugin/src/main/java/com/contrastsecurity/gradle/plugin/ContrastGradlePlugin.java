package com.contrastsecurity.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Gradle plugin for contrast utilities. The goals for this plugin are defined here <a
 * href=https://contrast.atlassian.net/browse/JAVA-8252>JAVA-8252</a>
 */
public class ContrastGradlePlugin implements Plugin<Project> {
  public void apply(final Project target) {
    target
        .getTasks()
        .register("hello", task -> task.doLast(s -> System.out.println("HelloWorld!")));

    target.getTasks().register("empty", EmptyTask.class);
  }
}
