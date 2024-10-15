package com.contrastsecurity.http;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2024 Contrast Security, Inc.
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

import static com.contrastsecurity.utils.ContrastSDKUtils.buildExpand;

import com.contrastsecurity.models.AgentType;
import java.io.UnsupportedEncodingException;
import java.util.EnumSet;

public class UrlBuilder {

  private static UrlBuilder instance = new UrlBuilder();

  private UrlBuilder() {}

  public static UrlBuilder getInstance() {
    return instance;
  }

  public String getGlobalPropertiesUrl() {
    return "/ng/global/properties";
  }

  public String getProfileOrganizationsUrl() {
    return "/ng/profile/organizations";
  }

  public String getOrganizationUsersUrl(String organizationId) {
    // ddooley buildExpand not needed.  Always want login and signup details to determine
    // inactivity.
    return String.format("/ng/%s/users?expand=login,signup", organizationId);
  }

  public String getProfileDefaultOrganizationUrl() {
    return "/ng/profile/organizations/default";
  }

  public String getApplicationUrl(
      String organizationId,
      String appId,
      EnumSet<FilterForm.ApplicationExpandValues> expandValues) {
    return String.format(
        "/ng/%s/applications/%s%s", organizationId, appId, buildExpand(expandValues));
  }

  public String getApplicationFilterUrl(
      String organizationId, ApplicationFilterForm applicationFilterForm) {
    return String.format(
        "/ng/%s/applications/filter%s", organizationId, applicationFilterForm.toString());
  }

  public String getCreateApplicationUrl(String organizationId) {
    return String.format("/ng/integrations/organizations/%s/applications", organizationId);
  }

  public String getApplicationByNameAndLanguageUrl(
      String organizationId, String appName, String language) {
    return String.format(
        "/ng/integrations/organizations/%s/applications?name=%s&language=%s",
        organizationId, appName, language);
  }

  public String getApplicationsUrl(String organizationId) {
    return String.format("/ng/%s/applications?%s", organizationId, "base=false");
  }

  public String getLicensedApplicationsUrl(String organizationId) {
    return String.format(
        "/ng/%s/applications%s",
        organizationId, "/filter?sort=appName&quickFilter=LICENSED&expand=license");
  }

  public String getApplicationsNameUrl(String organizationId) {
    return String.format("/ng/%s/applications/name", organizationId);
  }

  public String getCoverageUrl(String organizationId, String appId) {
    return String.format("/ng/%s/applications/%s/coverage", organizationId, appId);
  }

  public String getRouteCoverageUrl(String organizationId, String appId) {
    return String.format("/ng/%s/applications/%s/route?sort=-exercised", organizationId, appId);
  }

  public String getRouteCoverageWithMetadataUrl(String organizationId, String appId) {
    return String.format(
        "/ng/%s/applications/%s/route/filter?expand=observations", organizationId, appId);
  }

  public String getLibrariesUrl(String organizationId, FilterForm form) {
    String formString = form == null ? "" : form.toString();
    return String.format("/ng/%s/libraries%s", organizationId, formString);
  }

  public String getLibrariesUrl(
      String organizationId, String appId, EnumSet<FilterForm.LibrariesExpandValues> expandValues) {
    return String.format(
        "/ng/%s/applications/%s/libraries%s", organizationId, appId, buildExpand(expandValues));
  }

  public String getLibrariesFilterUrl(String organizationId, FilterForm form) {
    String formString = form == null ? "" : form.toString();
    return String.format("/ng/%s/libraries/filter%s", organizationId, formString);
  }

  public String getLibrariesFilterUrl(String organizationId, String appId, FilterForm form) {
    String formString = form == null ? "" : form.toString();
    return String.format(
        "/ng/%s/applications/%s/libraries/filter%s", organizationId, appId, formString);
  }

  public String getLibraryStatsUrl(String organizationId) {
    return String.format("/ng/%s/libraries/stats", organizationId);
  }

  public String getLibraryScoresUrl(String organizationId) {
    return String.format("/ng/%s/libraries/breakdown/scores", organizationId);
  }

  public String getServersUrl(String organizationId, FilterForm form) {
    String formString = form == null ? "" : form.toString();
    return String.format("/ng/%s/servers%s", organizationId, formString);
  }

  public String getServersFilterUrl(String organizationId, FilterForm form) {
    String formString = form == null ? "" : form.toString();
    return String.format("/ng/%s/servers/filter%s", organizationId, formString);
  }

  public String getTracesByOrganizationUrl(String organizationId, TraceFilterForm form)
      throws UnsupportedEncodingException {
    String formString = form == null ? "" : form.toQuery();
    return String.format("/ng/%s/orgtraces/filter/%s", organizationId, formString);
  }

  public String getTracesByApplicationUrl(String organizationId, String appId, TraceFilterForm form)
      throws UnsupportedEncodingException {
    String formString = form == null ? "" : form.toQuery();
    return String.format("/ng/%s/traces/%s/filter/%s", organizationId, appId, formString);
  }

  public String getRecommendationByTraceId(String organizationId, String traceId) {
    return String.format("/ng/%s/traces/%s/recommendation", organizationId, traceId);
  }

  public String getStoryByTraceId(String organizationId, String traceId) {
    return String.format("/ng/%s/traces/%s/story", organizationId, traceId);
  }

  public String getHttpRequestByTraceId(String organizationId, String traceId) {
    return String.format("/ng/%s/traces/%s/httprequest", organizationId, traceId);
  }

  public String getEventSummary(String organizationId, String traceId) {
    return String.format("/ng/%s/traces/%s/events/summary", organizationId, traceId);
  }

