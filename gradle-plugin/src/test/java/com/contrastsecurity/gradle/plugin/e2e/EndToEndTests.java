package com.contrastsecurity.gradle.plugin.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.contrastsecurity.gradle.plugin.EnvironmentUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** End-To-End tests for the gradle plugin for interacting with TeamServer */
public class EndToEndTests {

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

  @Test
  void verify_plugin_retrieves_agent_from_TS() throws IOException {
    writeString(getSettingsFile(), "");
    String config = writeContrastBuildFile();
    writeString(getBuildFile(), config);

    final GradleRunner testRunner = GradleRunner.create();
    testRunner.forwardOutput();
    testRunner.withPluginClasspath();
    // run with debug args to log statements we can check for in the output
    testRunner.withArguments("resolveAgent", "--debug");
    testRunner.withProjectDir(projectDir);
    final BuildResult result = testRunner.build();

    result
        .getTasks()
        .forEach(
            buildTask -> {
              assertEquals(buildTask.getOutcome(), TaskOutcome.SUCCESS);
            });

    assertTrue(result.getOutput().contains("Agent successfully retrieved from TeamServer"));
  }

  private static String writeContrastBuildFile() {
    return "plugins {  id('com.contrastsecurity.java') }\n"
        + "contrastConfiguration {\n"
        + "  username = "
        + "'"
        + EnvironmentUtils.getUsername()
        + "'"
        + "\n"
        + "  apiKey = "
        + "'"
        + EnvironmentUtils.getApiKey()
        + "'"
        + "\n"
        + "  serviceKey = "
        + "'"
        + EnvironmentUtils.getServiceKey()
        + "'"
        + "\n"
        + "  apiUrl = "
        + "'"
        + EnvironmentUtils.getApiUrl()
        + "'"
        + "\n"
        + "  orgUuid = "
        + "'"
        + EnvironmentUtils.getOrgUuid()
        + "'"
        + "\n"
        + "  appName = 'gradle-end-to-end-test'\n"
        + "  serverName = 'server1'\n"
        + "  appVersion = '0.0.1'\n"
        + "  attachToTests = true\n"
        + "}\n";
  }
}
