package com.contrastsecurity.gradle.plugin;

import static com.contrastsecurity.gradle.plugin.ContrastGradlePlugin.EXTENSION_NAME;

import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.models.AgentType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.UserAgentProduct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;

/**
 * Downloads the current java agent from TeamServer using Credentials provided by the
 * AgentCredentialsExtension
 */
public class InstallAgentTask extends DefaultTask {

  final ContrastConfigurationExtension config =
      (ContrastConfigurationExtension) getProject().getExtensions().getByName(EXTENSION_NAME);

  @TaskAction
  void installAgent() {

    // create sdk object for connecting to Contrast
    final ContrastSDK sdk = connectToContrast();

    // get agent, either from configured jar path or from TS
    final Path agent = retrieveAgent(sdk);

    attachAgentToTasks(agent.toAbsolutePath());
  }

  /** Configures JavaExec tasks to run with the agent attached */
  private void attachAgentToTasks(final Path agentPath) {
    getProject()
        .getTasks()
        .withType(JavaExec.class)
        .configureEach(
            task -> {
              task.jvmArgs(createContrastArgs(agentPath));
            });
  }

  /**
   * Creates the jvmArgs for running the java agent against JavaExec tasks
   *
   * @param agentPath preconfigured path to an agent defined by the ContrastConfigurationExtension
   * @return Set of arguments
   */
  private Collection<String> createContrastArgs(final Path agentPath) {
    final Collection<String> args = Collections.emptySet();
    args.add("-javaagent:" + agentPath.toAbsolutePath());
    args.add("-Dcontrast.override.appname=" + config.appName);
    args.add("-Dcontrast.server=" + config.serverName);
    args.add("-Dcontrast.env=qa");

    String appVersion = config.getAppVersion();
    if (appVersion == null) {
      appVersion = computeAppVersion();
    }

    args.add("-Dcontrast.override.appversion=" + appVersion);

    return args;
  }

  /**
   * Shamelessly stolen from the maven plugin TODO check if we still want to do this based on travis
   * or circle build number
   *
   * @return computed AppVersion
   */
  private String computeAppVersion() {
    final Date currentDate = new Date();
    String travisBuildNumber = System.getenv("TRAVIS_BUILD_NUMBER");
    String circleBuildNum = System.getenv("CIRCLE_BUILD_NUM");

    final String appVersionQualifier;
    if (travisBuildNumber != null) {
      appVersionQualifier = travisBuildNumber;
    } else if (circleBuildNum != null) {
      appVersionQualifier = circleBuildNum;
    } else {
      appVersionQualifier = new SimpleDateFormat("yyyyMMddHHmmss").format(currentDate);
    }
    return config.getAppName() + "-" + appVersionQualifier;
  }

  /** Use ContrastSDK to download agent and return the path where agent jar is stored */
  private Path retrieveAgent(final ContrastSDK connection) {
    // Initially attempt to run agent from the previously configured location
    final String jarPath = config.getJarPath();
    if (jarPath != null) {
      final Path agent = Paths.get(jarPath);
      if (!Files.exists(agent)) {
        throw new RuntimeException("Unable to find java agent at " + jarPath);
      }
      return agent;
    }

    // If no jar is provided, and no jarpath configured, attempt to retrieve the agent from TS
    final byte[] bytes;
    try {
      bytes = connection.getAgent(AgentType.JAVA, config.getOrgUuid());
    } catch (IOException e) {
      throw new RuntimeException("Failed to retrieve Contrast Java Agent: " + e);
    } catch (UnauthorizedException e) {
      throw new RuntimeException(
          "\nWe contacted Contrast successfully but couldn't authorize with the credentials you provided. The error is:",
          e);
    }

    // Save the jar to the 'target' directory
    final Path target = Paths.get(getProject().getProjectDir().getPath());
    try {
      FileUtils.forceMkdir(target.toFile());
    } catch (final IOException e) {
      throw new RuntimeException("Unable to create directory " + target, e);
    }

    final Path agent = target.resolve(AGENT_NAME);
    try {
      Files.write(agent, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    } catch (final IOException e) {
      throw new RuntimeException("Unable to save the latest java agent.", e);
    }
    return agent;
  }

  /** Use ContrastSDK to download agent creds for running the agent */
  private void downloadAgentCredentials(final ContrastSDK connection) {}

  /** Create ContrastSDK for connecting to TeamServer */
  private ContrastSDK connectToContrast() {
    // TODO get plugin version for this as well
    final UserAgentProduct gradle = UserAgentProduct.of("contrast-gradle-plugin");
    return new ContrastSDK.Builder(config.getUsername(), config.getServiceKey(), config.getApiKey())
        .withApiUrl(config.getApiUrl())
        // TODO figure out how to define this proxy
        // .withProxy(proxy) //with proxy?
        .withUserAgentProduct(gradle)
        .build();
  }

  private static final String AGENT_NAME = "contrast.jar";
}
