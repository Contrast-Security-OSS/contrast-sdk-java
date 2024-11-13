package com.contrastsecurity.maven.plugin;

/*-
 * #%L
 * Contrast Maven Plugin
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.scan.CodeArtifact;
import com.contrastsecurity.sdk.scan.Project;
import com.contrastsecurity.sdk.scan.Projects;
import com.contrastsecurity.sdk.scan.Scan;
import com.contrastsecurity.sdk.scan.ScanSummary;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Analyzes the Maven project's artifact with Contrast Scan to provide security insights
 *
 * @since 2.13.0
 */
@Mojo(
    name = "scan",
    defaultPhase = LifecyclePhase.INTEGRATION_TEST,
    requiresOnline = true,
    threadSafe = true)
public final class ContrastScanMojo extends AbstractContrastMojo {

  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject mavenProject;

  /**
   * Contrast Scan project unique ID to which the plugin runs new Scans. This will be replaced
   * imminently with a project name
   */
  @Parameter(property = "project", defaultValue = "${project.name}")
  private String projectName;

  /**
   * File path of the Java artifact to upload for scanning. By default, uses the path to this
   * module's Maven artifact produced in the {@code package} phase.
   */
  @Parameter private File artifactPath;

  /** A label to distinguish this scan from others in your project */
  @Parameter(defaultValue = "${project.version}")
  private String label;

  /**
   * When {@code true}, will wait for and retrieve scan results before completing the goal.
   * Otherwise, will start a scan then complete the goal without waiting for Contrast Scan to
   * complete.
   */
  @Parameter(defaultValue = "" + true)
  private boolean waitForResults;

  /** When {@code true}, will output a summary of the scan results to the console (build log). */
  @Parameter(defaultValue = "" + true)
  private boolean consoleOutput;

  /**
   * File path to where the scan results (in <a href="https://sarifweb.azurewebsites.net">SARIF</a>)
   * will be written at the conclusion of the scan. Note: no results are written when {@link
   * #waitForResults} is {@code false}.
   */
  @Parameter(
      defaultValue =
          "${project.build.directory}/contrast-scan-reports/contrast-scan-results.sarif.json")
  private File outputPath;

  /**
   * Maximum time (in milliseconds) to wait for a Scan to complete. Scans that exceed this threshold
   * fail this goal.
   */
  @Parameter(defaultValue = "" + 5 * 60 * 1000)
  private long timeoutMs;

  private ContrastSDK contrast;

  /** visible for testing */
  String getProjectName() {
    return projectName;
  }

  /** visible for testing */
  void setProjectName(final String projectName) {
    this.projectName = projectName;
  }

  /** visible for testing */
  boolean isConsoleOutput() {
    return consoleOutput;
  }

  /** visible for testing */
  void setConsoleOutput(final boolean consoleOutput) {
    this.consoleOutput = consoleOutput;
  }

  @Override
  public void execute() throws MojoFailureException, MojoFailureException {
    // initialize plugin
    initialize();
    final Projects projects = contrast.scan(getOrganizationId()).projects();

    // check that file exists
    final Path file = artifactPath == null ? findProjectArtifactOrFail() : artifactPath.toPath();
    if (!Files.exists(file)) {
      throw new MojoFailureException(
          file
              + " does not exist. Make sure to bind the scan goal to a phase that will execute after the artifact to scan has been built");
    }

    // get or create project
    final Project project = findOrCreateProject(projects);

    // upload code artifact
    getLog().info("Uploading " + file.getFileName() + " to Contrast Scan");
    final CodeArtifact codeArtifact;
    try {
      codeArtifact = project.codeArtifacts().upload(file);
    } catch (final IOException | HttpResponseException e) {
      throw new MojoFailureException("Failed to upload code artifact to Contrast Scan", e);
    }

    // start scan
    getLog().info("Starting scan with label " + label);
    final Scan scan;
    try {
      scan =
          project.scans().define().withLabel(label).withExistingCodeArtifact(codeArtifact).create();
    } catch (final IOException | HttpResponseException e) {
      throw new MojoFailureException("Failed to start scan for code artifact " + codeArtifact, e);
    }

    // show link in build log
    final URL clickableScanURL = createClickableScanURL(project.id(), scan.id());
    getLog().info("Scan results will be available at " + clickableScanURL.toExternalForm());

    // optionally wait for results, output summary to console, output sarif to file system
    if (waitForResults) {
      getLog().info("Waiting for scan results");
      waitForResults(scan);
    }
  }

