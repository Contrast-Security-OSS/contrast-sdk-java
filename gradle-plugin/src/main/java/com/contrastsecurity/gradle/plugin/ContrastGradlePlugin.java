package com.contrastsecurity.gradle.plugin;

import com.contrastsecurity.gradle.plugin.extensions.ContrastConfigurationExtension;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.UserAgentProduct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.Directory;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.testing.Test;

/**
 * Gradle plugin for contrast utilities. The goals for this plugin are defined here <a
 * href=https://contrast.atlassian.net/browse/JAVA-8252>JAVA-8252</a>
 */
public class ContrastGradlePlugin implements Plugin<Project> {

  public void apply(final Project target) {

    final Logger logger = target.getLogger();

    // Target path for the agent jar once it is resolved
    final Provider<Directory> buildDirectory =
        target.getLayout().getBuildDirectory().dir("contrast");

    final String agentPath = buildDirectory.get().file("contrast.jar").getAsFile().getPath();

    final ContrastConfigurationExtension extension =
        target.getExtensions().create(EXTENSION_NAME, ContrastConfigurationExtension.class);

    // afterEvaluate to ensure extension values are populated and all custom test tasks are
    // registered before attempting to configure
    target.afterEvaluate(
        project -> {
          final ContrastSDK sdk = getSdkInstance(extension);

          final TaskProvider<ResolveAgentTask> resolveAgentTask =
              target.getTasks().register("resolveAgent", ResolveAgentTask.class, sdk);

          final Property<String> providedJar = extension.getJarPath();

          File agentFile;

          // small amount of duplicate work to avoid unnecessarily writing a file to the build dir
          // if provided jar exists
          if (providedJar.isPresent() && Files.exists(Paths.get(providedJar.get()))) {
            agentFile = new File(providedJar.get());
          } else {
            agentFile = new File(agentPath);
          }

          resolveAgentTask.configure(
              task -> {
                task.getProvidedPath().set(providedJar);
                task.getAgent().set(agentFile);
              });

          // lifecycle task for grouping verification tasks
          final TaskProvider<Task> contrastCheck = target.getTasks().register("contrastCheck");

          final Collection<String> contrastArgs =
              createContrastArgs(
                  agentFile.getPath(),
                  extension.getAppName().get(),
                  extension.getServerName().get(),
                  extension.getAppVersion().get());

          // for each test task...
          target
              .getTasks()
              .withType(Test.class)
              .forEach(
                  testTask -> {
                    testTask.dependsOn(resolveAgentTask);

                    // attach agent args
                    testTask.jvmArgs(contrastArgs);

                    // Debug log for jvm arg testing
                    logger.debug(
                        "JVM args applied for task "
                            + testTask.getName()
                            + ": "
                            + testTask.getJvmArgs());

                    // generate ContrastVerifyTestTask for each test
                    final TaskProvider<ContrastVerifyTestTask> verifyTask =
                        target
                            .getTasks()
                            .register(
                                "contrastVerifyTest" + testTask.getName(),
                                ContrastVerifyTestTask.class,
                                sdk);
                    verifyTask.configure(
                        v -> {
                          v.dependsOn(testTask);

                          // set output file for found vulnerabilities
                          v.getTraceResults()
                              .set(
                                  new File(
                                      buildDirectory
                                          .get()
                                          .file("traceResults_" + testTask.getName() + ".txt")
                                          .getAsFile()
                                          .getPath()));
                        });

                    // configure lifecycle task to depend on this test's corresponding verifyTask
                    contrastCheck.configure(task -> task.dependsOn(verifyTask));
                  });
        });
  }

  /**
   * Creates the jvmArgs for running the java agent against JavaExec tasks
   *
   * @param agentPath preconfigured path to an agent defined by the ContrastConfigurationExtension
   * @param appName the name of the application on TS defined by the ContrastConfigurationExtension
   * @param serverName the name of the server on TS defined by the ContrastConfigurationExtension
   * @param appVersion the app version, if not supplied by the ContrastConfigurationExtension, then
   *     it is generated in {@link ContrastGradlePlugin#computeAppVersion(String)}
   * @return Set of arguments to be attached to specified tasks
   */
  public static Collection<String> createContrastArgs(
      final String agentPath, final String appName, final String serverName, String appVersion) {

    final Collection<String> args = new ArrayList<>();
    args.add("-javaagent:" + agentPath);
    args.add("-Dcontrast.override.appname=" + appName);
    args.add("-Dcontrast.server=" + serverName);
    args.add("-Dcontrast.override.appversion=" + appVersion);

    return args;
  }

  /**
   * Shamelessly stolen from the maven plugin TODO check if we still want to do this based on travis
   * or circle build number
   *
   * @return computed AppVersion
   */
  public static String computeAppVersion(final String appName) {
    final long currentTime = System.currentTimeMillis();
    final String travisBuildNumber = System.getenv("TRAVIS_BUILD_NUMBER");
    final String circleBuildNum = System.getenv("CIRCLE_BUILD_NUM");

    final String appVersionQualifier;

    if (travisBuildNumber != null) {
      appVersionQualifier = travisBuildNumber;

    } else if (circleBuildNum != null) {
      appVersionQualifier = circleBuildNum;

    } else {
      appVersionQualifier = TIME_FORMATTER.format(Instant.ofEpochMilli(currentTime));
    }

    return appName + "-" + appVersionQualifier;
  }

  /**
   * Initializes the SDK for the plugin. If configuration from the {@link
   * ContrastConfigurationExtension} is missing or incorrect, provides a NOOP Instance
   */
  public ContrastSDK getSdkInstance(final ContrastConfigurationExtension extension) {
    if (!extension.getUsername().isPresent()
        || !extension.getServiceKey().isPresent()
        || !extension.getApiKey().isPresent()) {
      return NOOP;
    }
    final UserAgentProduct gradle = UserAgentProduct.of("contrast-gradle-plugin");
    return new ContrastSDK.Builder(
            extension.getUsername().get(),
            extension.getServiceKey().get(),
            extension.getApiKey().get())
        .withApiUrl(extension.getApiUrl().get())
        // TODO JAVA-8883 figure out how to define this proxy
        // .withProxy(proxy) //with proxy?
        .withUserAgentProduct(gradle)
        .build();
  }

  private static final DateTimeFormatter TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
          .withLocale(Locale.getDefault())
          .withZone(ZoneId.systemDefault());

  public static String DEFAULT_URL = "https://app.contrastsecurity.com/Contrast/api";

  public static final ContrastSDK NOOP =
      new ContrastSDK.Builder("noop", "noop", "noop").withApiUrl(DEFAULT_URL).build();

  public static final String EXTENSION_NAME = "contrastConfiguration";
}
