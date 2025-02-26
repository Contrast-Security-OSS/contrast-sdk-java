package com.contrastsecurity.gradle.plugin;

import static org.junit.jupiter.api.Assertions.*;

import com.contrastsecurity.gradle.plugin.extensions.ContrastConfigurationExtension;
import com.contrastsecurity.gradle.plugin.util.ConfigurationExtensionValues;
import com.contrastsecurity.http.RuleSeverity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Smoke tests for proper plugin registration */
class ConfigurationTests {

  @Test
  void verifyJvmArgsCreated() {

    final String path = "/path/to/agent";
    final String appName = "appName";
    final String serverName = "serverName";
    final String appVersion = "1.0";

    final String contrastArgs =
        String.join(
            " ", ContrastGradlePlugin.createContrastArgs(path, appName, serverName, appVersion));
    final String expectedArgLine =
        "-javaagent:"
            + path
            + " -Dcontrast.override.appname="
            + appName
            + " -Dcontrast.server="
            + serverName
            + " -Dcontrast.override.appversion="
            + appVersion;

    assertEquals(expectedArgLine, contrastArgs);
  }

  /**
   * Test verifying the {@link ContrastConfigurationExtension} convention fields are correctly
   * configured
   */
  @ParameterizedTest
  @MethodSource("extensionValues")
  void contrastConfigurationExtensionDefaultTests(
      final ConfigurationExtensionValues extensionValues) {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("com.contrastsecurity.contrastplugin");

    project
        .getExtensions()
        .configure(
            ContrastConfigurationExtension.class,
            extension -> {
              extension.getUsername().set(extensionValues.getUsername());
              extension.getApiKey().set(extensionValues.getApiKey());
              extension.getServiceKey().set(extensionValues.getServiceKey());
              extension.getApiUrl().set(extensionValues.getApiUrl());
              extension.getOrgUuid().set(extensionValues.getOrgUuid());
              extension.getAppName().set(extensionValues.getAppName());
              extension.getServerName().set(extensionValues.getServerName());
              extension.getJarPath().set(extensionValues.getJarPath());
              extension.getAppVersion().set(extensionValues.getAppVersion());
              extension.getMinSeverity().set(extensionValues.getMinSeverity());
              extension.getAttachToTests().set(Boolean.valueOf(extensionValues.getAttachToTests()));
            });

    final ContrastConfigurationExtension result =
        project.getExtensions().getByType(ContrastConfigurationExtension.class);
    assertNotNull(result);

    assertTrue(result.getApiUrl().isPresent());
    final String expectedUrl =
        Optional.ofNullable(extensionValues.getApiUrl())
            .orElseGet(() -> ContrastGradlePlugin.DEFAULT_URL);

    assertEquals(result.getApiUrl().get(), expectedUrl);

    assertTrue(result.getAppName().isPresent());
    final String expectedName =
        Optional.ofNullable(extensionValues.getAppName()).orElseGet(project::getName);

    assertEquals(result.getAppName().get(), expectedName);

    assertTrue(result.getAppVersion().isPresent());

    final String expectedVersion =
        Optional.ofNullable(extensionValues.getAppVersion())
            .orElseGet(() -> ContrastGradlePlugin.computeAppVersion(expectedName));
    assertEquals(result.getAppVersion().get(), expectedVersion);

    assertTrue(result.getMinSeverity().isPresent());
    final String expectedMinSeverity =
        Optional.ofNullable(extensionValues.getMinSeverity()).orElseGet(() -> "Medium");
    assertEquals(result.getMinSeverity().get(), expectedMinSeverity);
  }

  private static Stream<Arguments> extensionValues() {
    final ConfigurationExtensionValues.Builder base =
        new ConfigurationExtensionValues.Builder()
            .setUsername("user")
            .setApiKey("apiKey1")
            .setServiceKey("serviceKey1")
            .setOrgUuid("uuid");

    return Stream.of(
        Arguments.of(base.build()),
        Arguments.of(
            base.setAppName("fooApp")
                .setApiUrl("localhost")
                .setAppVersion("1.1.1")
                .setMinSeverity("High")
                .build()),
        Arguments.of(new ConfigurationExtensionValues.Builder().build()));
  }

  @Test
  void verifyGetRuleSeverity() {
    final List<RuleSeverity> list = ContrastVerifyTestTask.getSeverityList("Medium");
    assertEquals(List.of(RuleSeverity.MEDIUM, RuleSeverity.HIGH, RuleSeverity.CRITICAL), list);
  }
}
