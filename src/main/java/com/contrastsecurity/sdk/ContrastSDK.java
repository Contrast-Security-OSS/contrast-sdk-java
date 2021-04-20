/*
 * Copyright (c) 2015, Contrast Security, LLC.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * Neither the name of the Contrast Security, LLC. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.contrastsecurity.sdk;

import com.contrastsecurity.exceptions.ApplicationCreateException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.http.*;
import com.contrastsecurity.models.*;
import com.contrastsecurity.models.dtm.ApplicationCreateRequest;
import com.contrastsecurity.models.dtm.AttestationCreateRequest;
import com.contrastsecurity.utils.ContrastSDKUtils;
import com.contrastsecurity.utils.MetadataDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Entry point for using the Contrast REST API. Make an instance of this class
 * and call methods. Easy!
 */
public class ContrastSDK {

    private String apiKey;
    private String serviceKey;
    private String user;
    private String restApiURL;
    private UrlBuilder urlBuilder;
    private Gson gson;
    Proxy proxy;
    private String integrationName;
    private String version;

    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;
    private static final int BUFFER_SIZE = 4096;

    public static class Builder {
        private String user;
        private String serviceKey;
        private String apiKey;
        private Proxy proxy;
        private String restApiURL;
        private String integrationName;
        private String version;

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

        public Builder withIntegrationName(String integrationName){
            this.integrationName = String.valueOf(IntegrationName.valueOf(integrationName));
            return this;
        }

        public Builder withVersion(String version){
            this.version = version;
            return this;
        }

        public ContrastSDK build() {
            ContrastSDK sdk = new ContrastSDK(this.user, this.serviceKey, this.apiKey);
            sdk.restApiURL = this.restApiURL;
            sdk.proxy = this.proxy;
            sdk.integrationName = this.integrationName;
            sdk.version = this.version;
            return sdk;
        }
    }

    /**
     * Use ContrastSDK.Builder
     */
    @Deprecated
    public ContrastSDK() {

    }

    /**
     * Create a ContrastSDK object to use the Contrast V3 API
     * Deprecated - Please use builder
     * @param user       Username (e.g., joe@acme.com)
     * @param serviceKey User service key
     * @param apiKey     API Key
     * @param restApiURL the base Contrast API URL
     * @throws IllegalArgumentException if the API URL is malformed
     */
    @Deprecated
    public ContrastSDK(String user, String serviceKey, String apiKey, String restApiURL) throws IllegalArgumentException {
        this.user = user;
        this.serviceKey = serviceKey;
        this.apiKey = apiKey;
        this.restApiURL = restApiURL;

        ContrastSDKUtils.validateUrl(this.restApiURL);
        this.restApiURL = ContrastSDKUtils.ensureApi(this.restApiURL);
        this.urlBuilder = UrlBuilder.getInstance();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(MetadataEntity.class, new MetadataDeserializer()).create();
        this.proxy = Proxy.NO_PROXY;
    }

    /**
     * Create a ContrastSDK object to use the Contrast V3 API through a Proxy.
     * Deprecated - Please use builder
     * @param user       Username (e.g., joe@acme.com)
     * @param serviceKey User service key
     * @param apiKey     API Key
     * @param restApiURL the base Contrast API URL
     * @param proxy Proxy to use
     * @throws IllegalArgumentException if the API URL is malformed
     */
    @Deprecated
    public ContrastSDK(String user, String serviceKey, String apiKey, String restApiURL, Proxy proxy) throws IllegalArgumentException {
        this.user = user;
        this.serviceKey = serviceKey;
        this.apiKey = apiKey;
        this.restApiURL = restApiURL;

        ContrastSDKUtils.validateUrl(this.restApiURL);
        this.restApiURL = ContrastSDKUtils.ensureApi(this.restApiURL);

        this.urlBuilder = UrlBuilder.getInstance();
        this.gson = new Gson();
        this.proxy = proxy;
    }

