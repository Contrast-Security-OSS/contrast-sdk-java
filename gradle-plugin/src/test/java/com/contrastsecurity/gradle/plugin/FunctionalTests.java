package com.contrastsecurity.gradle.plugin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Simple placeholder tests verifying tasks in the plugin run successfully */
public class FunctionalTests {

  @TempDir File projectDir;

  private File getBuildFile() {
    return new File(projectDir, "build.gradle");
  }

  private File getSettingsFile() {
    return new File(projectDir, "settings.gradle");
  }

  @Test
  void canRunTasks() throws IOException {
    writeString(getSettingsFile(), "");
    writeString(getBuildFile(), "plugins {" + "  id('com.contrastsecurity.java')" + "}");

    // Run the build
    final GradleRunner helloRunner = GradleRunner.create();
    helloRunner.forwardOutput();
    helloRunner.withPluginClasspath();
    helloRunner.withArguments("hello");
    helloRunner.withProjectDir(projectDir);
    final BuildResult result1 = helloRunner.build();

    // Verify the result
    assertTrue(result1.getOutput().contains("hello"));

    final GradleRunner emptyRunner = GradleRunner.create();
    emptyRunner.forwardOutput();
    emptyRunner.withPluginClasspath();
    emptyRunner.withArguments("empty");
    emptyRunner.withProjectDir(projectDir);
    final BuildResult result2 = emptyRunner.build();

    // Verify the result
    assertTrue(
        result2.tasks(TaskOutcome.SUCCESS).stream()
            .anyMatch(buildTask -> buildTask.getPath().contains("empty")));
  }

  private void writeString(File file, String string) throws IOException {
    try (Writer writer = new FileWriter(file)) {
      writer.write(string);
    }
  }
}
