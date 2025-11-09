package com.contrastsecurity.sdk;

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

import com.contrastsecurity.exceptions.ApplicationCreateException;
import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.http.ApplicationFilterForm;
import com.contrastsecurity.http.FilterForm;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.JobOutcomePolicyListResponse;
import com.contrastsecurity.http.LibraryFilterForm;
import com.contrastsecurity.http.MediaType;
import com.contrastsecurity.http.RequestConstants;
import com.contrastsecurity.http.SecurityCheckForm;
import com.contrastsecurity.http.SecurityCheckResponse;
import com.contrastsecurity.http.ServerFilterForm;
import com.contrastsecurity.http.TraceFilterForm;
import com.contrastsecurity.http.TraceFilterKeycode;
import com.contrastsecurity.http.TraceFilterType;
import com.contrastsecurity.http.UrlBuilder;
import com.contrastsecurity.models.AgentType;
import com.contrastsecurity.models.Application;
import com.contrastsecurity.models.Applications;
import com.contrastsecurity.models.AssessLicenseOverview;
import com.contrastsecurity.models.Chapter;
import com.contrastsecurity.models.Coverage;
import com.contrastsecurity.models.EventDetails;
import com.contrastsecurity.models.EventResource;
import com.contrastsecurity.models.EventSummaryResponse;
import com.contrastsecurity.models.GenericResponse;
import com.contrastsecurity.models.GlobalProperties;
import com.contrastsecurity.models.HttpRequestResponse;
import com.contrastsecurity.models.JobOutcomePolicy;
import com.contrastsecurity.models.Libraries;
import com.contrastsecurity.models.LibraryScores;
import com.contrastsecurity.models.LibraryStats;
import com.contrastsecurity.models.MakeRequestResponse;
import com.contrastsecurity.models.MetadataEntity;
import com.contrastsecurity.models.MetadataFilterResponse;
import com.contrastsecurity.models.NotificationsResponse;
import com.contrastsecurity.models.Organizations;
import com.contrastsecurity.models.PropertyResource;
import com.contrastsecurity.models.RecommendationResponse;
import com.contrastsecurity.models.RouteCoverageBySessionIDAndMetadataRequest;
import com.contrastsecurity.models.RouteCoverageResponse;
import com.contrastsecurity.models.Rules;
import com.contrastsecurity.models.SecurityCheck;
import com.contrastsecurity.models.ServerTagsResponse;
import com.contrastsecurity.models.Servers;
import com.contrastsecurity.models.StoryResponse;
import com.contrastsecurity.models.Tag;
import com.contrastsecurity.models.Tags;
import com.contrastsecurity.models.TagsResponse;
import com.contrastsecurity.models.TraceFilterBody;
import com.contrastsecurity.models.TraceListing;
import com.contrastsecurity.models.TraceNotesResponse;
import com.contrastsecurity.models.Traces;
import com.contrastsecurity.models.TracesWithResponse;
import com.contrastsecurity.models.Users;
import com.contrastsecurity.models.VulnerabilityTrend;
import com.contrastsecurity.models.dtm.ApplicationCreateRequest;
import com.contrastsecurity.models.dtm.AttestationCreateRequest;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.contrastsecurity.sdk.scan.ScanManager;
import com.contrastsecurity.sdk.scan.ScanManagerImpl;
import com.contrastsecurity.utils.ContrastSDKUtils;
import com.contrastsecurity.utils.MetadataDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * Entry point for using the Contrast REST API. Make an instance of this class and call methods.
 * Easy!
 */
public class ContrastSDK {

  private final String apiKey;
  private final String serviceKey;
  private final String user;
  @Getter private String restApiURL;
  private final UrlBuilder urlBuilder;
  private final Gson gson;
  Proxy proxy;
  private final String userAgent;

  private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
  private int readTimeout = DEFAULT_READ_TIMEOUT;
  private static final int BUFFER_SIZE = 4096;

  public static class Builder {
    private final String user;
    private final String serviceKey;
    private final String apiKey;
    private Proxy proxy;
    private String restApiURL;
    private UserAgentProduct product;

    public Builder(String user, String serviceKey, String apiKey) {
      this.user = user;
      this.serviceKey = serviceKey;
      this.apiKey = apiKey;
      this.restApiURL = DEFAULT_API_URL;
      ContrastSDKUtils.validateUrl(this.restApiURL);
      this.proxy = Proxy.NO_PROXY;
    }

    public Builder withApiUrl(String apiUrl) {
      ContrastSDKUtils.validateUrl(apiUrl);
      this.restApiURL = ContrastSDKUtils.ensureApi(apiUrl);
      return this;
    }

    public Builder withProxy(Proxy proxy) {
      this.proxy = proxy;
      return this;
    }

    /**
     * Prepends a custom product to the user-agent header included in each request to the Contrast
     * API.
     *
     * @param product custom product
     * @return this
     */
    public Builder withUserAgentProduct(UserAgentProduct product) {
      this.product = product;
      return this;
    }

    public ContrastSDK build() {
      ContrastSDK sdk = new ContrastSDK(this.user, this.serviceKey, this.apiKey, this.product);
      sdk.restApiURL = this.restApiURL;
      sdk.proxy = this.proxy;
      return sdk;
    }
  }