  /**
   * Inspects the {@link #mavenProject} to find the project's artifact, or fails if no such artifact
   * can be found. We may not find an artifact when the user has configured this goal to run before
   * the artifact is created, or if the project does not produce an artifact (e.g. a module of type
   * {@code pom}).
   *
   * <p>By default, some Maven plugins will skip their work instead of failing when inputs are not
   * found. For example, the {@code maven-surefire-plugin} default behavior will skip tests if no
   * test classes are found (and this may be overridden with configuration).
   *
   * <p>So, why does this plugin fail instead of simply skipping its work and logging a warning?
   * Because we want the user to avoid one particularly problematic configuration. Users can easily
   * mis-configure this plugin in a multi-module build by including it in the parent POM's build
   * plugins. In this case, all child modules will inherit this plugin in their builds, and the
   * build will scan not just the web application modules, but also the internal dependencies that
   * are components of their applications. Contrast Scan is intended to be used on their artifact
   * that represents the web application, and users should not scan the components of their web
   * application individually. We can detect this mis-configuration by failing when there is no
   * artifact, because the build will fail on the parent POM project (since it is of type {@code
   * pom}).
   *
   * @throws MojoFailureException when artifact does not exist
   */
  private Path findProjectArtifactOrFail() throws MojoFailureException {
    final Artifact artifact = mavenProject.getArtifact();
    final File file = artifact == null ? null : artifact.getFile();
    if (file == null) {
      throw new MojoFailureException(
          "Project's artifact file has not been set - see https://contrastsecurity.dev/contrast-maven-plugin/troubleshooting/artifact-not-set.html");
    }
    return file.toPath();
  }

  /**
   * Finds a Scan project with the project name from the plugin configuration, or creates such a
   * "Java" project if one does not exist.
   *
   * <p>Note: the Scan API does not expose an endpoint for doing this atomically, so it is possible
   * that another process creates the project after having determined it to not-exist but before
   * attempting to create it.
   *
   * @param projects project resource collection
   * @return existing or new {@link Project}
   * @throws MojoFailureException when fails to make request to the Scan API
   */
  private Project findOrCreateProject(Projects projects) throws MojoFailureException {
    final Optional<Project> optional;
    try {
      optional = projects.findByName(projectName);
    } catch (final IOException e) {
      throw new MojoFailureException("Failed to retrieve project " + projectName, e);
    } catch (final UnauthorizedException e) {
      throw new MojoFailureException(
          "Authentication failure while retrieving project "
              + projectName
              + " - verify Contrast connection configuration",
          e);
    }
    if (optional.isPresent()) {
      getLog().debug("Found project with name " + projectName);
      final Project project = optional.get();
      if (project.archived()) {
        // TODO the behavior of tools like this plugin has yet to be defined with respect to
        // archived projects; however, the UI exposes no way to archive projects at the moment.
        // For now, simply log a warning to help debug this in the future should we encounter this
        // case
        getLog().warn("Project " + projectName + " is archived");
      }
      return project;
    }

    getLog().debug("No project exists with name " + projectName + " - creating one");
    try {
      return projects.define().withName(projectName).withLanguage("JAVA").create();
    } catch (final IOException | HttpResponseException e) {
      throw new MojoFailureException("Failed to create project " + projectName, e);
    }
  }

  /**
   * visible for testing
   *
   * @return Contrast browser application URL for users to click-through and see their scan results
   * @throws MojoFailureException when the URL is malformed
   */
  URL createClickableScanURL(final String projectId, final String scanId)
      throws MojoFailureException {
    final String path =
        String.join(
            "/",
            "",
            "Contrast",
            "static",
            "ng",
            "index.html#",
            getOrganizationId(),
            "scans",
            projectId,
            "scans",
            scanId);
    try {
      final URL url = new URL(getURL());
      return new URL(url.getProtocol(), url.getHost(), url.getPort(), path);
    } catch (final MalformedURLException e) {
      throw new MojoFailureException(
          "Error building clickable Scan URL. Please contact support@contrastsecurity.com for help",
          e);
    }
  }

