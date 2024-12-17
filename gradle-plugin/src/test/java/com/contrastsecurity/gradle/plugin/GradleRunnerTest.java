package com.contrastsecurity.gradle.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.junit.jupiter.api.io.TempDir;

/**
 * Super class for Tests using the {@link org.gradle.testkit.runner.GradleRunner} to provide build
 * files and setting files
 */
public class GradleRunnerTest {
  @TempDir public File projectDir;

  public File getBuildFile() {
    return new File(projectDir, "build.gradle");
  }

  public File getSettingsFile() {
    return new File(projectDir, "settings.gradle");
  }

  public void writeString(File file, String string) throws IOException {
    try (Writer writer = new FileWriter(file)) {
      writer.write(string);
    }
  }

  public String writeConfig() {
    final String testConfig = "plugins {  id('com.contrastsecurity.java') }\n";

    return testConfig;
  }
}