    /**
     * Create a ContrastSDK object to use the Contrast V3 API
     * Deprecated - Please use Builder
     * <p>
     * This will use the default api url which is https://app.contrastsecurity.com/Contrast/api
     * @param user Username (e.g., joe@acme.com)
     * @param serviceKey User service key
     * @param apiKey API Key
     */
    @Deprecated
    public ContrastSDK(String user, String serviceKey, String apiKey) {
        this.user = user;
        this.serviceKey = serviceKey;
        this.apiKey = apiKey;
        this.restApiURL = DEFAULT_API_URL;
        ContrastSDKUtils.validateUrl(this.restApiURL);
        this.urlBuilder = UrlBuilder.getInstance();
        this.gson = new Gson();
        this.proxy = Proxy.NO_PROXY;
    }

    /**
     * Get all Assess Licensing for an Organizations.
     * @param organizationId the ID of the organization
     * @return AssessLicenseOverview with Assess Licensing for an Oeg.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public AssessLicenseOverview getAssessLicensing(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getAssessLicensingUrl(organizationId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, AssessLicenseOverview.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }
    /**
     * Get Total (Total Open and Total Closed each month) Vulnerability Trend for an Organizations.
     * @param organizationId the ID of the organization
     * @return VulnerabilityTrend with the yearly Vulnerability Trend for an Oeg.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public VulnerabilityTrend getYearlyVulnTrend(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getYearlyVulnTrendUrl(organizationId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, VulnerabilityTrend.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get New (New Open and New Closed each month) Vulnerability Trend for an Organizations.
     * @param organizationId the ID of the organization
     * @return VulnerabilityTrend with the yearly Vulnerability Trend for an Oeg.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public VulnerabilityTrend getYearlyNewVulnTrend(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getYearlyNewVulnTrendUrl(organizationId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, VulnerabilityTrend.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get all Vulnerability Trend for an Application.
     * @param organizationId the ID of the organization
     * @param appId the ID of the application
     * @return VulnerabilityTrend with the yearly Vulnerability Trend for an Oeg.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public VulnerabilityTrend getYearlyVulnTrendForApplication(String organizationId, String appId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getYearlyVulnTrendForApplicationUrl(organizationId, appId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, VulnerabilityTrend.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get all organizations for the user profile.
     *
     * @return Organization objects with a list of disabled and valid organizations for the user.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Organizations getProfileOrganizations() throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getProfileOrganizationsUrl());
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Organizations.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get all users for an organization.
     * @param organizationId the ID of the organization
     * @return A List of User Objects.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Users getOrganizationUsers(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getOrganizationUsersUrl(organizationId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Users.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get the default organization for the user profile.
     *
     * @return Organization object with the default Organizaiton.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Organizations getProfileDefaultOrganizations() throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getProfileDefaultOrganizationUrl());
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Organizations.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Creates an application without a server that is meant to be instrumented later.
     * @param organizationId
     * @param request
     * @return
     * @throws IOException
     * @throws UnauthorizedException
     */
    public Application createApplication(String organizationId, ApplicationCreateRequest request)
            throws IOException, UnauthorizedException, ApplicationCreateException {
        try (InputStream is = makeCreateRequest(HttpMethod.POST, urlBuilder.getCreateApplicationUrl(organizationId), this.gson.toJson(request), MediaType.JSON,false);
             InputStreamReader reader = new InputStreamReader(is)){
            Applications response = this.gson.fromJson(reader, Applications.class);
            return response.getApplication();
        }
    }