  /**
   * Waits for the scan to complete, writes results in SARIF to the file system, and optionally
   * displays a summary of the results in the console. Translates all errors that could occur while
   * retrieving results to one of {@code MojoFailureException} or {@code MojoFailureException}.
   *
   * @param scan the scan to wait for and retrieve the results of
   * @throws MojoFailureException when fails to retrieve scan results for unexpected reasons
   * @throws MojoFailureException when the wait for scan results operation times out
   */
  private void waitForResults(final Scan scan) throws MojoFailureException, MojoFailureException {
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    try {
      final Path outputFile = outputPath.toPath();
      final Path reportsDirectory = outputFile.getParent();
      try {
        Files.createDirectories(reportsDirectory);
      } catch (final IOException e) {
        throw new MojoFailureException("Failed to create Contrast Scan reports directory", e);
      }

      final CompletionStage<Scan> await = scan.await(scheduler);
      final CompletionStage<Void> save =
          await.thenCompose(
              completed ->
                  CompletableFuture.runAsync(
                      () -> {
                        try {
                          completed.saveSarif(outputFile);
                        } catch (final IOException e) {
                          throw new UncheckedIOException(e);
                        }
                      },
                      scheduler));
      final CompletionStage<Void> output =
          await.thenAccept(
              completed -> {
                final ScanSummary summary;
                try {
                  summary = completed.summary();
                } catch (final IOException e) {
                  throw new UncheckedIOException(e);
                }
                writeSummaryToConsole(summary, line -> getLog().info(line));
              });
      CompletableFuture.allOf(save.toCompletableFuture(), output.toCompletableFuture())
          .get(timeoutMs, TimeUnit.MILLISECONDS);
    } catch (final ExecutionException e) {
      // try to unwrap the extraneous ExecutionException
      final Throwable cause = e.getCause();
      // ExecutionException should always have a cause, but its constructor does not enforce this,
      // so check if the cause is null
      final Throwable inner = cause == null ? e : cause;
      throw new MojoFailureException("Failed to retrieve Contrast Scan results", inner);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new MojoFailureException("Interrupted while retrieving Contrast Scan results", e);
    } catch (final TimeoutException e) {
      final Duration duration = Duration.ofMillis(timeoutMs);
      final String durationString =
          duration.toMinutes() > 0
              ? duration.toMinutes() + " minutes"
              : (duration.toMillis() / 1000) + " seconds";
      throw new MojoFailureException(
          "Failed to retrieve Contrast Scan results in " + durationString, e);
    } finally {
      scheduler.shutdown();
    }
  }

  /**
   * visible for testing
   *
   * @param summary the scan summary to write to console
   * @param consoleLogger describes a console logger where each accepted string is printed to a new
   *     line
   */
  void writeSummaryToConsole(final ScanSummary summary, final Consumer<String> consoleLogger) {
    consoleLogger.accept("Scan completed");
    if (consoleOutput) {
      consoleLogger.accept("New Results\t" + summary.totalNewResults());
      consoleLogger.accept("Fixed Results\t" + summary.totalFixedResults());
      consoleLogger.accept("Total Results\t" + summary.totalResults());
    }
  }

  /**
   * Must be called after Maven has completed field injection.
   *
   * <p>I don't believe Maven has a post-injection callback that we bind to this method, so the
   * {@link #execute()} method calls this before continuing.
   *
   * <p>This is useful for tests to initialize the {@link ContrastSDK} without running the whole
   * {@link #execute()} method
   *
   * @throws IllegalStateException when has already been initialized
   * @throws MojoFailureException when cannot connect to Contrast
   */
  private synchronized void initialize() throws MojoFailureException {
    if (contrast != null) {
      throw new IllegalStateException("Already initialized");
    }
    contrast = connectToContrast();
  }
}
