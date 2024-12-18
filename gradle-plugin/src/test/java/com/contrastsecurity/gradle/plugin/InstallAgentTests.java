package com.contrastsecurity.gradle.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.contrastsecurity.sdk.ContrastSDK;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

/** Unit tests for verifying logic in {@link InstallAgentTask} works properly */
public class InstallAgentTests {

  @Test
  void verify_correct_contrast_args() {
    final Collection<String> expectedArgs =
        Set.of(
            "-javaagent:/test/path",
            "-Dcontrast.override.appname=foo",
            "-Dcontrast.server=bar",
            "-Dcontrast.override.appversion=0.0.1");
    final Collection<String> actualArgs =
        InstallAgentTask.createContrastArgs("name", Path.of("/test/path"), "foo", "bar", "0.0.1");
    assertTrue(actualArgs.containsAll(expectedArgs));
  }

  @Test
  void verify_agent_retrieval_given_real_path() throws IOException {
    final Path tempAgent = Files.createTempFile("agent", "jar");
    final ContrastSDK connection = new ContrastSDK.Builder("user", "serviceKey", "apiKey").build();
    final Path agentPath =
        InstallAgentTask.retrieveAgent(
            connection, tempAgent.toString(), "uuid", ProjectBuilder.builder().build());
    assertEquals(tempAgent.toString(), agentPath.toString());
  }

  @Test
  void verify_no_proxy_set() {
    final Project project = ProjectBuilder.builder().build();
    final Proxy noProxy = InstallAgentTask.getProxy(project.getLogger(), null, null, null, null);
    assertEquals(noProxy, Proxy.NO_PROXY);
  }

  @Test
  void verify_proxy_correctly_configured_no_auth() {
    final Project project = ProjectBuilder.builder().build();

    final Proxy proxy =
        InstallAgentTask.getProxy(project.getLogger(), "localhost", 1234, null, null);
    assertEquals(proxy.address(), new InetSocketAddress("localhost", 1234));
  }
}
