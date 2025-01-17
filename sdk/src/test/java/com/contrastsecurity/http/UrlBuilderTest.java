package com.contrastsecurity.http;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2025 Contrast Security, Inc.
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

import static org.assertj.core.api.Assertions.assertThat;

import com.contrastsecurity.models.AgentType;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class UrlBuilderTest {

  private static String applicationId;
  private static String organizationId;
  private static String testTrace;
  private static UrlBuilder urlBuilder;

  @BeforeAll
  public static void setUp() {
    applicationId = "test-app";
    organizationId = "test-org";
    testTrace = "test-trace";

    urlBuilder = UrlBuilder.getInstance();
  }

  @Test
  public void testProfileOrganizationsUrl() {
    String expectedUrl = "/ng/profile/organizations";

    assertThat(urlBuilder.getProfileOrganizationsUrl()).isEqualTo(expectedUrl);
  }

  @Test
  public void testApplicationUrl() {
    String expectedUrl = "/ng/test-org/applications/test-app";

    assertThat(urlBuilder.getApplicationUrl(organizationId, applicationId, null))
        .isEqualTo(expectedUrl);
  }

  @Test
  public void testFilterApplicationUrl() {
    String expectedUrl =
        "/ng/test-org/applications/filter?expand=license&includeArchived=false&includeOnlyLicensed=false&includeMerged=true";

    ApplicationFilterForm form = new ApplicationFilterForm();

    form.setExpand(EnumSet.of(ApplicationFilterForm.ApplicationExpandValues.LICENSE));

    assertThat(urlBuilder.getApplicationFilterUrl(organizationId, form)).isEqualTo(expectedUrl);
  }

  @Test
  public void testCreateApplicationUrl() {
    String expectedUrl = "/ng/integrations/organizations/test-org/applications";
    assertThat(urlBuilder.getCreateApplicationUrl(organizationId)).isEqualTo(expectedUrl);
  }

  @Test
  public void testApplicationByNameAndLanguageUrl() {
    String expectedUrl =
        "/ng/integrations/organizations/test-org/applications?name=app&language=JAVA";
    assertThat(urlBuilder.getApplicationByNameAndLanguageUrl(organizationId, "app", "JAVA"))
        .isEqualTo(expectedUrl);
  }

  @Test
  public void testApplicationsUrl() {
    String expectedUrl = "/ng/test-org/applications?base=false";

    assertThat(urlBuilder.getApplicationsUrl(organizationId)).isEqualTo(expectedUrl);
  }

  @Test
  public void testApplicationsNamesUrl() {
    String expectedUrl = "/ng/test-org/applications/name";

    assertThat(urlBuilder.getApplicationsNameUrl(organizationId)).isEqualTo(expectedUrl);
  }

  @Test
  public void testCoverageUrl() {
    String expectedUrl = "/ng/test-org/applications/test-app/coverage";

    assertThat(urlBuilder.getCoverageUrl(organizationId, applicationId)).isEqualTo(expectedUrl);
  }

  @Test
  public void testLibrariesUrl() {
    String expectedUrl = "/ng/test-org/applications/test-app/libraries";

    String resultUrl = urlBuilder.getLibrariesUrl(organizationId, applicationId, null);

    assertThat(resultUrl).isEqualTo(expectedUrl);
  }

  @Test
  public void testTracesUrl() throws UnsupportedEncodingException {
    String expectedUrl =
        "/ng/test-org/traces/test-app/filter/?expand=application&tracked=true&untracked=false";

    TraceFilterForm form = new TraceFilterForm();

    form.setExpand(EnumSet.of(TraceFilterForm.TraceExpandValue.APPLICATION));

    assertThat(urlBuilder.getTracesByApplicationUrl(organizationId, applicationId, form))
        .isEqualTo(expectedUrl);
  }

  @Test
  public void testGetTracesByApplicationUrl() throws UnsupportedEncodingException {
    String expectedUrl =
        "/ng/test-org/traces/test-app/filter/?appVersionTags=The+application&tracked=true&untracked=false";

    TraceFilterForm form = new TraceFilterForm();
    List<String> appVersionTags = new ArrayList<String>();
    appVersionTags.add("The application");

    form.setAppVersionTags(appVersionTags);

    assertThat(urlBuilder.getTracesByApplicationUrl(organizationId, applicationId, form))
        .isEqualTo(expectedUrl);
  }

  @Test
  public void testGetTracesWithBodyUrl() throws UnsupportedEncodingException {
    String expectedUrl = "/ng/test-org/traces/test-app/filter";

    assertThat(urlBuilder.getTracesWithBodyUrl(organizationId, applicationId))
        .isEqualTo(expectedUrl);
  }

  @Test
  public void testSecurityCheckUrl() {
    String expectedSecurityCheckUrl = "/ng/test-org/securityChecks";
    assertThat(urlBuilder.getSecurityCheckUrl(organizationId)).isEqualTo(expectedSecurityCheckUrl);
  }

  @Test
  public void testEnabledJobOutcomePolicyListUrl() {
    String expectedJobOutcomePolicyListUrl = "/ng/test-org/jobOutcomePolicies/enabled";
    assertThat(urlBuilder.getEnabledJobOutcomePolicyListUrl(organizationId))
        .isEqualTo(expectedJobOutcomePolicyListUrl);
  }

  @Test
  public void testEnabledJobOutcomePolicyListUrlByApplication() {
    String expectedJobOutcomePolicyListUrl = "/ng/test-org/jobOutcomePolicies/enabled/test-app";
    assertThat(
            urlBuilder.getEnabledJobOutcomePolicyListUrlByApplication(
                organizationId, applicationId))
        .isEqualTo(expectedJobOutcomePolicyListUrl);
  }

  @Test
  public void testGetRecommendationByTraceIdUrl() {
    String expectedRecommendationUrl = "/ng/test-org/traces/test-trace/recommendation";
    assertThat(urlBuilder.getRecommendationByTraceId(organizationId, testTrace))
        .isEqualTo(expectedRecommendationUrl);
  }

  @Test
  public void testGetStoryByTraceIdUrl() {
    String expectedStoryUrl = "/ng/test-org/traces/test-trace/story";
    assertThat(urlBuilder.getStoryByTraceId(organizationId, testTrace)).isEqualTo(expectedStoryUrl);
  }

  @Test
  public void testGetHttpRequestByTraceIdUrl() {
    String expectedHttpUrl = "/ng/test-org/traces/test-trace/httprequest";
    assertThat(urlBuilder.getHttpRequestByTraceId(organizationId, testTrace))
        .isEqualTo(expectedHttpUrl);
  }

  @Test
  public void testGetEventSummaryUrl() {
    String expectedEventSummaryUrl = "/ng/test-org/traces/test-trace/events/summary";
    assertThat(urlBuilder.getEventSummary(organizationId, testTrace))
        .isEqualTo(expectedEventSummaryUrl);
  }

  @Test
  public void testGetEventDetailsUrl() {
    String expectedEventDetailsUrl = "/ng/test-org/traces/test-trace/events/event-id/details";
    assertThat(urlBuilder.getEventDetails(organizationId, testTrace, "event-id"))
        .isEqualTo(expectedEventDetailsUrl);
  }

  @Test
  public void testGetOrCreateTagsByOrganizationUrl() {
    String expectedGetOrCreateTagsUrl = "/ng/test-org/tags/traces";
    assertThat(urlBuilder.getOrCreateTagsByOrganization(organizationId))
        .isEqualTo(expectedGetOrCreateTagsUrl);
  }

  @Test
  public void testGetTagsByTraceUrl() {
    String expectedTagsUrl = "/ng/test-org/tags/traces/trace/test-trace";
    assertThat(urlBuilder.getTagsByTrace(organizationId, testTrace)).isEqualTo(expectedTagsUrl);
  }

  @Test
  public void testDeleteTagUrl() {
    String expectedTagsUrl = "/ng/test-org/tags/trace/test-trace";
    assertThat(urlBuilder.deleteTag(organizationId, testTrace)).isEqualTo(expectedTagsUrl);
  }

  @Test
  public void testSetStatusUrl() {
    String expectedTagsUrl = "/ng/test-org/orgtraces/mark";
    assertThat(urlBuilder.setTraceStatus(organizationId)).isEqualTo(expectedTagsUrl);
  }

  @Test
  public void testAgentUrls() {
    String expectedJavaUrl = "/ng/test-org/agents/default/java?jvm=1_6";

    assertThat(urlBuilder.getAgentUrl(AgentType.JAVA, organizationId, "default"))
        .isEqualTo(expectedJavaUrl);

    String expectedNodeWithCustomProfileUrl = "/ng/test-org/agents/customnodeprofile/node";

    assertThat(urlBuilder.getAgentUrl(AgentType.NODE, organizationId, "customnodeprofile"))
        .isEqualTo(expectedNodeWithCustomProfileUrl);

    String expectedDotNetProfile = "/ng/test-org/agents/default/dotnet";

    assertThat(urlBuilder.getAgentUrl(AgentType.DOTNET, organizationId, "default"))
        .isEqualTo(expectedDotNetProfile);

    String expectedRubyUrl = "/ng/test-org/agents/default/ruby";

    assertThat(urlBuilder.getAgentUrl(AgentType.RUBY, organizationId, "default"))
        .isEqualTo(expectedRubyUrl);

    String expectedPythonUrl = "/ng/test-org/agents/default/python";

    assertThat(urlBuilder.getAgentUrl(AgentType.PYTHON, organizationId, "default"))
        .isEqualTo(expectedPythonUrl);

    String expectedDotNetCoreUrl = "/ng/test-org/agents/default/dotnet_core";

    assertThat(urlBuilder.getAgentUrl(AgentType.DOTNET_CORE, organizationId, "default"))
        .isEqualTo(expectedDotNetCoreUrl);
  }

  @Test
  public void testGetYearlyVulnTrendForApplicationUrl() {
    String expectedApplicationTrendUrl =
        "/ng/test-org/orgtraces/stats/trend/year/total?applications=test-application";

    assertThat(urlBuilder.getYearlyVulnTrendForApplicationUrl("test-org", "test-application"))
        .isEqualTo(expectedApplicationTrendUrl);
  }
}
