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

import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.http.RuleSeverity;
import com.contrastsecurity.http.ServerFilterForm;
import com.contrastsecurity.http.TraceFilterForm;
import com.contrastsecurity.models.Application;
import com.contrastsecurity.models.Applications;
import com.contrastsecurity.models.Server;
import com.contrastsecurity.models.Servers;
import com.contrastsecurity.models.Trace;
import com.contrastsecurity.models.Traces;
import com.contrastsecurity.sdk.ContrastSDK;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Verifies that none of the vulnerabilities found by Contrast Assess during integration testing
 * violate the project's security policy (fails the build when violations are detected).
 */
@Mojo(name = "verify", requiresOnline = true, defaultPhase = LifecyclePhase.VERIFY)
public final class ContrastVerifyMojo extends AbstractAssessMojo {

  /**
   * Verifies that no vulnerabilities were found at this or a higher severity level. Severity levels
   * include Note, Low, Medium, High, and Critical.
   */
  @Parameter(property = "minSeverity", defaultValue = "Medium")
  String minSeverity;

  public void execute() throws MojoFailureException {
    verifyAppIdOrNameNotNull();
    ContrastSDK contrast = connectToContrast();

    getLog().info("Successfully authenticated to Contrast.");

    getLog().info("Checking for new vulnerabilities for appVersion [" + computedAppVersion + "]");

    final String applicationId;
    if (getAppId() != null) {
      applicationId = getAppId();
      if (getAppName() != null) {
        getLog().info("Using 'appId' property; 'appName' property is ignored.");
      }

    } else {
      applicationId = getApplicationId(contrast, getAppName());
    }

    List<Long> serverIds = null;

    if (getServerName() != null) {
      serverIds = getServerId(contrast, applicationId);
    }

    TraceFilterForm form = getTraceFilterForm(serverIds);

    getLog().info("Sending vulnerability request to Contrast.");

    Traces traces;

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    try {
      final String organizationID = getOrganizationId();
      traces = contrast.getTraces(organizationID, applicationId, form);
    } catch (IOException e) {
      throw new MojoFailureException("Unable to retrieve the traces.", e);
    } catch (UnauthorizedException e) {
      throw new MojoFailureException("Unable to connect to Contrast.", e);
    }

    if (traces != null && traces.getCount() > 0) {
      getLog().info(traces.getCount() + " new vulnerability(s) were found.");

      for (Trace trace : traces.getTraces()) {
        getLog().info(generateTraceReport(trace));
      }

      throw new MojoFailureException(
          "Your application is vulnerable. Please see the above report for new vulnerabilities.");
    } else {
      getLog().info("No new vulnerabilities were found.");
    }

    getLog().info("Finished verifying your application.");
  }

  TraceFilterForm getTraceFilterForm(List<Long> serverIds) {
    TraceFilterForm form = new TraceFilterForm();
    form.setSeverities(getSeverityList(minSeverity));
    form.setAppVersionTags(Collections.singletonList(computedAppVersion));
    if (serverIds != null) {
      form.setServerIds(serverIds);
    }
    return form;
  }

  /**
   * Retrieves the server id by server name
   *
   * @param sdk Contrast SDK object
   * @param applicationId application id to filter on
   * @return List<Long> id of the servers
   * @throws MojoFailureException
   */
  private List<Long> getServerId(ContrastSDK sdk, String applicationId)
      throws MojoFailureException {
    ServerFilterForm serverFilterForm = new ServerFilterForm();
    serverFilterForm.setApplicationIds(Arrays.asList(applicationId));

    Servers servers;
    List<Long> serverIds;

    final String organizationID = getOrganizationId();
    try {
      serverFilterForm.setQ(URLEncoder.encode(getServerName(), "UTF-8"));
      servers = sdk.getServersWithFilter(organizationID, serverFilterForm);
    } catch (IOException e) {
      throw new MojoFailureException("Unable to retrieve the servers.", e);
    } catch (UnauthorizedException e) {
      throw new MojoFailureException("Unable to connect to Contrast.", e);
    }

    if (!servers.getServers().isEmpty()) {
      serverIds = new ArrayList<Long>();
      for (Server server : servers.getServers()) {
        serverIds.add(server.getServerId());
      }
    } else {
      throw new MojoFailureException(
          "\n\nServer with name '"
              + getServerName()
              + "' not found. Make sure this server name appears in Contrast under the 'Servers' tab.\n");
    }

    return serverIds;
  }

  /**
   * Retrieves the application id by application name; else null
   *
   * @param sdk Contrast SDK object
   * @param applicationName application name to filter on
   * @return String of the application
   * @throws MojoFailureException
   */
  private String getApplicationId(ContrastSDK sdk, String applicationName)
      throws MojoFailureException {

    Applications applications;

    final String organizationID = getOrganizationId();
    try {
      applications = sdk.getApplications(organizationID);
    } catch (Exception e) {
      throw new MojoFailureException(
          "\n\nUnable to retrieve the application list from Contrast. Please check Contrast connection configuration\n",
          e);
    }

    for (Application application : applications.getApplications()) {
      if (applicationName.equals(application.getName())) {
        return application.getId();
      }
    }

    throw new MojoFailureException(
        "\n\nApplication with name '"
            + applicationName
            + "' not found. Make sure this server name appears in Contrast under the 'Applications' tab.\n");
  }

  /**
   * Creates a basic report for a Trace object
   *
   * @param trace Trace object
   * @return String report
   */
  private String generateTraceReport(Trace trace) {
    StringBuilder sb = new StringBuilder();
    sb.append("Trace: ");
    sb.append(
        trace
            .getTitle()
            .replaceAll("\\{\\{\\#unlicensed\\}\\}", "(")
            .replaceAll("\\{\\{\\/unlicensed\\}\\}", ")"));
    sb.append("\nTrace Uuid: ");
    sb.append(trace.getUuid());
    sb.append("\nTrace Severity: ");
    sb.append(trace.getSeverity());
    sb.append("\nTrace Likelihood: ");
    sb.append(trace.getLikelihood());
    sb.append("\n");

    return sb.toString();
  }

  /**
   * Returns the sublist of severities greater than or equal to the configured severity level
   *
   * @param severity include severity to filter with severity list with
   * @return list of severity strings
   */
  private static EnumSet<RuleSeverity> getSeverityList(String severity) {

    List<String> severityList = SEVERITIES.subList(SEVERITIES.indexOf(severity), SEVERITIES.size());

    List<RuleSeverity> ruleSeverities = new ArrayList<RuleSeverity>();

    for (String severityToAdd : severityList) {
      ruleSeverities.add(RuleSeverity.valueOf(severityToAdd.toUpperCase()));
    }

    return EnumSet.copyOf(ruleSeverities);
  }

  // Severity levels
  private static final List<String> SEVERITIES =
      Arrays.asList("Note", "Low", "Medium", "High", "Critical");
}
