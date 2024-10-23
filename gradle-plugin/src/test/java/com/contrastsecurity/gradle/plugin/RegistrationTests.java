package com.contrastsecurity.gradle.plugin;

import static org.junit.jupiter.api.Assertions.*;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

/** Smoke tests for proper plugin registration */
class RegistrationTests {
  @Test
  void pluginRegistersATask() {
    // Create a test project and apply the plugin
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("com.contrastsecurity.java");

    // Verify the result
    assertNotNull(project.getTasks().findByName("installAgent"));
  }
}
