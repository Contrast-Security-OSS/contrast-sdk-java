package com.contrastsecurity.gradle.plugin.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.contrastsecurity.gradle.plugin.GradleRunnerTest;
import com.contrastsecurity.gradle.plugin.util.ConfigurationExtensionValues;
import com.contrastsecurity.gradle.plugin.util.EnvironmentUtils;
import java.io.IOException;
import java.nio.file.Files;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

/** End-To-End tests for the gradle plugin for interacting with TeamServer */
public class EndToEndTests extends GradleRunnerTest {

  @Test
  void verify_plugin_retrieves_agent_from_TS() throws IOException {
    Files.writeString(getSettingsFile().toPath(), "");
    Files.writeString(getBuildFile().toPath(), config.buildContrastBuildFile());

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

  private static final ConfigurationExtensionValues config =
      new ConfigurationExtensionValues.Builder()
          .setUsername(EnvironmentUtils.getUsername())
          .setApiUrl(EnvironmentUtils.getApiUrl())
          .setApiKey(EnvironmentUtils.getApiKey())
          .setServiceKey(EnvironmentUtils.getServiceKey())
          .setOrgUuid(EnvironmentUtils.getOrgUuid())
          .setAppVersion("0.0.1")
          .setAppName("gradle-end-to-end-test")
          .setServerName("server1")
          .setMinSeverity("Medium")
          .build();
}
