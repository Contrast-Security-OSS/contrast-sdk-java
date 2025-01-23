package com.contrastsecurity.gradle.plugin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Smoke tests for proper plugin registration */
class GradlePluginTests {

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
}
