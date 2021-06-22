package com.contrastsecurity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.contrastsecurity.http.ApplicationFilterForm;
import com.contrastsecurity.http.TraceFilterForm;
import com.contrastsecurity.http.UrlBuilder;
import com.contrastsecurity.models.AgentType;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public class UrlBuilderTest {

  private static String applicationId;
  private static String organizationId;
  private static String testTrace;
  private static UrlBuilder urlBuilder;

  @BeforeClass
  public static void setUp() {
    applicationId = "test-app";
    organizationId = "test-org";
    testTrace = "test-trace";

    urlBuilder = UrlBuilder.getInstance();
  }

  @Test
  public void testProfileOrganizationsUrl() {
    String expectedUrl = "/ng/profile/organizations";

    assertEquals(expectedUrl, urlBuilder.getProfileOrganizationsUrl());
  }

  @Test
  public void testApplicationUrl() {
    String expectedUrl = "/ng/test-org/applications/test-app";

    assertEquals(expectedUrl, urlBuilder.getApplicationUrl(organizationId, applicationId, null));
  }

  @Test
  public void testFilterApplicationUrl() {
    String expectedUrl =
        "/ng/test-org/applications/filter?expand=license&includeArchived=false&includeOnlyLicensed=false&includeMerged=true";

    ApplicationFilterForm form = new ApplicationFilterForm();

    form.setExpand(EnumSet.of(ApplicationFilterForm.ApplicationExpandValues.LICENSE));

    assertEquals(expectedUrl, urlBuilder.getApplicationFilterUrl(organizationId, form));
  }

  @Test
  public void testCreateApplicationUrl() {
    String expectedUrl = "/ng/integrations/organizations/test-org/applications";
    assertEquals(expectedUrl, urlBuilder.getCreateApplicationUrl(organizationId));
  }

  @Test
  public void testApplicationByNameAndLanguageUrl() {
    String expectedUrl =
        "/ng/integrations/organizations/test-org/applications?name=app&language=JAVA";
    assertEquals(
        expectedUrl, urlBuilder.getApplicationByNameAndLanguageUrl(organizationId, "app", "JAVA"));
  }

  @Test
  public void testApplicationsUrl() {
    String expectedUrl = "/ng/test-org/applications?base=false";

    assertEquals(expectedUrl, urlBuilder.getApplicationsUrl(organizationId));
  }

  @Test
  public void testApplicationsNamesUrl() {
    String expectedUrl = "/ng/test-org/applications/name";

    assertEquals(expectedUrl, urlBuilder.getApplicationsNameUrl(organizationId));
  }

  @Test
  public void testCoverageUrl() {
    String expectedUrl = "/ng/test-org/applications/test-app/coverage";

    assertEquals(expectedUrl, urlBuilder.getCoverageUrl(organizationId, applicationId));
  }

  @Test
  public void testLibrariesUrl() {
    String expectedUrl = "/ng/test-org/applications/test-app/libraries";

    String resultUrl = urlBuilder.getLibrariesUrl(organizationId, applicationId, null);

    assertEquals(expectedUrl, resultUrl);
  }

  @Test
  public void testTracesUrl() {
    String expectedUrl = "/ng/test-org/traces/test-app/filter/?expand=application";

    TraceFilterForm form = new TraceFilterForm();

    form.setExpand(EnumSet.of(TraceFilterForm.TraceExpandValue.APPLICATION));

    try {
      assertEquals(
          expectedUrl, urlBuilder.getTracesByApplicationUrl(organizationId, applicationId, form));
    } catch (UnsupportedEncodingException e) {
      fail();
    }
  }

  @Test
  public void testGetTracesByApplicationUrl() {
    String expectedUrl = "/ng/test-org/traces/test-app/filter/?appVersionTags=The+application";

    TraceFilterForm form = new TraceFilterForm();
    List<String> appVersionTags = new ArrayList<String>();
    appVersionTags.add("The application");

    form.setAppVersionTags(appVersionTags);

    try {
      assertEquals(
          expectedUrl, urlBuilder.getTracesByApplicationUrl(organizationId, applicationId, form));
    } catch (UnsupportedEncodingException e) {
      fail();
    }
  }

  @Test
  public void testGetTracesByMetadataUrl() {
    String expectedUrl = "/ng/test-org/traces/test-app/filter";

    try {
      assertEquals(expectedUrl, urlBuilder.getTracesByMetadataUrl(organizationId, applicationId));
    } catch (UnsupportedEncodingException e) {
      fail();
    }
  }

  @Test
  public void testSecurityCheckUrl() {
    String expectedSecurityCheckUrl = "/ng/test-org/securityChecks";
    assertEquals(expectedSecurityCheckUrl, urlBuilder.getSecurityCheckUrl(organizationId));
  }

  @Test
  public void testEnabledJobOutcomePolicyListUrl() {
    String expectedJobOutcomePolicyListUrl = "/ng/test-org/jobOutcomePolicies/enabled";
    assertEquals(
        expectedJobOutcomePolicyListUrl,
        urlBuilder.getEnabledJobOutcomePolicyListUrl(organizationId));
  }

  @Test
  public void testEnabledJobOutcomePolicyListUrlByApplication() {
    String expectedJobOutcomePolicyListUrl = "/ng/test-org/jobOutcomePolicies/enabled/test-app";
    assertEquals(
        expectedJobOutcomePolicyListUrl,
        urlBuilder.getEnabledJobOutcomePolicyListUrlByApplication(organizationId, applicationId));
  }

  @Test
  public void testGetRecommendationByTraceIdUrl() {
    String expectedRecommendationUrl = "/ng/test-org/traces/test-trace/recommendation";
    assertEquals(
        expectedRecommendationUrl,
        urlBuilder.getRecommendationByTraceId(organizationId, testTrace));
  }

  @Test
  public void testGetStoryByTraceIdUrl() {
    String expectedStoryUrl = "/ng/test-org/traces/test-trace/story";
    assertEquals(expectedStoryUrl, urlBuilder.getStoryByTraceId(organizationId, testTrace));
  }

  @Test
  public void testGetHttpRequestByTraceIdUrl() {
    String expectedHttpUrl = "/ng/test-org/traces/test-trace/httprequest";
    assertEquals(expectedHttpUrl, urlBuilder.getHttpRequestByTraceId(organizationId, testTrace));
  }

  @Test
  public void testGetEventSummaryUrl() {
    String expectedEventSummaryUrl = "/ng/test-org/traces/test-trace/events/summary";
    assertEquals(expectedEventSummaryUrl, urlBuilder.getEventSummary(organizationId, testTrace));
  }

  @Test
  public void testGetEventDetailsUrl() {
    String expectedEventDetailsUrl = "/ng/test-org/traces/test-trace/events/event-id/details";
    assertEquals(
        expectedEventDetailsUrl, urlBuilder.getEventDetails(organizationId, testTrace, "event-id"));
  }

  @Test
  public void testGetOrCreateTagsByOrganizationUrl() {
    String expectedGetOrCreateTagsUrl = "/ng/test-org/tags/traces";
    assertEquals(
        expectedGetOrCreateTagsUrl, urlBuilder.getOrCreateTagsByOrganization(organizationId));
  }

  @Test
  public void testGetTagsByTraceUrl() {
    String expectedTagsUrl = "/ng/test-org/tags/traces/trace/test-trace";
    assertEquals(expectedTagsUrl, urlBuilder.getTagsByTrace(organizationId, testTrace));
  }

  @Test
  public void testDeleteTagUrl() {
    String expectedTagsUrl = "/ng/test-org/tags/trace/test-trace";
    assertEquals(expectedTagsUrl, urlBuilder.deleteTag(organizationId, testTrace));
  }

  @Test
  public void testSetStatusUrl() {
    String expectedTagsUrl = "/ng/test-org/orgtraces/mark";
    assertEquals(expectedTagsUrl, urlBuilder.setTraceStatus(organizationId));
  }

  @Test
  public void testAgentUrls() {
    String expectedJavaUrl = "/ng/test-org/agents/default/java?jvm=1_6";

    assertEquals(
        expectedJavaUrl, urlBuilder.getAgentUrl(AgentType.JAVA, organizationId, "default"));

    String expectedNodeWithCustomProfileUrl = "/ng/test-org/agents/customnodeprofile/node";

    assertEquals(
        expectedNodeWithCustomProfileUrl,
        urlBuilder.getAgentUrl(AgentType.NODE, organizationId, "customnodeprofile"));

    String expectedDotNetProfile = "/ng/test-org/agents/default/dotnet";

    assertEquals(
        expectedDotNetProfile, urlBuilder.getAgentUrl(AgentType.DOTNET, organizationId, "default"));

    String expectedRubyUrl = "/ng/test-org/agents/default/ruby";

    assertEquals(
        expectedRubyUrl, urlBuilder.getAgentUrl(AgentType.RUBY, organizationId, "default"));

    String expectedPythonUrl = "/ng/test-org/agents/default/python";

    assertEquals(
        expectedPythonUrl, urlBuilder.getAgentUrl(AgentType.PYTHON, organizationId, "default"));

    String expectedDotNetCoreUrl = "/ng/test-org/agents/default/dotnet_core";

    assertEquals(
        expectedDotNetCoreUrl,
        urlBuilder.getAgentUrl(AgentType.DOTNET_CORE, organizationId, "default"));
  }

  @Test
  public void testGetYearlyVulnTrendForApplicationUrl() {
    String expectedApplicationTrendUrl =
        "/ng/test-org/orgtraces/stats/trend/year/total?applications=test-application";

    assertEquals(
        expectedApplicationTrendUrl,
        urlBuilder.getYearlyVulnTrendForApplicationUrl("test-org", "test-application"));
  }
}
