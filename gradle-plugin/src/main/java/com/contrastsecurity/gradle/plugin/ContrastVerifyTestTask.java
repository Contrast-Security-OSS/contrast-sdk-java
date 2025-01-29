package com.contrastsecurity.gradle.plugin;

import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.gradle.plugin.extensions.ContrastConfigurationExtension;
import com.contrastsecurity.http.RuleSeverity;
import com.contrastsecurity.http.ServerFilterForm;
import com.contrastsecurity.models.Application;
import com.contrastsecurity.models.Applications;
import com.contrastsecurity.models.Servers;
import com.contrastsecurity.models.Trace;
import com.contrastsecurity.models.TraceFilterBody;
import com.contrastsecurity.models.Traces;
import com.contrastsecurity.sdk.ContrastSDK;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

/**
 * Task for verifying test results for its test. There is one of these tasks created for each Test
 * task in the project.
 *
 * <p>This task retrieves TS results for it's corresponding Test task, and if any vulnerabilities
 * are found which match or exceed the configured minimum severity level, this task fails the build.
 */
@CacheableTask
public abstract class ContrastVerifyTestTask extends DefaultTask {

  final ContrastSDK sdk;

  @Inject
  public ContrastVerifyTestTask(ContrastSDK sdk) {
    super();
    this.sdk = sdk;
  }

  /** The trace results retrieved from TeamServer */
  @OutputFile
  public abstract RegularFileProperty getTraceResults();

  @TaskAction
  public void verifyFindings() {
    final Logger logger = getProject().getLogger();

    final ContrastConfigurationExtension config =
        getProject().getExtensions().getByType(ContrastConfigurationExtension.class);

    if (!config.getOrgUuid().isPresent()) {
      throw new GradleException("Organization UUID not set");
    }

    final String orgUuid = config.getOrgUuid().get();
    final String appId = getApplicationId(config, orgUuid);
    final String serverName = config.getServerName().get();

    final List<String> serverIds = getServerId(sdk, appId, orgUuid, serverName);

    final TraceFilterBody traceFilter =
        getTraceFilterBody(serverIds, config.getMinSeverity().get(), config.getAppVersion().get());

    Traces traces;
    try {
      traces = sdk.getTraces(orgUuid, appId, traceFilter);
    } catch (IOException e) {
      throw new GradleException("Failed to retrieve traces from TeamServer", e);
    }

    if (traces != null && traces.getCount() > 0) {
      logger.info(traces.getCount() + " new vulnerability(s) were found.");

      // Write TeamServer results to a file in the build directory
      try (BufferedWriter writer =
          new BufferedWriter(new FileWriter(getTraceResults().get().getAsFile(), true))) {
        for (Trace trace : traces.getTraces()) {
          final String traceReport = generateTraceReport(trace);
          writer.write(traceReport);
          writer.newLine();
          logger.lifecycle(traceReport);
        }
      } catch (final IOException e) {
        logger.error("Could not write Contrast Findings to build directory", e);
      }

      throw new GradleException(
          "Your application is vulnerable. Please see the above report for new vulnerabilities.");
    } else {
      logger.lifecycle("No new vulnerabilities were found.");
    }

    logger.lifecycle("Finished verifying your application.");
  }

  /**
   * Retrieves the server id by server name
   *
   * @param sdk Contrast SDK object
   * @param applicationId application id to filter on
   * @return List<Long> id of the servers
   */
  private List<String> getServerId(
      final ContrastSDK sdk,
      final String applicationId,
      final String orgUuid,
      final String serverName) {
    ServerFilterForm serverFilterForm = new ServerFilterForm();
    serverFilterForm.setApplicationIds(Collections.singletonList(applicationId));

    Servers servers;
    List<String> serverIds;

    try {
      serverFilterForm.setQ(URLEncoder.encode(serverName, "UTF-8"));
      servers = sdk.getServersWithFilter(orgUuid, serverFilterForm);
    } catch (IOException e) {
      throw new GradleException("Unable to retrieve the servers.", e);
    } catch (UnauthorizedException e) {
      throw new GradleException("Unable to connect to Contrast.", e);
    }

    if (!servers.getServers().isEmpty()) {
      serverIds = new ArrayList<>();
      servers.getServers().forEach(server -> serverIds.add(String.valueOf(server.getServerId())));
    } else {
      throw new GradleException(
          "\n\nServer with name '"
              + serverName
              + "' not found. Make sure this server name appears in Contrast under the 'Servers' tab.\n");
    }

    return serverIds;
  }

  /** Retrieves appId from TeamServer using the OrgUuid and Appname */
  private String getApplicationId(
      final ContrastConfigurationExtension config, final String orgUuid) {
    Applications applications;

    if (!config.getAppName().isPresent()) {
      throw new GradleException("AppName not set");
    }
    try {
      applications = sdk.getApplications(orgUuid);
    } catch (IOException e) {
      throw new GradleException("Unable to retrieve the applications.", e);
    } catch (UnauthorizedException e) {
      throw new GradleException("Unable to connect to TeamServer", e);
    }

    final String appName = config.getAppName().get();
    for (Application application : applications.getApplications()) {
      if (Objects.equals(appName, application.getName())) {
        return application.getId();
      }
    }

    throw new GradleException("Application with name '" + appName + "' not found.");
  }

  private TraceFilterBody getTraceFilterBody(
      final List<String> serverIds, final String minSeverity, final String computedAppVersion) {
    TraceFilterBody body = new TraceFilterBody();
    body.setSeverities(getSeverityList(minSeverity));
    body.setAppVersionTags(Collections.singletonList(computedAppVersion));
    if (serverIds != null) {
      body.setServers(serverIds);
    }
    return body;
  }

  /**
   * Creates a basic report for a Trace object
   *
   * @param trace Trace object
   * @return String report
   */
  private String generateTraceReport(final Trace trace) {
    return "Trace: "
        + trace.getTitle().replace("{{#unlicensed}}", "(").replace("{{/unlicensed}}", ")")
        + "\nTrace Uuid: "
        + trace.getUuid()
        + "\nTrace Severity: "
        + trace.getSeverity()
        + "\nTrace Likelihood: "
        + trace.getLikelihood()
        + "\n";
  }

  /**
   * Returns the sublist of severities greater than or equal to the configured severity level
   *
   * @param severity include severity to filter with severity list with
   * @return list of severity strings
   */
  public static List<RuleSeverity> getSeverityList(final String severity) {
    final EnumSet<RuleSeverity> ruleSeverities =
        EnumSet.range(RuleSeverity.valueOf(severity.toUpperCase()), RuleSeverity.CRITICAL);
    return new ArrayList<>(ruleSeverities);
  }
}
