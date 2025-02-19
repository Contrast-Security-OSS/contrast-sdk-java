package com.contrastsecurity.gradle.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.contrastsecurity.gradle.plugin.util.ConfigurationExtensionValues;
import com.contrastsecurity.gradle.plugin.util.TestHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests with dummy ContrastAPI data. These tests allow us to run the lifecycle of the plugin
 * without relying on real connections to TS.
 */
public class GradlePluginTests extends GradleRunnerTest {

  private HttpServer server;
  static final int port = 8080;

  @BeforeEach
  void setUp() throws IOException {
    server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext(RETRIEVE_AGENT_URL, new TestHandler("json".getBytes()));
    server.createContext(APPLICATIONS_URL, new TestHandler(APPLICATIONS_RESPONSE));
    server.createContext(SERVERS_URL, new TestHandler(SERVERS_RESPONSE));
    server.setExecutor(Executors.newFixedThreadPool(1));
    server.start();
  }

  @AfterEach
  void tearDown() {
    server.stop(0);
  }

  @Test
  void verify_plugin_lifecycle_and_fail_build() throws IOException {
    server.createContext(TRACES_URL, new TestHandler(TRACES_RESPONSE));

    Files.writeString(getSettingsFile().toPath(), "");
    final String buildFile =
        config.buildContrastBuildFile()
            + "\n"
            + "tasks.register(\"custom\", org.gradle.api.tasks.testing.Test){ \n"
            + " System.out.println(\"Hello World!\")"
            + "}";

    Files.writeString(getBuildFile().toPath(), buildFile);

    final GradleRunner testRunner = GradleRunner.create();
    testRunner.forwardOutput();
    testRunner.withPluginClasspath();
    // run with debug args to log statements we can check for in the output
    testRunner.withArguments("contrastCheck", "--debug");
    testRunner.withDebug(true);
    testRunner.withProjectDir(projectDir);
    final BuildResult result = testRunner.buildAndFail();

    assertTrue(result.getOutput().contains("Agent successfully retrieved from TeamServer"));
    assertTrue(result.getOutput().contains("-javaagent"));

    // create verify task for test
    assertTrue(
        result.getTasks().stream()
            .anyMatch(
                buildTask -> buildTask.getPath().equalsIgnoreCase(":contrastVerifyTestcustom")));

    assertTrue(
        result
            .getOutput()
            .contains(
                "Your application is vulnerable. Please see the above report for new vulnerabilities."));

    final Path verifyOutputPath = Paths.get(projectDir.getPath() + VERIFY_TASK_OUTPUT_FILE);
    assertTrue(Files.exists(verifyOutputPath));

    final String verifyContents = Files.readString(verifyOutputPath);

    assertEquals(verifyContents, Files.readString(Paths.get(EXPECTED_TRACES_FILE)));
  }

  @Test
  void verify_plugin_finds_no_vulns() throws IOException {
    server.createContext(TRACES_URL, new TestHandler(NO_TRACES_RESPONSE));
    Files.writeString(getSettingsFile().toPath(), "");
    final String buildFile =
        config.buildContrastBuildFile()
            + "\n"
            + "tasks.register(\"custom\", org.gradle.api.tasks.testing.Test){ \n"
            + " System.out.println(\"Hello World!\")"
            + "}";

    Files.writeString(getBuildFile().toPath(), buildFile);

    final GradleRunner testRunner = GradleRunner.create();
    testRunner.forwardOutput();
    testRunner.withPluginClasspath();
    // run with debug args to log statements we can check for in the output
    testRunner.withArguments("contrastCheck", "--debug");
    testRunner.withDebug(true);
    testRunner.withProjectDir(projectDir);
    final BuildResult result = testRunner.build();

    assertTrue(result.getOutput().contains("Agent successfully retrieved from TeamServer"));
    assertTrue(result.getOutput().contains("-javaagent"));

    // create verify task for test
    assertTrue(
        result.getTasks().stream()
            .anyMatch(
                buildTask -> buildTask.getPath().equalsIgnoreCase(":contrastVerifyTestcustom")));

    assertTrue(result.getOutput().contains("No new vulnerabilities were found."));
  }

  /*
   All path variables have been filled for the expected user credentials. All instances of 'orgId' and 'appId' are added
   by the SDK from the ContrastConfigurationExtension.
  */
  private static final String RETRIEVE_AGENT_URL = "/ng/orgId/agents/default/java";
  private static final String APPLICATIONS_URL = "/ng/orgId/applications";
  private static final String SERVERS_URL = "/ng/orgId/servers/filter";
  private static final String TRACES_URL = "/ng/orgId/traces/appId/filter";

  // Dummy TS data with expected data from config.
  private static final byte[] APPLICATIONS_RESPONSE;
  private static final byte[] SERVERS_RESPONSE;
  private static final byte[] TRACES_RESPONSE;
  private static final byte[] NO_TRACES_RESPONSE;

  static {
    try {
      APPLICATIONS_RESPONSE =
          Files.readAllBytes(
              Paths.get("src/test/resources/api-responses/applicationResponse.json"));
      SERVERS_RESPONSE =
          Files.readAllBytes(Paths.get("src/test/resources/api-responses/serversResponse.json"));
      TRACES_RESPONSE =
          Files.readAllBytes(Paths.get("src/test/resources/api-responses/tracesResponse.json"));
      NO_TRACES_RESPONSE =
          Files.readAllBytes(Paths.get("src/test/resources/api-responses/noTracesResponse.json"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static final String EXPECTED_TRACES_FILE = "src/test/resources/expectedOutput.txt";
  static final String VERIFY_TASK_OUTPUT_FILE = "/build/contrast/traceResults_custom.txt";

  private static final ConfigurationExtensionValues config =
      new ConfigurationExtensionValues.Builder()
          .setUsername("testUser")
          .setApiUrl("http://localhost:" + port)
          .setApiKey("apiKey")
          .setServiceKey("serviceKey")
          .setOrgUuid("orgId")
          .setAppVersion("1.0.0")
          .setAppName("testAppName")
          .setServerName("serverTestName")
          .setMinSeverity("Medium")
          .build();
}
