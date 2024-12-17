package com.contrastsecurity.gradle.plugin;

import static com.contrastsecurity.gradle.plugin.ContrastGradlePlugin.EXTENSION_NAME;

import com.contrastsecurity.models.AgentType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.UserAgentProduct;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Downloads the current java agent from TeamServer using Credentials provided by the
 * AgentCredentialsExtension
 */
public class InstallAgentTask extends DefaultTask {

  final ContrastConfigurationExtension config =
      (ContrastConfigurationExtension) getProject().getExtensions().getByName(EXTENSION_NAME);

  @TaskAction
  void installAgent() {

    logger.debug("Running installAgentTask");
    // create sdk object for connecting to Contrast
    final ContrastSDK sdk = connectToContrast();

    logger.debug("Connected to Contrast at: " + sdk.getRestApiURL());

    // get agent, either from configured jar path or from TS
    final Path agent = retrieveAgent(sdk, config.getJarPath(), config.getOrgUuid(), getProject());

    logger.debug("preparing to attach agent");
    attachAgentToTasks(agent.toAbsolutePath());
  }

  /**
   * Configures tasks to run with the agent attached Should be configurable via the plugin
   * configurations to determine which tasks we attach to. For now, this configuration is just a
   * boolean and only attaches to Test tasks.
   */
  private void attachAgentToTasks(final Path agentPath) {
    if (config.getAttachToTests()) {
      Collection<String> arguments =
          createContrastArgs(
              getProject().getName(),
              agentPath,
              config.getAppName(),
              config.getServerName(),
              config.getAppVersion());
      getProject()
          .getTasks()
          .withType(Test.class)
          .configureEach(
              task -> {
                task.jvmArgs(arguments);
              });

      getProject()
          .getTasks()
          .withType(Test.class)
          .forEach(s -> logger.debug(s.getAllJvmArgs().toString()));
    }
  }

  /**
   * Creates the jvmArgs for running the java agent against JavaExec tasks
   *
   * @param projectName name of the current Gradle Project
   * @param agentPath preconfigured path to an agent defined by the ContrastConfigurationExtension
   * @param appName the name of the application on TS defined by the ContrastConfigurationExtension
   * @param serverName the name of the server on TS defined by the ContrastConfigurationExtension
   * @param appVersion the app version, if not supplied by the ContrastConfigurationExtension, then
   *     it is generated in {@link InstallAgentTask#computeAppVersion(String)}
   * @return Set of arguments to be attached to specified tasks
   */
  @VisibleForTesting
  public static Collection<String> createContrastArgs(
      final String projectName,
      final Path agentPath,
      final String appName,
      final String serverName,
      String appVersion) {

    // List to preserve ordering of arguments
    final Collection<String> args = new LinkedList<>();

    args.add("-javaagent:" + agentPath.toAbsolutePath());

    // Use gradle project name in the even the appName is not set
    if ("null".equals(appName) || appName == null) {
      args.add("-Dcontrast.override.appname=" + projectName);
    } else {
      args.add("-Dcontrast.override.appname=" + appName);
    }

    args.add("-Dcontrast.server=" + serverName);

    if (appVersion == null) {
      appVersion = computeAppVersion(appName);
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
  @VisibleForTesting
  public static String computeAppVersion(final String appName) {
    final Date currentDate = new Date();
    final String travisBuildNumber = System.getenv("TRAVIS_BUILD_NUMBER");
    final String circleBuildNum = System.getenv("CIRCLE_BUILD_NUM");

    final String appVersionQualifier;
    if (travisBuildNumber != null) {
      appVersionQualifier = travisBuildNumber;
    } else if (circleBuildNum != null) {
      appVersionQualifier = circleBuildNum;
    } else {
      appVersionQualifier = new SimpleDateFormat("yyyyMMddHHmmss").format(currentDate);
    }
    return appName + "-" + appVersionQualifier;
  }

  /** Use ContrastSDK to download agent and return the path where agent jar is stored */
  @VisibleForTesting
  public static Path retrieveAgent(
      final ContrastSDK connection,
      final String jarPath,
      final String uuid,
      final Project project) {

    final Logger logger = project.getLogger();
    // Initially attempt to get agent from the previously configured location
    if (jarPath != null) {
      final Path agent = Paths.get(jarPath);
      if (!Files.exists(agent)) {
        throw new GradleException("Unable to find java agent at " + jarPath);
      }
      logger.debug("Agent provided via configuration retrieved");
      return agent;
    }

    logger.debug("No agent path provided, checking for cached agent");

    final Path agent = Paths.get(project.getProjectDir().getPath()).resolve(AGENT_NAME);
    if (Files.exists(agent)) {
      System.out.println("Agent jar found at " + project.getProjectDir().getPath());
      return agent;
    }

    logger.debug("Attempting to retrieve agent from TeamServer");
    // If no jar is provided, and no jarpath configured, attempt to retrieve the agent from TS
    final byte[] bytes;
    Path downloadedAgent;
    try {
      bytes = connection.getAgent(AgentType.JAVA, uuid);

      // Save the jar to the 'target' directory
      final Path target = Paths.get(project.getProjectDir().getPath());

      try {
        Files.createFile(target);
      } catch (FileAlreadyExistsException e) {
        logger.debug("Project dir already exists");
      }

      downloadedAgent = target.resolve(AGENT_NAME);

      Files.write(downloadedAgent, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

    } catch (RuntimeException | IOException e) {
      throw new GradleException("Failed to download java agent from the Contrast api: " + e);
    }

    logger.debug("Agent retrieved from TeamServer");
    return downloadedAgent;
  }

  /** Create ContrastSDK for connecting to TeamServer */
  private ContrastSDK connectToContrast() {
    // TODO get plugin version for this as well
    final UserAgentProduct gradle = UserAgentProduct.of("contrast-gradle-plugin");
    return new ContrastSDK.Builder(config.getUsername(), config.getServiceKey(), config.getApiKey())
        .withApiUrl(config.getApiUrl() + "/api")
        // TODO JAVA-8883 figure out how to define this proxy
        // .withProxy(proxy) //with proxy?
        .withUserAgentProduct(gradle)
        .build();
  }

  private static final String AGENT_NAME = "contrast.jar";
  final Logger logger = getProject().getLogger();
}