  public String getEventDetails(String organizationId, String traceId, String eventId) {
    return String.format("/ng/%s/traces/%s/events/%s/details", organizationId, traceId, eventId);
  }

  public String getNotesByApplicationUrl(
      String organizationId, String appId, String traceId, TraceFilterForm form)
      throws UnsupportedEncodingException {
    String formString = form == null ? "" : form.toQuery();
    return String.format(
        "/ng/%s/applications/%s/traces/%s/notes", organizationId, appId, traceId, formString);
  }

  public String getOrCreateTagsByOrganization(String organizationId) {
    return String.format("/ng/%s/tags/traces", organizationId);
  }

  public String getTagsByTrace(String organizationId, String traceId) {
    return String.format("/ng/%s/tags/traces/trace/%s", organizationId, traceId);
  }

  public String deleteTag(String organizationId, String traceId) {
    return String.format("/ng/%s/tags/trace/%s", organizationId, traceId);
  }

  public String getTraceTagsByApplicationUrl(String organizationId, String appId) {
    return String.format("/ng/%s/tags/traces/application/%s", organizationId, appId);
  }

  public String getTracesWithBodyUrl(String organizationId, String appId)
      throws UnsupportedEncodingException {
    return String.format("/ng/%s/traces/%s/filter", organizationId, appId);
  }

  public String getSessionMetadataForApplicationUrl(
      String organizationId, String appId, TraceFilterForm form)
      throws UnsupportedEncodingException {
    String formString = form == null ? "?" : form.toQuery();
    formString = formString.isEmpty() ? "?" : String.format("%s&", formString);

    return String.format(
        "/ng/%s/metadata/session/%s/filters%smodules=%s", organizationId, appId, formString, appId);
  }

  public String getAttestationReportByApplicationUrl(String organizationId, String appId) {
    return String.format("/ng/%s/applications/%s/attestation", organizationId, appId);
  }

  public String clearNotificationsUrl(String organizationId) {
    return String.format("/ng/%s/notifications/read", organizationId);
  }

  public String getNotificationsUrl(String organizationId, TraceFilterForm form) {
    return String.format("/ng/%s/notifications%s", organizationId, form);
  }

  public String getServerTagsUrl(String organizationId, String appId) {
    return String.format("/ng/%s/tags/servers/list/application/%s", organizationId, appId);
  }

  public String downloadAttestationReportUrl(
      String organizationId, String userId, String reportId) {
    return String.format(
        "/ng/%s/reports/download/%s/%s?expand=skip_links", organizationId, userId, reportId);
  }

  public String setTraceStatus(String organizationId) {
    return String.format("/ng/%s/orgtraces/mark", organizationId);
  }

  public String getTraceListingUrl(
      String organizationId, String appId, TraceFilterType traceFilterType) {
    return String.format(
        "/ng/%s/traces/%s/filter/%s/listing", organizationId, appId, traceFilterType.toString());
  }

  public String getTracesWithFilterUrl(
      String organizationId,
      String appId,
      TraceFilterType traceFilterType,
      TraceFilterKeycode traceFilterKeycode,
      TraceFilterForm form)
      throws UnsupportedEncodingException {
    String formString = form == null ? "" : form.toQuery();
    return String.format(
        "/ng/%s/traces/%s/filter/%s/%s/search%s",
        organizationId,
        appId,
        traceFilterType.toString(),
        traceFilterKeycode.toString(),
        formString);
  }

  public String getRules(String organizationId) {
    return String.format("/ng/%s/rules", organizationId);
  }

  public String getSecurityCheckUrl(String organizationId) {
    return String.format("/ng/%s/securityChecks", organizationId);
  }

  public String getEnabledJobOutcomePolicyListUrl(String organizationId) {
    return String.format("/ng/%s/jobOutcomePolicies/enabled", organizationId);
  }

  public String getEnabledJobOutcomePolicyListUrlByApplication(
      String organizationId, String appId) {
    return String.format("/ng/%s/jobOutcomePolicies/enabled/%s", organizationId, appId);
  }

  public String getAssessLicensingUrl(String organizationId) {
    return String.format("/ng/%s/licenses", organizationId);
  }

  public String getYearlyVulnTrendUrl(String organizationId) {
    return String.format("/ng/%s/orgtraces/stats/trend/year/total", organizationId);
  }

  public String getYearlyNewVulnTrendUrl(String organizationId) {
    return String.format("/ng/%s/orgtraces/stats/trend/year/new", organizationId);
  }

  public String getYearlyVulnTrendForApplicationUrl(String organizationId, String appId) {
    return String.format(
        "/ng/%s/orgtraces/stats/trend/year/total?applications=%s", organizationId, appId);
  }

  public String getAgentUrl(AgentType type, String organizationId, String profileName) {
    String url;

    switch (type) {
      case JAVA:
        url = String.format("/ng/%s/agents/%s/java?jvm=1_6", organizationId, profileName);
        break;
      case JAVA1_5:
        url = String.format("/ng/%s/agents/%s/java?jvm=1_5", organizationId, profileName);
        break;
      case DOTNET:
        url = String.format("/ng/%s/agents/%s/dotnet", organizationId, profileName);
        break;
      case NODE:
        url = String.format("/ng/%s/agents/%s/node", organizationId, profileName);
        break;
      case RUBY:
        url = String.format("/ng/%s/agents/%s/ruby", organizationId, profileName);
        break;
      case PYTHON:
        url = String.format("/ng/%s/agents/%s/python", organizationId, profileName);
        break;
      case DOTNET_CORE:
        url = String.format("/ng/%s/agents/%s/dotnet_core", organizationId, profileName);
        break;
      default:
        url = "";
        break;
    }

    return url;
  }
}
