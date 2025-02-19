package com.contrastsecurity.gradle.plugin;

import static java.util.Objects.requireNonNull;

import com.contrastsecurity.gradle.plugin.extensions.ContrastConfigurationExtension;
import com.contrastsecurity.models.AgentType;
import com.contrastsecurity.sdk.ContrastSDK;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

/**
 * Task for setting the agent in the project. This task takes in a user provided agent from the
 * {@link ContrastConfigurationExtension} and writes it to a file in the project's build directory.
 * If no agent is provided, then this task downloads an agent with the {@link ContrastSDK} and puts
 * it in the projects build directory.
 */
public abstract class ResolveAgentTask extends DefaultTask {

  private final ContrastSDK sdk;

  @Inject
  public ResolveAgentTask(final ContrastSDK sdk) {
    super();
    requireNonNull(sdk);
    this.sdk = sdk;
  }

  /** The agent path provided via the {@link ContrastConfigurationExtension} */
  @Internal
  public abstract Property<String> getProvidedPath();

  /** The agent returned by the task */
  @OutputFile
  public abstract RegularFileProperty getAgent();

  @TaskAction
  public void resolveAgent() {

    final Logger logger = getProject().getLogger();

    // Check if an agent was provided, and if so write that to the expected location
    if (getProvidedPath().isPresent()) {
      final Path providedPath = Paths.get(getProvidedPath().get());
      if (!Files.exists(providedPath)) {
        logger.debug("Unable to find java agentPath at " + providedPath);

      } else {
        logger.debug("Agent provided via configuration retrieved");
        return;
      }
    }

    final ContrastConfigurationExtension extension =
        getProject().getExtensions().getByType(ContrastConfigurationExtension.class);
    requireNonNull(extension);

    final Path agentBuildPath = getAgent().getAsFile().get().toPath();

    logger.debug("Attempting to retrieve agent from TeamServer");

    // If no jar is provided, attempt to retrieve the agentPath from TS
    try {
      final byte[] bytes = sdk.getAgent(AgentType.JAVA, extension.getOrgUuid().get());

      // Write the agent to the expected location
      writeToAgent(bytes, agentBuildPath);

      logger.debug("Agent successfully retrieved from TeamServer");

    } catch (final RuntimeException | IOException e) {
      throw new GradleException("Failed to download java agent from the Contrast api: " + e);
    }
  }

  /**
   * Utility for writing the agent to the expected output file
   *
   * @param bytes to write to file
   */
  private static void writeToAgent(final byte[] bytes, final Path jarPath) throws IOException {
    Files.write(jarPath, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
  }
}