    /**
     * Gets a single application based on the org, name, and language
     * @param orgId ID of the organization
     * @param appName Application name when the application was first created
     * @param language Language of the application
     * @return the Application found, returns null if the application is not found
     * @throws IOException
     * @throws UnauthorizedException
     */
    public Application getApplicationByNameAndLanguage(String orgId, String appName, AgentType language) throws IOException, UnauthorizedException{
        try (InputStream is = makeRequest(HttpMethod.GET, urlBuilder.getApplicationByNameAndLanguageUrl(orgId, appName, language.name()));
             InputStreamReader reader = new InputStreamReader(is)) {
            Applications response = this.gson.fromJson(reader, Applications.class);
            return response.getApplication();
        }
    }

    /**
     * Private helper method for createApplication to make a request with special error handling
     * @param method
     * @param path
     * @param body
     * @param mediaType
     * @return
     * @throws IOException
     * @throws UnauthorizedException
     * @throws ApplicationCreateException
     */
    private InputStream makeCreateRequest(HttpMethod method, String path, String body, MediaType mediaType, boolean setAcceptType) throws IOException, UnauthorizedException, ApplicationCreateException {
        String url = restApiURL + path;

        HttpURLConnection connection = makeConnection(url, method.toString());
        if(mediaType != null && body != null && (method.equals(HttpMethod.PUT) || method.equals(HttpMethod.POST))) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",mediaType.getType());
            if(setAcceptType){
                connection.setRequestProperty("Accept",mediaType.getType());
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
        } else if(rc >= BAD_REQUEST && rc < SERVER_ERROR) {
            throw new UnauthorizedException(rc);
        }
        is = connection.getInputStream();
        return is;
    }

