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

import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.http.FilterForm;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.RequestConstants;
import com.contrastsecurity.http.ServerFilterForm;
import com.contrastsecurity.http.TraceFilterForm;
import com.contrastsecurity.http.TraceFilterKeycode;
import com.contrastsecurity.http.TraceFilterType;
import com.contrastsecurity.http.UrlBuilder;
import com.contrastsecurity.models.AgentType;
import com.contrastsecurity.models.Applications;
import com.contrastsecurity.models.Coverage;
import com.contrastsecurity.models.Libraries;
import com.contrastsecurity.models.Organizations;
import com.contrastsecurity.models.Rules;
import com.contrastsecurity.models.Servers;
import com.contrastsecurity.models.TraceListing;
import com.contrastsecurity.models.Traces;
import com.contrastsecurity.models.Groups;
import com.contrastsecurity.models.Users;
import com.contrastsecurity.models.GroupDetails;
import com.contrastsecurity.utils.ContrastSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
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
     * ddooley
     * Create a User in an organization.
     * Unfortunately can't reuse makeRequest without at least making changes there specific to new POST/PUT requests
     * ToDo capture commonality into new methods as progress
     * @param organizationId the ID of the organization
     * @param UserId the ID of the User
     * @param firstName the User's first name
     * @param lastName the User's last name
     * @param orgRole the ID of the Org Role for the User
     *                1 Admin
     *                2 Rules Admin
     *                3 Edit
     *                4 View
     * @param groupIds A list of group Ids. The User will be added to the respective groups
     *
     * @return Return HTTP Code.  200 is a success.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public int createUser(String organizationId, String UserId, String firstName, String lastName, long orgRole, ArrayList<Long> groupIds) throws IOException, UnauthorizedException {
        HttpURLConnection connection = null;
        try {
            String url = restApiURL + this.urlBuilder.createUsersUrl(organizationId);
            /* Create Connection */
            connection = (HttpURLConnection) new URL(url).openConnection(this.proxy);
            connection.setRequestMethod("POST");
            connection.setRequestProperty(RequestConstants.AUTHORIZATION, ContrastSDKUtils.makeAuthorizationToken(user, serviceKey));
            connection.setRequestProperty(RequestConstants.API_KEY, apiKey);
            connection.setUseCaches(false);
            if (connectionTimeout > DEFAULT_CONNECTION_TIMEOUT)
                connection.setConnectTimeout(connectionTimeout);
            if (readTimeout > DEFAULT_READ_TIMEOUT)
                connection.setReadTimeout(readTimeout);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            /* End Create  Connection */

            JsonObject body = buildCreateUserBody(UserId, firstName, lastName, orgRole, groupIds);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(body.toString());
            wr.flush();

            int rc = connection.getResponseCode();
            connection.disconnect();
            if (rc >= BAD_REQUEST && rc < SERVER_ERROR) {
                throw new UnauthorizedException(rc);
            }
            return rc;
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }
    /**
     * Create a Group in an organization.  Wrapper method.
     * @param organizationId the ID of the organization
     * @param name the name of the new group
     * @param role the role of the new group
     * @param users A list of usesrs (UUID) that will be added to the group. May be empty i.e. no UI "members" to add right now.
     *
     * @return Return HTTP Code.  200 is a success.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public int createGroup(String organizationId, String name, String role, ArrayList<String> users) throws IOException, UnauthorizedException {
        return this.touchGroup(organizationId, name, role, "POST", users, -1);
    }

    /**
     * Update a Group in an organization.  Wrapper method.
     * @param organizationId the ID of the organization
     * @param name the name of the new group
     * @param role the role of the new group
     * @param users A list of usesrs (UUID) that will be added to the group. May be empty i.e. no UI "members" to add right now.
     *
     * @return Return HTTP Code.  200 is a success.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public int updateGroup(String organizationId, String name, String role, ArrayList<String> users, int groupId) throws IOException, UnauthorizedException {
        return this.touchGroup(organizationId, name, role, "PUT", users, groupId);
    }

    /**
     * Create or Update a Group in an organization.
     * Unfortunately can't reuse makeRequest without at least making changes there specific to new POST/PUT requests (BODY required)
     * Can be used to update (PUT) or create (POST)
     * @param organizationId the ID of the organization
     * @param name the name of the new group
     * @param role the role of the new group
     * @param users A list of usesrs (UUID) that will be added to the group. May be empty i.e. no UI "members" to add right now.
     *
     * @return Return HTTP Code.  200 is a success.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public int touchGroup(String organizationId, String name, String role, String method, ArrayList<String> users, int groupID) throws IOException, UnauthorizedException {
        HttpURLConnection connection = null;
        try {
            String url = restApiURL + this.urlBuilder.createGroupsUrl(organizationId);
            if (groupID > 0) {
                // Update an existing Group
                url = url + "/" + Integer.toString(groupID);
            }
            /* Create Connection */
            connection = (HttpURLConnection) new URL(url).openConnection(this.proxy);
            connection.setRequestMethod(method);
            connection.setRequestProperty(RequestConstants.AUTHORIZATION, ContrastSDKUtils.makeAuthorizationToken(user, serviceKey));
            connection.setRequestProperty(RequestConstants.API_KEY, apiKey);
            connection.setUseCaches(false);
            if (connectionTimeout > DEFAULT_CONNECTION_TIMEOUT)
                connection.setConnectTimeout(connectionTimeout);
            if (readTimeout > DEFAULT_READ_TIMEOUT)
                connection.setReadTimeout(readTimeout);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            /* End Create Connection */

            JsonObject body = buildGroupBody(name, role, users);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(body.toString());
            wr.flush();

            int rc = connection.getResponseCode();
            connection.disconnect();
            if (rc >= BAD_REQUEST && rc < SERVER_ERROR) {
                throw new UnauthorizedException(rc);
            }
            return rc;
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }

    /**
     * Get all groups for an organization.
     * @param organizationId the ID of the organization
     * @return A List of Group Objects.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Groups getOrganizationGroups(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getOrganizationGroupsUrl(organizationId));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Groups.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get all groups details for a group.
     * @param organizationId the ID of the organization
     * @param groupID the ID of the group
     * @return A Group Detail Object.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public GroupDetails getOrganizationGroupDetails(String organizationId, long groupID) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getOrganizationGroupDetailsUrl(organizationId, groupID));
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, GroupDetails.class);
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

    public static void main(String[] args) throws UnauthorizedException, IOException, ResourceNotFoundException {
        ContrastSDK conn = new ContrastSDK("contrast_admin", "demo", "demo", LOCALHOST_API_URL, Proxy.NO_PROXY);

        String orgId = conn.getProfileDefaultOrganizations().getOrganization().getOrgUuid();
        String appId = "";

        Gson gson = new Gson();

        System.out.println(gson.toJson(conn.getProfileOrganizations()));

        // Examples
        // TraceFilterForm form = new TraceFilterForm();
        //form.setSeverities(EnumSet.of(RuleSeverity.LOW, RuleSeverity.MEDIUM, RuleSeverity.HIGH, RuleSeverity.CRITICAL));
        // form.setServerIds(Arrays.asList(1L));
        // System.out.println(gson.toJson(conn.getTraces(orgId, appId, form)));

        // System.out.println(gson.toJson(conn.getTraceFilters(orgId, appId)));
        // System.out.println(gson.toJson(conn.getServers(orgId, null)));
        // System.out.println(gson.toJson(conn.getRules(orgId)));
        // System.out.println(gson.toJson(conn.getApplication(orgId, appId, EnumSet.of(FilterForm.ApplicationExpandValues.SCORES, FilterForm.ApplicationExpandValues.TRACE_BREAKDOWN))));
        // System.out.println(gson.toJson(conn.getProfileDefaultOrganizations()));
        // System.out.println(gson.toJson(conn.getApplications(orgId)));
        // System.out.println(gson.toJson(conn.getCoverage(orgId, appId)));
        // System.out.println(gson.toJson(conn.getTraces(orgId, appId, null)));
        // System.out.println(gson.toJson(conn.getLibraries(orgId, appId)));
        // System.out.println(gson.toJson(conn.getTraceFilters(orgId, appId)));
        // FileUtils.writeByteArrayToFile(new File("contrast.jar"), conn.getAgent(AgentType.JAVA, orgId));
    }

    /**
     * Build JSON object for the body of User Creation POST requests.
     * @param UserId the ID of the User
     * @param firstName the User's first name
     * @param lastName the User's last name
     * @param orgRole the ID of the Org Role for the User
     *                1 Admin
     *                2 Rules Admin
     *                3 Edit
     *                4 View
     * @param groupIds A list of group Ids. The User will be added to the respective groups
     *
     * @return Success Code.
     */

    private JsonObject buildCreateUserBody(String UserId, String firstName, String lastName, long orgRole, ArrayList<Long> groupIds) {
        /* Build JSON Body */
        JsonObject body = new JsonObject();
        body.addProperty("api_only",false);
        body.addProperty("date_format","MM/dd/yyyy");
        body.addProperty("enabled", true);
        body.addProperty("first_name", firstName);
        JsonArray groups = new JsonArray();
        for (long l : groupIds) {
            groups.add(l);
        }
        body.add("groups", groups);
        body.addProperty("last_name", lastName);
        body.addProperty("protect", false);
        body.addProperty("role", orgRole);
        body.addProperty("time_format", "HH:mm");
        body.addProperty("time_zone", "America/Chicago");
        body.addProperty("username", UserId);
        return body;
    }

    /**
     * Build JSON object for the body of User Creation POST requests.
     * @param name the name of the Group
     * @param role the role of the group as it applies to Applications
     * @param users A list of users to add to the group.
     *
     * @return Success Code.
     */

    private JsonObject buildGroupBody(String name, String role, ArrayList<String> users) {
        /* Build JSON Body */
        JsonObject body = new JsonObject();
        body.addProperty("name", name);
        JsonArray userArray = new JsonArray();
        for (String user : users) {
            userArray.add(user);
        }
        body.add("users", userArray);
        // Add the Scope JSON Object
        JsonObject scope = new JsonObject();
        JsonObject appScope = new JsonObject();
        /*
         * The API changes depending on app or all onboarded selected
         * For now Customer O specific, but a bit obscure for others.  Still need to pull this out.
        */
        if (name.endsWith("_Analyst") || name.endsWith("_Developer")) {
            // This automatically onboards new applications
            appScope.addProperty("onboard_role", role);
        } else {
            appScope.addProperty("role", role);
        }
        JsonArray emptyArray = new JsonArray();
        appScope.add("exceptions", emptyArray);
        scope.add("app_scope", appScope);
        body.add("scope", scope);

        return body;
    }
    // ------------------------ Utilities -----------------------------------------------

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
