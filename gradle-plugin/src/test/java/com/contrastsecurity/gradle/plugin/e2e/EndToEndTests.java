package com.contrastsecurity.gradle.plugin.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.contrastsecurity.gradle.plugin.EnvironmentUtils;
import com.contrastsecurity.gradle.plugin.GradleRunnerTest;
import com.contrastsecurity.gradle.plugin.InstallAgentTask;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.UserAgentProduct;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

/** End-To-End tests for the gradle plugin for interacting with TeamServer */
public class EndToEndTests extends GradleRunnerTest {

  @Test
  void verify_retrieval_of_agent_from_teamserver() {

    final ContrastSDK connection =
        new ContrastSDK.Builder(
                EnvironmentUtils.getUsername(),
                EnvironmentUtils.getServiceKey(),
                EnvironmentUtils.getApiKey())
            .withApiUrl(EnvironmentUtils.getApiUrl())
            .withUserAgentProduct(UserAgentProduct.of("contrast-gradle-plugin"))
            .build();
    final Path agentPath =
        InstallAgentTask.retrieveAgent(
            connection, null, EnvironmentUtils.getOrgUuid(), ProjectBuilder.builder().build());
    assertNotNull(agentPath);
    assertTrue(agentPath.endsWith("contrast.jar"));
  }

  @Test
  void verify_attaches_agent_to_tests() throws IOException {
    writeString(getSettingsFile(), "");
    String config =
        writeContrastBuildFile()
            + "tasks.register('fakeTask', org.gradle.api.tasks.testing.Test) { \nSystem.out.println('test') \n}";
    writeString(getBuildFile(), config);

    final GradleRunner testRunner = GradleRunner.create();
    testRunner.forwardOutput();
    testRunner.withPluginClasspath();
    // outputs debug logs to stdout for testing
    testRunner.withArguments("installAgent", "--debug");
    testRunner.withDebug(true);
    testRunner.withProjectDir(projectDir);
    final BuildResult result = testRunner.build();

    result
        .getTasks()
        .forEach(
            buildTask -> {
              assertEquals(buildTask.getOutcome(), TaskOutcome.SUCCESS);
            });

    for (final String arg : AGENT_ARGS) {
      assertTrue(result.getOutput().contains(arg));
    }
  }

  @Test
  void verify_proxy_settings_retrieved() throws IOException {
    writeString(getSettingsFile(), "");
    String config = writeContrastBuildFile();
    writeString(getBuildFile(), config);

    final GradleRunner testRunner = GradleRunner.create();
    testRunner.forwardOutput();
    testRunner.withPluginClasspath();
    // outputs debug logs to stdout for testing
    testRunner.withArguments(
        "installAgent",
        "--debug",
        "-Dhttps.proxyHost=localHost",
        "-Dhttp.proxyPort=1234",
        "-Dhttps.proxyUser=user",
        "-Dhttps.proxyPassword=password");
    testRunner.withDebug(true);
    testRunner.withProjectDir(projectDir);
    final BuildResult result = testRunner.build();

    result
        .getTasks()
        .forEach(
            buildTask -> {
              assertEquals(buildTask.getOutcome(), TaskOutcome.SUCCESS);
            });

    assertTrue(result.getOutput().contains("Using proxy for host localhost and port 1234"));
    assertTrue(result.getOutput().contains("Proxy authentication set"));
  }

  private String writeContrastBuildFile() {
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

  private static final Collection<String> AGENT_ARGS = new HashSet<>();

  static {
    AGENT_ARGS.add("-javaagent:");
    AGENT_ARGS.add("-Dcontrast.override.appname=gradle-end-to-end-test");
    AGENT_ARGS.add("-Dcontrast.server=server");
    AGENT_ARGS.add("-Dcontrast.override.appversion=0.0.1");
  }
}