  /**
   * Create a ContrastSDK object to use the Contrast V3 API
   *
   * <p>This will use the default api url which is https://app.contrastsecurity.com/Contrast/api
   *
   * @param user Username (e.g., joe@acme.com)
   * @param serviceKey User service key
   * @param apiKey API Key
   */
  private ContrastSDK(
      String user, String serviceKey, String apiKey, final UserAgentProduct component) {
    this.user = user;
    this.serviceKey = serviceKey;
    this.apiKey = apiKey;
    this.userAgent = buildUserAgent(component);
    this.restApiURL = DEFAULT_API_URL;
    ContrastSDKUtils.validateUrl(this.restApiURL);
    this.urlBuilder = UrlBuilder.getInstance();
    this.gson = GsonFactory.create();
    this.proxy = Proxy.NO_PROXY;
  }

  /** visible for testing */
  static String buildUserAgent(final UserAgentProduct product) {
    final UserAgentProduct platform =
        UserAgentProduct.of("Java", System.getProperty("java.runtime.version"));
    final UserAgentProduct sdk = UserAgentProduct.of("contrast-sdk-java", Version.VERSION);
    return Stream.of(product, sdk, platform)
        .filter(Objects::nonNull)
        .map(UserAgentProduct::toEncodedString)
        .collect(Collectors.joining(" "));
  }

  public ScanManager scan(final String organizationId) {
    return new ScanManagerImpl(this, gson, organizationId);
  }

  /**
   * Gets the global properties from TeamServer.
   *
   * @return GlobalProperties with the properties from TeamServer.
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public GlobalProperties getGlobalProperties() throws IOException, UnauthorizedException {
    try (InputStream is = makeRequest(HttpMethod.GET, this.urlBuilder.getGlobalPropertiesUrl());
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, GlobalProperties.class);
    }
  }

  /**
   * Get all Assess Licensing for an Organizations.
   *
   * @param organizationId the ID of the organization
   * @return AssessLicenseOverview with Assess Licensing for an Oeg.
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public AssessLicenseOverview getAssessLicensing(String organizationId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, this.urlBuilder.getAssessLicensingUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, AssessLicenseOverview.class);
    }
  }
  /**
   * Get Total (Total Open and Total Closed each month) Vulnerability Trend for an Organizations.
   *
   * @param organizationId the ID of the organization
   * @return VulnerabilityTrend with the yearly Vulnerability Trend for an Oeg.
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public VulnerabilityTrend getYearlyVulnTrend(String organizationId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, this.urlBuilder.getYearlyVulnTrendUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, VulnerabilityTrend.class);
    }
  }

  /**
   * Get New (New Open and New Closed each month) Vulnerability Trend for an Organizations.
   *
   * @param organizationId the ID of the organization
   * @return VulnerabilityTrend with the yearly Vulnerability Trend for an Oeg.
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public VulnerabilityTrend getYearlyNewVulnTrend(String organizationId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, this.urlBuilder.getYearlyNewVulnTrendUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, VulnerabilityTrend.class);
    }
  }

  /**
   * Get all Vulnerability Trend for an Application.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @return VulnerabilityTrend with the yearly Vulnerability Trend for an Oeg.
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public VulnerabilityTrend getYearlyVulnTrendForApplication(String organizationId, String appId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                this.urlBuilder.getYearlyVulnTrendForApplicationUrl(organizationId, appId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, VulnerabilityTrend.class);
    }
  }

  /**
   * Get all organizations for the user profile.
   *
   * @return Organization objects with a list of disabled and valid organizations for the user.
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Organizations getProfileOrganizations() throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, this.urlBuilder.getProfileOrganizationsUrl());
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, Organizations.class);
    }
  }

  /**
   * Get all users for an organization.
   *
   * @param organizationId the ID of the organization
   * @return A List of User Objects.
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Users getOrganizationUsers(String organizationId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, this.urlBuilder.getOrganizationUsersUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, Users.class);
    }
  }

  /**
   * Get the default organization for the user profile.
   *
   * @return Organization object with the default Organizaiton.
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Organizations getProfileDefaultOrganizations() throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, this.urlBuilder.getProfileDefaultOrganizationUrl());
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, Organizations.class);
    }
  }

  /**
   * Creates an application without a server that is meant to be instrumented later.
   *
   * @param organizationId
   * @param request
   * @return
   * @throws IOException
   * @throws UnauthorizedException
   */
  public Application createApplication(String organizationId, ApplicationCreateRequest request)
      throws IOException, UnauthorizedException, ApplicationCreateException {
    try (InputStream is =
            makeCreateRequest(
                HttpMethod.POST,
                urlBuilder.getCreateApplicationUrl(organizationId),
                this.gson.toJson(request),
                MediaType.JSON,
                false);
        InputStreamReader reader = new InputStreamReader(is)) {
      Applications response = this.gson.fromJson(reader, Applications.class);
      return response.getApplication();
    }
  }