    /**
     * Private helper method for extracting the messages from an errorstream
     * @param errorStream
     * @return
     * @throws IOException
     */
    private String getErrorMessage(InputStream errorStream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(errorStream);
        StringBuilder builder = new StringBuilder();
        try( BufferedReader bufferedReader = new BufferedReader(streamReader)) {
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
     * @param appId          the ID of the application
     * @return Applications object that contains one Application; wrapper
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Applications getApplication(String organizationId, String appId) throws IOException, UnauthorizedException {
        return getApplication(organizationId, appId, null);
    }

    /**
     * Get summary information about a single app.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @param expandValues   Expand values to filter on
     * @return Applications object that contains one Application; wrapper
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Applications getApplication(String organizationId, String appId, EnumSet<FilterForm.ApplicationExpandValues> expandValues) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getApplicationUrl(organizationId, appId, expandValues));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Applications.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get the list of applications being monitored by Contrast.
     *
     * @param organizationId the ID of the organization
     * @return Applications object that contains the list of Application's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Applications getApplications(String organizationId) throws UnauthorizedException, IOException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getApplicationsUrl(organizationId));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, Applications.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get the list of filtered applications being monitored by Contrast.
     *
     * @param organizationId the ID of the organization
     * @param applicationFilterForm  Query params to add more info to response
     * @return Applications object that contains the list of Application's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Applications getFilteredApplications(String organizationId, ApplicationFilterForm applicationFilterForm) throws UnauthorizedException, IOException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getApplicationFilterUrl(organizationId, applicationFilterForm));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, Applications.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get the list of licensed applications being monitored by Contrast.
     *
     * @param organizationId the ID of the organization
     * @return Applications object that contains the list of Application's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Applications getLicensedApplications(String organizationId) throws UnauthorizedException, IOException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getLicensedApplicationsUrl(organizationId));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, Applications.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    public Applications getApplicationsNames(String organizationId) throws UnauthorizedException, IOException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getApplicationsNameUrl(organizationId));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, Applications.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Return route coverage data about the monitored Contrast application.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @param metadata       option session metadata keys and values to return route coverage based on
     * @return RouteCoverage object for the given app
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public RouteCoverageResponse getRouteCoverage(String organizationId, String appId, RouteCoverageBySessionIDAndMetadataRequest metadata) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            if (metadata == null) {
                is = makeRequest(HttpMethod.GET, urlBuilder.getRouteCoverageUrl(organizationId, appId));
            } else {
                is = makeRequestWithBody(HttpMethod.POST, urlBuilder.getRouteCoverageWithMetadataUrl(organizationId, appId), this.gson.toJson(metadata), MediaType.JSON);
            }
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, RouteCoverageResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Return coverage data about the monitored Contrast application.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @return Coverage object for the given app
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Coverage getCoverage(String organizationId, String appId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getCoverageUrl(organizationId, appId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Coverage.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    public Libraries getLibraries(String organizationId, String appId) throws IOException, UnauthorizedException {
        return getLibraries(organizationId, appId, null);
    }

    /**
     * Return the libraries of the monitored Contrast application.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @param expandValues   Query params to add more info to response
     * @return Libraries object that contains the list of Library objects
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Libraries getLibraries(String organizationId, String appId, EnumSet<FilterForm.LibrariesExpandValues> expandValues) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getLibrariesUrl(organizationId, appId, expandValues));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Libraries.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Return the library Scores for an Organization.
     *
     * @param organizationId the ID of the organization
     * @return LibraryScores object that contains the Library scores for an Org
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public LibraryScores getLibraryScores(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getLibraryScoresUrl(organizationId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, LibraryScores.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }
    /**
     * Return the library Stats for an Organization.
     *
     * @param organizationId the ID of the organization
     * @return LibraryScores object that contains the Library stats for an Org
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public LibraryStats getLibraryStats(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getLibraryStatsUrl(organizationId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, LibraryStats.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Return the servers of the monitored Contrast application.
     *
     * @param organizationId the ID of the organization
     * @param filterForm     FilterForm query parameters
     * @return Servers object that contains the list of Library objects
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Servers getServers(String organizationId, ServerFilterForm filterForm) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getServersUrl(organizationId, filterForm));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Servers.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Return the servers of the monitored Contrast application.
     *
     * @param organizationId the ID of the organization
     * @param filterForm     FilterForm query parameters
     * @return Servers object that contains the list of Library objects
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Servers getServersWithFilter(String organizationId, ServerFilterForm filterForm) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getServersFilterUrl(organizationId, filterForm));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Servers.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Get the vulnerabilities in the application whose ID is passed in.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @param form           FilterForm query parameters
     * @return Traces object that contains the list of Trace's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Traces getTraces(String organizationId, String appId, TraceFilterForm form) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getTracesByApplicationUrl(organizationId, appId, form));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, Traces.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Get the vulnerabilities in the application whose ID is passed in.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @param form           FilterForm query parameters
     * @return TracesWithResponse object that contains the list of Trace's and the response code
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public TracesWithResponse getTracesWithResponse(String organizationId, String appId, TraceFilterForm form) throws IOException, UnauthorizedException {
        InputStreamReader reader = null;
        try {
            MakeRequestResponse mrr = makeRequestWithResponse(HttpMethod.GET, urlBuilder.getTracesByApplicationUrl(organizationId,appId,form));
            reader = new InputStreamReader(mrr.is);
            TracesWithResponse twr = new TracesWithResponse();
            twr.t = this.gson.fromJson(reader, Traces.class);
            twr.rc = mrr.rc;
            return twr;
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Get the notes (discussion) for the vulnerability ID in the application whose ID is passed in.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @param traceId        the ID of the vulnerability
     * @param form           FilterForm query parameters
     * @return Traces object that contains the list of Trace's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public TraceNotesResponse getNotes(String organizationId, String appId, String traceId, TraceFilterForm form) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getNotesByApplicationUrl(organizationId, appId, traceId, form));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, TraceNotesResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }


    /**
     * Get the available vulnerability tags in the application whose ID is passed in.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @return TagsResponse object that contains the list of Tags
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public TagsResponse getVulnTagsByApplication(String organizationId, String appId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getTraceTagsByApplicationUrl(organizationId, appId));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, TagsResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Get the available session metadata values in the application whose ID is passed in.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @param form           FilterForm query parameters
     * @return Traces object that contains the list of Trace's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public MetadataFilterResponse getSessionMetadataForApplication(String organizationId, String appId, TraceFilterForm form) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getSessionMetadataForApplicationUrl(organizationId, appId, form));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, MetadataFilterResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Generate an attestation report for the application whose ID is passed in.
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @param request
     * @throws IOException
     * @throws UnauthorizedException
     */
    public GenericResponse generateAttestationReport(String organizationId, String appId, AttestationCreateRequest request)
            throws IOException, UnauthorizedException, ApplicationCreateException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeCreateRequest(HttpMethod.POST, urlBuilder.getAttestationReportByApplicationUrl(organizationId, appId), this.gson.toJson(request), MediaType.JSON,true);
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, GenericResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }


    /**
     * Get the vulnerabilities in the organization whose ID is passed in.
     *
     * @param organizationId the ID of the organization
     * @param userId the id of the user who requested the report
     * @param reportId the id of the report that was generated
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public void downloadAttestationReport(String organizationId, String userId, String reportId) throws IOException, UnauthorizedException {
        downloadFile(HttpMethod.POST,urlBuilder.downloadAttestationReportUrl(organizationId,userId,reportId),".");
    }

    /**
     * Get notifications for the org.
     *
     * @param organizationId the ID of the organization
     * @param form FilterForm query parameters
     * @return Notifications object that contains the response from the API
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public NotificationsResponse getNotifications(String organizationId, TraceFilterForm form) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getNotificationsUrl(organizationId, form));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, NotificationsResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Get server tags for the org.
     *
     * @param organizationId the ID of the organization
     * @param appId the ID of the app
     * @return ServerTagsResponse object that contains the server tags
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public ServerTagsResponse getServerTags(String organizationId, String appId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getServerTagsUrl(organizationId, appId));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, ServerTagsResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Clear notifications for the org.
     *
     * @param organizationId the ID of the organization
     * @return GenericResponse object that contains the response from the server
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public GenericResponse clearNotifications(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.PUT, urlBuilder.clearNotificationsUrl(organizationId));
            reader = new InputStreamReader(is);
            return this.gson.fromJson(reader, GenericResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Get the vulnerabilities in the organization whose ID is passed in.
     *
     * @param organizationId the ID of the organization
     * @param form FilterForm query parameters
     * @return Traces object that contains the list of Trace's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Traces getTracesInOrg(String organizationId, TraceFilterForm form) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getTracesByOrganizationUrl(organizationId, form));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Traces.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Get the filters for the traces in the application.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @return TraceListing object that contains the trace filters for the application
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public TraceListing getTraceFilters(String organizationId, String appId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getTraceListingUrl(organizationId, appId, TraceFilterType.VULNTYPE));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, TraceListing.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }


    /**
     * Get the vulnerabilities in the application whose ID is passed in with a filter.
     *
     * @param organizationId  the ID of the organization
     * @param appId           the ID of the application
     * @param traceFilterType filter type
     * @param keycode         id or key to filter on
     * @param form            FilterForm query parameters
     * @return Traces object that contains the list of Trace's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Traces getTracesWithFilter(String organizationId, String appId, TraceFilterType traceFilterType, TraceFilterKeycode keycode, TraceFilterForm form) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;

        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getTracesWithFilterUrl(organizationId, appId, traceFilterType, keycode, form));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Traces.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Get the vulnerabilities in the application by the rule.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @param ruleNames      FilterForm query parameters
     * @return Traces object that contains the list of Trace's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    @Deprecated
    public Traces getTraceFilterByRule(String organizationId, String appId, List<String> ruleNames) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;

        TraceFilterForm ruleNameForm = new TraceFilterForm();
        ruleNameForm.setVulnTypes(ruleNames);

        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getTracesByApplicationUrl(organizationId, appId, ruleNameForm));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Traces.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
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
    public SecurityCheck makeSecurityCheck(String organizationId, SecurityCheckForm securityCheckForm) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequestWithBody(HttpMethod.POST, urlBuilder.getSecurityCheckUrl(organizationId), this.gson.toJson(securityCheckForm), MediaType.JSON);
            reader = new InputStreamReader(is);

            SecurityCheckResponse response = this.gson.fromJson(reader, SecurityCheckResponse.class);
            return response.getSecurityCheck();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Gets a list of enabled Job Outcome policies in an organization
     * @param organizationId The organization ID
     * @return The list of enabled Job Outcome Policies
     * @throws IOException
     * @throws UnauthorizedException
     */
    public List<JobOutcomePolicy> getEnabledJobOutcomePolicies(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getEnabledJobOutcomePolicyListUrl(organizationId));
            reader = new InputStreamReader(is);

            JobOutcomePolicyListResponse response = this.gson.fromJson(reader, JobOutcomePolicyListResponse.class);
            return response.getPolicies();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Gets a list of enabeld Job Outcome Policies in an organization that applies to an application
     * @param organizationId The organization ID
     * @param appId The Application ID
     * @return the list of enabled Job Outcome Policies that apply to the application
     */
    public List<JobOutcomePolicy> getEnabledJoboutcomePoliciesByApplication(String organizationId, String appId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getEnabledJobOutcomePolicyListUrlByApplication(organizationId, appId));
            reader = new InputStreamReader(is);

            JobOutcomePolicyListResponse response = this.gson.fromJson(reader, JobOutcomePolicyListResponse.class);
            return response.getPolicies();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Get the rules for an organization
     *
     * @param organizationId the ID of the organization
     * @return Traces object that contains the list of Trace's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Rules getRules(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;

        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getRules(organizationId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Rules.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Download a contrast.jar agent associated with this account. The user should save
     * this byte array to a file named 'contrast.jar'. This signature takes a parameter
     * which contains the name of the saved engine profile to download.
     *
     * @param type           the type of agent you want to download; Java, Java 1.5, .NET, or Node
     * @param profileName    the name of the saved engine profile to download,
     * @param organizationId the ID of the organization,
     * @return a byte[] array of the contrast.jar file contents, which the user should convert to a new File
     * @throws IOException if there was a communication problem
     * @throws UnauthorizedException if authentication fails
     */
    public byte[] getAgent(AgentType type, String organizationId, String profileName) throws IOException, UnauthorizedException {
        InputStream is = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getAgentUrl(type, organizationId, profileName));

            return IOUtils.toByteArray(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Download a contrast.jar agent associated with this account. The user should save
     * this byte array to a file named 'contrast.jar'. This signature takes a parameter
     * which contains the name of the saved engine profile to download.
     * <p>
     * This uses 'default' as the profile name.
     *
     * @param type           the type of agent you want to download; Java, Java 1.5, .NET, or Node
     * @param organizationId the ID of the organization,
     * @return a byte[] array of the contrast.jar file contents, which the user should convert to a new File
     * @throws IOException if there was a communication problem
     * @throws UnauthorizedException if authentication fails
     */
    public byte[] getAgent(AgentType type, String organizationId) throws IOException, UnauthorizedException {
        return getAgent(type, organizationId, DEFAULT_AGENT_PROFILE);
    }

    public InputStream makeRequestWithBody(HttpMethod method, String path, String body, MediaType mediaType) throws IOException, UnauthorizedException {
        String url = restApiURL + path;
        OutputStream os = null;
        HttpURLConnection connection = makeConnection(url, method.toString());
        if(mediaType != null && body != null && (method.equals(HttpMethod.PUT) || method.equals(HttpMethod.POST))) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",mediaType.getType());
            os = connection.getOutputStream();
            byte[] bodyByte = body.getBytes("utf-8");
            os.write(bodyByte, 0, bodyByte.length);
        }
        int rc = connection.getResponseCode();
        InputStream is = connection.getInputStream();
        if (rc >= BAD_REQUEST && rc < SERVER_ERROR) {
            IOUtils.closeQuietly(is);
            if(os != null) {
                IOUtils.closeQuietly(os);
            }
            throw new UnauthorizedException(rc);
        }
        return is;
    }

    public InputStream makeRequest(HttpMethod method, String path) throws IOException, UnauthorizedException {
        String url = restApiURL + path;

        HttpURLConnection connection = makeConnection(url, method.toString());
        InputStream is = connection.getInputStream();

        int rc = connection.getResponseCode();
        if (rc >= BAD_REQUEST && rc < SERVER_ERROR) {
            IOUtils.closeQuietly(is);
            throw new UnauthorizedException(rc);
        }
        return is;
    }

    public MakeRequestResponse makeRequestWithResponse(HttpMethod method, String path) throws IOException, UnauthorizedException {
        String url = restApiURL + path;

        HttpURLConnection connection = makeConnection(url, method.toString());
        InputStream is = connection.getInputStream();
        int rc = connection.getResponseCode();
        if (rc >= BAD_REQUEST && rc < SERVER_ERROR) {
            IOUtils.closeQuietly(is);
            throw new UnauthorizedException(rc);
        }
        MakeRequestResponse mrr = new MakeRequestResponse();
        mrr.is = is;
        mrr.rc = rc;
        return mrr;
    }

    public void downloadFile(HttpMethod method, String path, String saveDir) throws IOException, UnauthorizedException {
        String fileURL = restApiURL + path;

        //URL url = new URL(fileURL);
        //HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        HttpURLConnection connection = makeConnection(fileURL,method.toString());
        connection.setRequestProperty("accept", "application/json, text/plain, */*");
        connection.setRequestProperty("accept-encoding","gzip, deflate, br");
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
                    fileName = disposition.substring(index + 9,
                            disposition.length());
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
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
        connection.setRequestProperty(RequestConstants.AUTHORIZATION, ContrastSDKUtils.makeAuthorizationToken(user, serviceKey));
        connection.setRequestProperty(RequestConstants.API_KEY, apiKey);
        connection.setUseCaches(false);
        if(connectionTimeout > DEFAULT_CONNECTION_TIMEOUT)
            connection.setConnectTimeout(connectionTimeout);
        if(readTimeout > DEFAULT_READ_TIMEOUT)
            connection.setReadTimeout(readTimeout);
        return connection;
    }

    /**
     * Sets a custom connection timeout for all SDK requests. This value must be set before a call to makeConnection is done.
     * @param timeout Timeout value in milliseconds.
     */
    public void setConnectionTimeout(final int timeout) {
        this.connectionTimeout = timeout;
    }

    /**
     * Set a custom read timeout for all SDK requests. This value must be set before calling makeConnection method in order
     * to take effect.
     * @param timeout TImeout value in milliseconds
     */
    public void setReadTimeout(final int timeout) {
        this.readTimeout = timeout;
    }

    /**
     * Default connection timeout. If connection timeout its set to this value, custom timeout will be ignored and requests will take
     * the default value that its usually assigned to them.
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = -1;
    /**
     * Default read timeout. If read timeout its set to this value, custom timeout will be ignored and requests will take
     * default value that its usually assigned to them.
     */
    public static final int DEFAULT_READ_TIMEOUT = -1;

    private static final int BAD_REQUEST = 400;
    private static final int SERVER_ERROR = 500;

    private static final List<Integer> CREATE_APPLICATION_ERROR_RESPONSE = Arrays.asList(400,409,412,500);

    private static final String DEFAULT_API_URL = "https://app.contrastsecurity.com/Contrast/api";
    private static final String LOCALHOST_API_URL = "http://localhost:19080/Contrast/api";
    private static final String DEFAULT_AGENT_PROFILE = "default";
}