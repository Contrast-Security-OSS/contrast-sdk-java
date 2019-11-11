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

import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.http.FilterForm;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.RequestConstants;
import com.contrastsecurity.http.ServerFilterForm;
import com.contrastsecurity.http.TraceFilterForm;
import com.contrastsecurity.http.TraceFilterKeycode;
import com.contrastsecurity.http.TraceFilterType;
import com.contrastsecurity.http.UrlBuilder;
import com.contrastsecurity.models.*;
import com.contrastsecurity.utils.ContrastSDKUtils;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
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
    
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;

    public ContrastSDK() {

    }

    /**
     * Create a ContrastSDK object to use the Contrast V3 API
     *
     * @param user       Username (e.g., joe@acme.com)
     * @param serviceKey User service key
     * @param apiKey     API Key
     * @param restApiURL the base Contrast API URL
     * @throws IllegalArgumentException if the API URL is malformed
     */
    public ContrastSDK(String user, String serviceKey, String apiKey, String restApiURL) throws IllegalArgumentException {
        this.user = user;
        this.serviceKey = serviceKey;
        this.apiKey = apiKey;
        this.restApiURL = restApiURL;

        ContrastSDKUtils.validateUrl(this.restApiURL);
        this.restApiURL = ContrastSDKUtils.ensureApi(this.restApiURL);
        this.urlBuilder = UrlBuilder.getInstance();
        this.gson = new Gson();
        this.proxy = Proxy.NO_PROXY;
    }

    /**
     * Create a ContrastSDK object to use the Contrast V3 API through a Proxy.
     *
     * @param user       Username (e.g., joe@acme.com)
     * @param serviceKey User service key
     * @param apiKey     API Key
     * @param restApiURL the base Contrast API URL
     * @param proxy Proxy to use
     * @throws IllegalArgumentException if the API URL is malformed
     */
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
     * <p>
     * This will use the default api url which is https://app.contrastsecurity.com/Contrast/api
     * @param user Username (e.g., joe@acme.com)
     * @param serviceKey User service key
     * @param apiKey API Key
     */
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
     * Get all Vulnerability Trend for an Organizations.
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

    private static final String DEFAULT_API_URL = "https://app.contrastsecurity.com/Contrast/api";
    private static final String LOCALHOST_API_URL = "http://localhost:19080/Contrast/api";
    private static final String DEFAULT_AGENT_PROFILE = "default";
}