  /**
   * Gets a single application based on the org, name, and language
   *
   * @param orgId ID of the organization
   * @param appName Application name when the application was first created
   * @param language Language of the application
   * @return the Application found, returns null if the application is not found
   * @throws IOException
   * @throws UnauthorizedException
   */
  public Application getApplicationByNameAndLanguage(
      String orgId, String appName, AgentType language) throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getApplicationByNameAndLanguageUrl(orgId, appName, language.name()));
        InputStreamReader reader = new InputStreamReader(is)) {
      Applications response = this.gson.fromJson(reader, Applications.class);
      return response.getApplication();
    }
  }

  /**
   * Private helper method for createApplication to make a request with special error handling
   *
   * @param method
   * @param path
   * @param body
   * @param mediaType
   * @return
   * @throws IOException
   * @throws UnauthorizedException
   * @throws ApplicationCreateException
   */
  private InputStream makeCreateRequest(
      HttpMethod method, String path, String body, MediaType mediaType, boolean setAcceptType)
      throws IOException, UnauthorizedException, ApplicationCreateException {
    String url = restApiURL + path;

    HttpURLConnection connection = makeConnection(url, method.toString());
    if (mediaType != null
        && body != null
        && (method.equals(HttpMethod.PUT) || method.equals(HttpMethod.POST))) {
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", mediaType.getType());
      if (setAcceptType) {
        connection.setRequestProperty("Accept", mediaType.getType());
      }
      OutputStream os = connection.getOutputStream();
      byte[] bodyByte = body.getBytes("utf-8");
      os.write(bodyByte, 0, bodyByte.length);
    }
    int rc = connection.getResponseCode();
    InputStream is;
    if (CREATE_APPLICATION_ERROR_RESPONSE.contains(rc)) {
      is = connection.getErrorStream();
      String message = getErrorMessage(is);
      throw new ApplicationCreateException(rc, message);
    } else if (rc >= BAD_REQUEST && rc < SERVER_ERROR) {
      throw new UnauthorizedException(rc);
    }
    is = connection.getInputStream();
    return is;
  }

  /**
   * Private helper method for extracting the messages from an errorstream
   *
   * @param errorStream
   * @return
   * @throws IOException
   */
  private String getErrorMessage(InputStream errorStream) throws IOException {
    InputStreamReader streamReader = new InputStreamReader(errorStream);
    StringBuilder builder = new StringBuilder();
    try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        builder.append(line);
      }
    }
    JsonObject json = this.gson.fromJson(builder.toString(), JsonObject.class);
    return json.get("messages").getAsString();
  }

  /**
   * Get summary information about a single app without expandValues.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @return Applications object that contains one Application; wrapper
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Applications getApplication(String organizationId, String appId)
      throws IOException, UnauthorizedException {
    return getApplication(organizationId, appId, null);
  }

  /**
   * Get summary information about a single app.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param expandValues Expand values to filter on
   * @return Applications object that contains one Application; wrapper
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Applications getApplication(
      String organizationId, String appId, EnumSet<FilterForm.ApplicationExpandValues> expandValues)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                this.urlBuilder.getApplicationUrl(organizationId, appId, expandValues));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, Applications.class);
    }
  }

  /**
   * Get the list of applications being monitored by Contrast.
   *
   * @param organizationId the ID of the organization
   * @return Applications object that contains the list of Application's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Applications getApplications(String organizationId)
      throws UnauthorizedException, IOException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getApplicationsUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Applications.class);
    }
  }

  /**
   * Get the list of filtered applications being monitored by Contrast.
   *
   * @param organizationId the ID of the organization
   * @param applicationFilterForm Query params to add more info to response
   * @return Applications object that contains the list of Application's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Applications getFilteredApplications(
      String organizationId, ApplicationFilterForm applicationFilterForm)
      throws UnauthorizedException, IOException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getApplicationFilterUrl(organizationId, applicationFilterForm));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Applications.class);
    }
  }

  /**
   * Get the list of licensed applications being monitored by Contrast.
   *
   * @param organizationId the ID of the organization
   * @return Applications object that contains the list of Application's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Applications getLicensedApplications(String organizationId)
      throws UnauthorizedException, IOException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getLicensedApplicationsUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Applications.class);
    }
  }

  public Applications getApplicationsNames(String organizationId)
      throws UnauthorizedException, IOException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getApplicationsNameUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Applications.class);
    }
  }

  /**
   * Return route coverage data about the monitored Contrast application.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param metadata option session metadata keys and values to return route coverage based on
   * @return RouteCoverage object for the given app
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public RouteCoverageResponse getRouteCoverage(
      String organizationId, String appId, RouteCoverageBySessionIDAndMetadataRequest metadata)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            metadata == null
                ? makeRequest(HttpMethod.GET, urlBuilder.getRouteCoverageUrl(organizationId, appId))
                : makeRequestWithBody(
                    HttpMethod.POST,
                    urlBuilder.getRouteCoverageWithMetadataUrl(organizationId, appId),
                    this.gson.toJson(metadata),
                    MediaType.JSON);
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, RouteCoverageResponse.class);
    }
  }

  /**
   * Return coverage data about the monitored Contrast application.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @return Coverage object for the given app
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Coverage getCoverage(String organizationId, String appId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getCoverageUrl(organizationId, appId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Coverage.class);
    }
  }

  /**
   * Return the libraries of the Contrast organization.
   *
   * @param organizationId the ID of the organization
   * @param filterForm FilterForm query parameters
   * @return Libraries object that contains the list of Library objects
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Libraries getLibraries(String organizationId, LibraryFilterForm filterForm)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getLibrariesUrl(organizationId, filterForm));
        Reader reader = new InputStreamReader(is)) {
      return gson.fromJson(reader, Libraries.class);
    }
  }

  /**
   * Return the libraries of the Contrast organization.
   *
   * @param organizationId the ID of the organization
   * @param filterForm FilterForm query parameters
   * @return Libraries object that contains the list of Library objects
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Libraries getLibrariesWithFilter(String organizationId, LibraryFilterForm filterForm)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getLibrariesFilterUrl(organizationId, filterForm));
        Reader reader = new InputStreamReader(is)) {
      return gson.fromJson(reader, Libraries.class);
    }
  }

  public Libraries getLibraries(String organizationId, String appId)
      throws IOException, UnauthorizedException {
    return getLibraries(organizationId, appId, null);
  }

  /**
   * Return the libraries of the monitored Contrast application.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param expandValues Query params to add more info to response
   * @return Libraries object that contains the list of Library objects
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Libraries getLibraries(
      String organizationId, String appId, EnumSet<FilterForm.LibrariesExpandValues> expandValues)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getLibrariesUrl(organizationId, appId, expandValues));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Libraries.class);
    }
  }

  /**
   * Return the libraries of the monitored Contrast application.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param filterForm FilterForm query parameters
   * @return Libraries object that contains the list of Library objects
   * @throws IOException if there was a communication problem
   */
  public Libraries getLibrariesWithFilter(
      String organizationId, String appId, LibraryFilterForm filterForm)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getLibrariesFilterUrl(organizationId, appId, filterForm));
        Reader reader = new InputStreamReader(is)) {
      return gson.fromJson(reader, Libraries.class);
    }
  }

  /**
   * Return the library Scores for an Organization.
   *
   * @param organizationId the ID of the organization
   * @return LibraryScores object that contains the Library scores for an Org
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public LibraryScores getLibraryScores(String organizationId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getLibraryScoresUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, LibraryScores.class);
    }
  }
  /**
   * Return the library Stats for an Organization.
   *
   * @param organizationId the ID of the organization
   * @return LibraryScores object that contains the Library stats for an Org
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public LibraryStats getLibraryStats(String organizationId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getLibraryStatsUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, LibraryStats.class);
    }
  }

  /**
   * Return the servers of the monitored Contrast application.
   *
   * @param organizationId the ID of the organization
   * @param filterForm FilterForm query parameters
   * @return Servers object that contains the list of Library objects
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Servers getServers(String organizationId, ServerFilterForm filterForm)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getServersUrl(organizationId, filterForm));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, Servers.class);
    }
  }

  /**
   * Return the servers of the monitored Contrast application.
   *
   * @param organizationId the ID of the organization
   * @param filterForm FilterForm query parameters
   * @return Servers object that contains the list of Library objects
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Servers getServersWithFilter(String organizationId, ServerFilterForm filterForm)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getServersFilterUrl(organizationId, filterForm));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, Servers.class);
    }
  }

  /**
   * Get the vulnerabilities in the application whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param form FilterForm query parameters
   * @return Traces object that contains the list of Trace's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   * @deprecated Use {@link #getTraces(String, String, TraceFilterBody) getTraces} instead.
   */
  @Deprecated
  public Traces getTraces(String organizationId, String appId, TraceFilterForm form)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getTracesByApplicationUrl(organizationId, appId, form));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Traces.class);
    }
  }

  /**
   * Get the vulnerabilities in the application that match specific metadata filters.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param filters TraceMetadataFilters filters to query on
   * @return Traces object that contains the list of Trace's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Traces getTraces(String organizationId, String appId, TraceFilterBody filters)
      throws IOException, UnauthorizedException {
    return getTraces(organizationId, appId, filters, null);
  }

  /**
   * Get the vulnerabilities in the application that match specific metadata filters with expanded
   * fields.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param filters TraceMetadataFilters filters to query on
   * @param expand the fields to expand (e.g., SESSION_METADATA, SERVER_ENVIRONMENTS)
   * @return Traces object that contains the list of Trace's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Traces getTraces(
      String organizationId,
      String appId,
      TraceFilterBody filters,
      EnumSet<TraceFilterForm.TraceExpandValue> expand)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequestWithBody(
                HttpMethod.POST,
                urlBuilder.getTracesWithBodyUrl(organizationId, appId, expand),
                this.gson.toJson(filters),
                MediaType.JSON);
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Traces.class);
    }
  }

  /**
   * Get the vulnerabilities in the application whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param form FilterForm query parameters
   * @return TracesWithResponse object that contains the list of Trace's and the response code
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public TracesWithResponse getTracesWithResponse(
      String organizationId, String appId, TraceFilterForm form)
      throws IOException, UnauthorizedException {
    MakeRequestResponse mrr =
        makeRequestWithResponse(
            HttpMethod.GET, urlBuilder.getTracesByApplicationUrl(organizationId, appId, form));
    try (Reader reader = new InputStreamReader(mrr.is)) {
      TracesWithResponse twr = new TracesWithResponse();
      twr.t = this.gson.fromJson(reader, Traces.class);
      twr.rc = mrr.rc;
      return twr;
    }
  }

  /**
   * Get the notes (discussion) for the vulnerability ID in the application whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param traceId the ID of the vulnerability
   * @param form FilterForm query parameters
   * @return Traces object that contains the list of Trace's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public TraceNotesResponse getNotes(
      String organizationId, String appId, String traceId, TraceFilterForm form)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getNotesByApplicationUrl(organizationId, appId, traceId, form));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, TraceNotesResponse.class);
    }
  }

  /**
   * Get the recommendation for the vulnerability ID that is passed in.
   *
   * @param organizationId the ID of the organization
   * @param traceId the ID of the vulnerability
   * @return RecommendationResponse object that contains the recommendation text, cwe and owasp
   *     links, rule references, and any custom recommendations or rule references, and response
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public RecommendationResponse getRecommendation(String organizationId, String traceId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getRecommendationByTraceId(organizationId, traceId));
        Reader reader = new InputStreamReader(is)) {
      return gson.fromJson(reader, RecommendationResponse.class);
    }
  }

  /**
   * Get the story for the vulnerability ID that is passed in.
   *
   * @param organizationId the ID of the organization
   * @param traceId the ID of the vulnerability
   * @return StoryResponse object that contains the story, which contains a list of chapters, and a
   *     set properties
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public StoryResponse getStory(String organizationId, String traceId)
      throws IOException, UnauthorizedException {
    final String inputString;
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getStoryByTraceId(organizationId, traceId));
        ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      final byte[] buffer = new byte[4096];
      int read;
      while ((read = is.read(buffer)) > 0) {
        bos.write(buffer, 0, read);
      }
      inputString = bos.toString(StandardCharsets.UTF_8.name());
    }
    StoryResponse story = gson.fromJson(inputString, StoryResponse.class);
    JsonObject object = (JsonObject) new JsonParser().parse(inputString);
    JsonObject storyObject = (JsonObject) object.get("story");
    if (storyObject != null) {
      JsonArray chaptersArray = (JsonArray) storyObject.get("chapters");
      List<Chapter> chapters = story.getStory().getChapters();
      if (chapters == null) {
        chapters = new ArrayList<>();
      } else {
        chapters.clear();
      }
      for (int i = 0; i < chaptersArray.size(); i++) {
        JsonObject member = (JsonObject) chaptersArray.get(i);
        Chapter chapter = gson.fromJson(member, Chapter.class);
        chapters.add(chapter);
        JsonObject properties = (JsonObject) member.get("properties");
        if (properties != null) {
          Set<Entry<String, JsonElement>> entries = properties.entrySet();
          Iterator<Entry<String, JsonElement>> iter = entries.iterator();
          List<PropertyResource> propertyResources = new ArrayList<>();
          chapter.setPropertyResources(propertyResources);
          while (iter.hasNext()) {
            Entry<String, JsonElement> prop = iter.next();
            // String key = prop.getKey();
            JsonElement entryValue = prop.getValue();
            if (entryValue != null && entryValue.isJsonObject()) {
              JsonObject obj = (JsonObject) entryValue;
              JsonElement name = obj.get("name");
              JsonElement value = obj.get("value");
              if (name != null && value != null) {
                PropertyResource propertyResource = new PropertyResource();
                propertyResource.setName(name.getAsString());
                propertyResource.setValue(value.getAsString());
                propertyResources.add(propertyResource);
              }
            }
          }
        }
      }
    }
    return story;
  }

  /**
   * Get the event for the vulnerability ID that is passed in.
   *
   * @param organizationId the ID of the organization
   * @param traceId the ID of the vulnerability
   * @return EventSummaryResponse object that contains the risk, evidence, and list of events
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public EventSummaryResponse getEventSummary(String organizationId, String traceId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getEventSummary(organizationId, traceId));
        Reader reader = new InputStreamReader(is)) {
      EventSummaryResponse eventResource = gson.fromJson(reader, EventSummaryResponse.class);
      for (EventResource event : eventResource.getEvents()) {
        if (event.getCollapsedEvents() != null && !event.getCollapsedEvents().isEmpty()) {
          getCollapsedEventsDetails(event, organizationId, traceId);
        } else {
          EventDetails eventDetails = getEventDetails(event, organizationId, traceId);
          event.setEvent(eventDetails.getEvent());
        }
      }
      return eventResource;
    }
  }

  /**
   * Set the collapsed event details and each parent event to track the list of events in order for
   * the vulnerability ID that is passed in.
   *
   * @param parentEvent the resource of the event to get details of
   * @param organizationId the ID of the organization
   * @param traceId the ID of the vulnerability
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  private void getCollapsedEventsDetails(
      EventResource parentEvent, final String organizationId, final String traceId)
      throws IOException, UnauthorizedException {
    for (EventResource event : parentEvent.getCollapsedEvents()) {
      EventDetails eventDetails = getEventDetails(event, organizationId, traceId);
      event.setEvent(eventDetails.getEvent());
      event.setParent(parentEvent);
    }
  }

  /**
   * Get the collapsed event details for the vulnerability ID that is passed in.
   *
   * @param event the resource of the event to get details of
   * @param organizationId the ID of the organization
   * @param traceId the ID of the vulnerability
   * @return EventDetails object that contains collapsed details of the given event
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  private EventDetails getEventDetails(EventResource event, String organizationId, String traceId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getEventDetails(organizationId, traceId, event.getId()));
        Reader reader = new InputStreamReader(is)) {
      return gson.fromJson(reader, EventDetails.class);
    }
  }

  /**
   * Get the http request for the vulnerability ID that is passed in.
   *
   * @param organizationId the ID of the organization
   * @param traceId the ID of the vulnerability
   * @return HttpRequestResponse object that contains the request object and the reason text.
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public HttpRequestResponse getHttpRequest(String organizationId, String traceId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getHttpRequestByTraceId(organizationId, traceId));
        Reader reader = new InputStreamReader(is)) {
      return gson.fromJson(reader, HttpRequestResponse.class);
    }
  }

  /**
   * Get the available vulnerability tags in the application whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @return TagsResponse object that contains the list of Tags
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public TagsResponse getVulnTagsByApplication(String organizationId, String appId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getTraceTagsByApplicationUrl(organizationId, appId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, TagsResponse.class);
    }
  }

  /**
   * Get the available session metadata values in the application whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param form FilterForm query parameters
   * @return Traces object that contains the list of Trace's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public MetadataFilterResponse getSessionMetadataForApplication(
      String organizationId, String appId, TraceFilterForm form)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getSessionMetadataForApplicationUrl(organizationId, appId, form));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, MetadataFilterResponse.class);
    }
  }

  /**
   * Generate an attestation report for the application whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param request
   * @throws IOException
   * @throws UnauthorizedException
   */
  public GenericResponse generateAttestationReport(
      String organizationId, String appId, AttestationCreateRequest request)
      throws IOException, UnauthorizedException, ApplicationCreateException {
    try (InputStream is =
            makeCreateRequest(
                HttpMethod.POST,
                urlBuilder.getAttestationReportByApplicationUrl(organizationId, appId),
                this.gson.toJson(request),
                MediaType.JSON,
                true);
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, GenericResponse.class);
    }
  }

  /**
   * Get the vulnerabilities in the organization whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param userId the id of the user who requested the report
   * @param reportId the id of the report that was generated
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public void downloadAttestationReport(String organizationId, String userId, String reportId)
      throws IOException, UnauthorizedException {
    downloadFile(
        HttpMethod.POST,
        urlBuilder.downloadAttestationReportUrl(organizationId, userId, reportId),
        ".");
  }

  /**
   * Get notifications for the org.
   *
   * @param organizationId the ID of the organization
   * @param form FilterForm query parameters
   * @return Notifications object that contains the response from the API
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public NotificationsResponse getNotifications(String organizationId, TraceFilterForm form)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getNotificationsUrl(organizationId, form));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, NotificationsResponse.class);
    }
  }

  /**
   * Get server tags for the org.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the app
   * @return ServerTagsResponse object that contains the server tags
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public ServerTagsResponse getServerTags(String organizationId, String appId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getServerTagsUrl(organizationId, appId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, ServerTagsResponse.class);
    }
  }

  /**
   * Delete the tag for the vulnerability whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param traceId the ID of the vulnerability
   * @param tag the tag name/value to delete
   * @return TagsResponse object that contains the http response and success message
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public TagsResponse deleteVulnerabilityTag(String organizationId, String traceId, Tag tag)
      throws IOException, UnauthorizedException {
    String tagsUrl = urlBuilder.deleteTag(organizationId, traceId);
    try (InputStream is =
            makeRequestWithBody(HttpMethod.DELETE, tagsUrl, gson.toJson(tag), MediaType.JSON);
        Reader reader = new InputStreamReader(is)) {
      return gson.fromJson(reader, TagsResponse.class);
    }
  }

  /**
   * Get the tags for the vulnerability whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param traceId the ID of the vulnerability
   * @return TagsResponse object that contains the http response and success message and the list of
   *     Tags
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public TagsResponse getTagsByTrace(String organizationId, String traceId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getTagsByTrace(organizationId, traceId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, TagsResponse.class);
    }
  }

  /**
   * Get the vulnerability tags for the organization whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @return TagsResponse object that contains the http response and success message and the list of
   *     Tags
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public TagsResponse getTraceTagsByOrganization(String organizationId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getOrCreateTagsByOrganization(organizationId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, TagsResponse.class);
    }
  }

  /**
   * Create a vulnerability tag for the organization whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @return TagsResponse object that contains the http response and success message
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public TagsResponse createTag(String organizationId, Tags tags)
      throws IOException, UnauthorizedException {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(MetadataEntity.class, new MetadataDeserializer())
            .create();
    String tagsUrl = urlBuilder.getOrCreateTagsByOrganization(organizationId);
    try (InputStream is =
            makeRequestWithBody(
                HttpMethod.PUT,
                tagsUrl,
                gson.toJson(tags.setTagNamesAndGetTagObject()),
                MediaType.JSON);
        Reader reader = new InputStreamReader(is)) {
      return gson.fromJson(reader, TagsResponse.class);
    }
  }

  /**
   * Clear notifications for the org.
   *
   * @param organizationId the ID of the organization
   * @return GenericResponse object that contains the response from the server
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public GenericResponse clearNotifications(String organizationId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.PUT, urlBuilder.clearNotificationsUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, GenericResponse.class);
    }
  }

  /**
   * Get the vulnerabilities in the organization whose ID is passed in.
   *
   * @param organizationId the ID of the organization
   * @param form FilterForm query parameters
   * @return Traces object that contains the list of Trace's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Traces getTracesInOrg(String organizationId, TraceFilterForm form)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getTracesByOrganizationUrl(organizationId, form));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, Traces.class);
    }
  }

  /**
   * Get the filters for the traces in the application.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @return TraceListing object that contains the trace filters for the application
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public TraceListing getTraceFilters(String organizationId, String appId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getTraceListingUrl(organizationId, appId, TraceFilterType.VULNTYPE));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, TraceListing.class);
    }
  }

  public TraceListing getTraceFiltersByType(
      String organizationId, String appId, TraceFilterType type)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getTraceListingUrl(organizationId, appId, type));
        Reader reader = new InputStreamReader(is)) {

      return this.gson.fromJson(reader, TraceListing.class);
    }
  }

  /**
   * Get the vulnerabilities in the application whose ID is passed in with a filter.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param traceFilterType filter type
   * @param keycode id or key to filter on
   * @param form FilterForm query parameters
   * @return Traces object that contains the list of Trace's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Traces getTracesWithFilter(
      String organizationId,
      String appId,
      TraceFilterType traceFilterType,
      TraceFilterKeycode keycode,
      TraceFilterForm form)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getTracesWithFilterUrl(
                    organizationId, appId, traceFilterType, keycode, form));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Traces.class);
    }
  }

  public GenericResponse setTraceStatus(String organizationId, String statusRequest)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequestWithBody(
                HttpMethod.PUT,
                urlBuilder.setTraceStatus(organizationId),
                statusRequest,
                MediaType.JSON);
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, GenericResponse.class);
    }
  }

  /**
   * Get the vulnerabilities in the application by the rule.
   *
   * @param organizationId the ID of the organization
   * @param appId the ID of the application
   * @param ruleNames FilterForm query parameters
   * @return Traces object that contains the list of Trace's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  @Deprecated
  public Traces getTraceFilterByRule(String organizationId, String appId, List<String> ruleNames)
      throws IOException, UnauthorizedException {
    TraceFilterForm ruleNameForm = new TraceFilterForm();
    ruleNameForm.setVulnTypes(ruleNames);
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getTracesByApplicationUrl(organizationId, appId, ruleNameForm));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Traces.class);
    }
  }

  /**
   * Make a security check in a given organization by the security check form
   *
   * @param organizationId the ID of the organization
   * @param securityCheckForm the security check form
   * @return the security check that was made
   * @throws IOException
   * @throws UnauthorizedException
   */
  public SecurityCheck makeSecurityCheck(String organizationId, SecurityCheckForm securityCheckForm)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequestWithBody(
                HttpMethod.POST,
                urlBuilder.getSecurityCheckUrl(organizationId),
                this.gson.toJson(securityCheckForm),
                MediaType.JSON);
        Reader reader = new InputStreamReader(is)) {
      SecurityCheckResponse response = this.gson.fromJson(reader, SecurityCheckResponse.class);
      return response.getSecurityCheck();
    }
  }

  /**
   * Gets a list of enabled Job Outcome policies in an organization
   *
   * @param organizationId The organization ID
   * @return The list of enabled Job Outcome Policies
   * @throws IOException
   * @throws UnauthorizedException
   */
  public List<JobOutcomePolicy> getEnabledJobOutcomePolicies(String organizationId)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET, urlBuilder.getEnabledJobOutcomePolicyListUrl(organizationId));
        Reader reader = new InputStreamReader(is)) {
      JobOutcomePolicyListResponse response =
          this.gson.fromJson(reader, JobOutcomePolicyListResponse.class);
      return response.getPolicies();
    }
  }

  /**
   * Gets a list of enabeld Job Outcome Policies in an organization that applies to an application
   *
   * @param organizationId The organization ID
   * @param appId The Application ID
   * @return the list of enabled Job Outcome Policies that apply to the application
   */
  public List<JobOutcomePolicy> getEnabledJoboutcomePoliciesByApplication(
      String organizationId, String appId) throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(
                HttpMethod.GET,
                urlBuilder.getEnabledJobOutcomePolicyListUrlByApplication(organizationId, appId));
        Reader reader = new InputStreamReader(is)) {
      JobOutcomePolicyListResponse response =
          this.gson.fromJson(reader, JobOutcomePolicyListResponse.class);
      return response.getPolicies();
    }
  }

  /**
   * Get the rules for an organization
   *
   * @param organizationId the ID of the organization
   * @return Traces object that contains the list of Trace's
   * @throws UnauthorizedException if the Contrast account failed to authorize
   * @throws IOException if there was a communication problem
   */
  public Rules getRules(String organizationId) throws IOException, UnauthorizedException {
    try (InputStream is = makeRequest(HttpMethod.GET, urlBuilder.getRules(organizationId));
        Reader reader = new InputStreamReader(is)) {
      return this.gson.fromJson(reader, Rules.class);
    }
  }

  /**
   * Download a contrast.jar agent associated with this account. The user should save this byte
   * array to a file named 'contrast.jar'. This signature takes a parameter which contains the name
   * of the saved engine profile to download.
   *
   * @param type the type of agent you want to download; Java, Java 1.5, .NET, or Node
   * @param profileName the name of the saved engine profile to download,
   * @param organizationId the ID of the organization,
   * @return a byte[] array of the contrast.jar file contents, which the user should convert to a
   *     new File
   * @throws IOException if there was a communication problem
   * @throws UnauthorizedException if authentication fails
   */
  public byte[] getAgent(AgentType type, String organizationId, String profileName)
      throws IOException, UnauthorizedException {
    try (InputStream is =
            makeRequest(HttpMethod.GET, urlBuilder.getAgentUrl(type, organizationId, profileName));
        ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      final byte[] buffer = new byte[4096];
      int read;
      while ((read = is.read(buffer)) > 0) {
        bos.write(buffer, 0, read);
      }
      return bos.toByteArray();
    }
  }

  /**
   * Download a contrast.jar agent associated with this account. The user should save this byte
   * array to a file named 'contrast.jar'. This signature takes a parameter which contains the name
   * of the saved engine profile to download.
   *
   * <p>This uses 'default' as the profile name.
   *
   * @param type the type of agent you want to download; Java, Java 1.5, .NET, or Node
   * @param organizationId the ID of the organization,
   * @return a byte[] array of the contrast.jar file contents, which the user should convert to a
   *     new File
   * @throws IOException if there was a communication problem
   * @throws UnauthorizedException if authentication fails
   */
  public byte[] getAgent(AgentType type, String organizationId)
      throws IOException, UnauthorizedException {
    return getAgent(type, organizationId, DEFAULT_AGENT_PROFILE);
  }

  public InputStream makeRequestWithBody(
      HttpMethod method, String path, String body, MediaType mediaType)
      throws IOException, UnauthorizedException {
    String url = restApiURL + path;
    HttpURLConnection connection = makeConnection(url, method.toString());
    if (mediaType != null
        && body != null
        && (method.equals(HttpMethod.PUT)
            || method.equals(HttpMethod.POST)
            || method.equals(HttpMethod.DELETE))) {
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", mediaType.getType());
      try (OutputStream os = connection.getOutputStream()) {
        byte[] bodyByte = body.getBytes(StandardCharsets.UTF_8);
        os.write(bodyByte, 0, bodyByte.length);
      }
    }
    int rc = connection.getResponseCode();
    if (rc >= HttpURLConnection.HTTP_BAD_REQUEST) {
      throw HttpResponseException.fromConnection(
          connection, "Received unexpected status code from Contrast");
    }
    return connection.getInputStream();
  }

  public InputStream makeRequest(HttpMethod method, String path)
      throws IOException, UnauthorizedException {
    return makeRequestWithResponse(method, path).is;
  }

  public MakeRequestResponse makeRequestWithResponse(HttpMethod method, String path)
      throws IOException, UnauthorizedException {
    String url = restApiURL + path;

    HttpURLConnection connection = makeConnection(url, method.toString());
    int rc = connection.getResponseCode();
    if (rc >= HttpURLConnection.HTTP_BAD_REQUEST) {
      throw HttpResponseException.fromConnection(
          connection, "Received unexpected status code from Contrast");
    }
    MakeRequestResponse mrr = new MakeRequestResponse();
    mrr.is = connection.getInputStream();
    mrr.rc = rc;
    return mrr;
  }

  public void downloadFile(HttpMethod method, String path, String saveDir)
      throws IOException, UnauthorizedException {
    String fileURL = restApiURL + path;

    // URL url = new URL(fileURL);
    // HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    HttpURLConnection connection = makeConnection(fileURL, method.toString());
    connection.setRequestProperty("accept", "application/json, text/plain, */*");
    connection.setRequestProperty("accept-encoding", "gzip, deflate, br");
    int responseCode = connection.getResponseCode();

    // always check HTTP response code first
    if (responseCode == HttpURLConnection.HTTP_OK) {
      String fileName = "";
      String disposition = connection.getHeaderField("Content-Disposition");
      String contentType = connection.getContentType();
      int contentLength = connection.getContentLength();

      if (disposition != null) {
        // extracts file name from header field
        int index = disposition.indexOf("filename=");
        if (index > 0) {
          fileName = disposition.substring(index + 9, disposition.length());
        }
      } else {
        // extracts file name from URL
        fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
      }

      System.out.println("fileName = " + fileName);

      // opens input stream from the HTTP connection
      InputStream inputStream = connection.getInputStream();
      String saveFilePath = saveDir + File.separator + fileName;

      // opens an output stream to save into file
      FileOutputStream outputStream = new FileOutputStream(saveFilePath);

      int bytesRead = -1;
      byte[] buffer = new byte[BUFFER_SIZE];
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      outputStream.close();
      inputStream.close();

      System.out.println("File downloaded to path: " + saveFilePath);
    } else {
      System.out.println("No file to download. Server replied HTTP code: " + responseCode);
    }
    connection.disconnect();
  }

  public HttpURLConnection makeConnection(String url, String method) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(this.proxy);
    connection.setRequestMethod(method);
    connection.setRequestProperty(
        RequestConstants.AUTHORIZATION, ContrastSDKUtils.makeAuthorizationToken(user, serviceKey));
    connection.setRequestProperty(RequestConstants.API_KEY, apiKey);
    connection.setRequestProperty("User-Agent", userAgent);

    connection.setUseCaches(false);
    if (connectionTimeout > DEFAULT_CONNECTION_TIMEOUT)
      connection.setConnectTimeout(connectionTimeout);
    if (readTimeout > DEFAULT_READ_TIMEOUT) connection.setReadTimeout(readTimeout);
    return connection;
  }

  /**
   * Sets a custom connection timeout for all SDK requests. This value must be set before a call to
   * makeConnection is done.
   *
   * @param timeout Timeout value in milliseconds.
   */
  public void setConnectionTimeout(final int timeout) {
    this.connectionTimeout = timeout;
  }

  /**
   * Set a custom read timeout for all SDK requests. This value must be set before calling
   * makeConnection method in order to take effect.
   *
   * @param timeout TImeout value in milliseconds
   */
  public void setReadTimeout(final int timeout) {
    this.readTimeout = timeout;
  }

  /**
   * Default connection timeout. If connection timeout its set to this value, custom timeout will be
   * ignored and requests will take the default value that its usually assigned to them.
   */
  public static final int DEFAULT_CONNECTION_TIMEOUT = -1;
  /**
   * Default read timeout. If read timeout its set to this value, custom timeout will be ignored and
   * requests will take default value that its usually assigned to them.
   */
  public static final int DEFAULT_READ_TIMEOUT = -1;

  private static final int BAD_REQUEST = 400;
  private static final int SERVER_ERROR = 500;

  private static final List<Integer> CREATE_APPLICATION_ERROR_RESPONSE =
      Arrays.asList(400, 409, 412, 500);

  private static final String DEFAULT_API_URL = "https://app.contrastsecurity.com/Contrast/api";
  private static final String LOCALHOST_API_URL = "http://localhost:19080/Contrast/api";
  private static final String DEFAULT_AGENT_PROFILE = "default";
}
