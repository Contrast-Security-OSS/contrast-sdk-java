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
import com.contrastsecurity.http.*;
import com.contrastsecurity.models.*;
import com.contrastsecurity.utils.ContrastSDKUtils;
import com.contrastsecurity.utils.UrlConstants;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.*;

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
    private Proxy proxy;

    /**
     * Default connection timeout. If connection timeout its set to this value, custom timeout will be ignored and requests will take
     * the default value that its usually assigned to them.
     */
    private final int DEFAULT_CONNECTION_TIMEOUT = -1;
    /**
     * Default read timeout. If read timeout its set to this value, custom timeout will be ignored and requests will take
     * default value that its usually assigned to them.
     */
    private final int DEFAULT_READ_TIMEOUT = -1;

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
     * @param proxy      Proxy to use
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
     *
     * @param user       Username (e.g., joe@acme.com)
     * @param serviceKey User service key
     * @param apiKey     API Key
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
            is = makeRequest(HttpMethod.GET, UrlConstants.PROFILE_ORGANIZATIONS, null);
            reader = new InputStreamReader(is);

            return this.gson.fromJson(reader, Organizations.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get all users for an organization.
     *
     * @param organizationId the ID of the organization
     * @return A List of User Objects.
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Users getOrganizationUsers(String organizationId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getOrganizationUsersUrl(organizationId), null);
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
            is = makeRequest(HttpMethod.GET, UrlConstants.PROFILE_DEFAULT_ORGANIZATION, null);
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
            is = makeRequest(HttpMethod.GET, this.urlBuilder.getApplicationUrl(organizationId,
                    appId, expandValues), null);
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
            is = makeRequest(HttpMethod.GET, urlBuilder.getApplicationsUrl(organizationId), null);
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
            is = makeRequest(HttpMethod.GET, urlBuilder.getCoverageUrl(organizationId, appId), null);
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
            is = makeRequest(HttpMethod.GET, urlBuilder.getLibrariesUrl(organizationId, appId, expandValues), null);
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
            is = makeRequest(HttpMethod.GET, urlBuilder.getServersUrl(organizationId, filterForm), null);
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
            is = makeRequest(HttpMethod.GET, urlBuilder.getServersFilterUrl(organizationId, filterForm), null);
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
            is = makeRequest(HttpMethod.GET, urlBuilder.getTracesByApplicationUrl(organizationId, appId, form), null);
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
     * @param form           FilterForm query parameters
     * @return Traces object that contains the list of Trace's
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Traces getTracesInOrg(String organizationId, TraceFilterForm form) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getTracesByOrganizationUrl(organizationId, form), null);
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
        return getApplicationTraceFiltersByType(organizationId, appId, TraceFilterType.VULNTYPE.toString());
    }

    public TraceListing getApplicationTraceFiltersByType(final String orgUuid, final String appId, final String filterType) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            final String filtersUrl = String.format(UrlConstants.APPLICATION_TRACE_FILTERS, orgUuid, appId, filterType);
            is = makeRequest(HttpMethod.GET, filtersUrl, null);

            reader = new InputStreamReader(is);
            return gson.fromJson(reader, TraceListing.class);
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
            is = makeRequest(HttpMethod.GET, urlBuilder.getTracesWithFilterUrl(organizationId, appId, traceFilterType, keycode, form), null);
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
            is = makeRequest(HttpMethod.GET, urlBuilder.getTracesByApplicationUrl(organizationId, appId, ruleNameForm), null);
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
            is = makeRequest(HttpMethod.GET, urlBuilder.getRules(organizationId), null);
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
     * @throws IOException           if there was a communication problem
     * @throws UnauthorizedException if authentication fails
     */
    public byte[] getAgent(AgentType type, String organizationId, String profileName) throws IOException, UnauthorizedException {
        InputStream is = null;
        try {
            is = makeRequest(HttpMethod.GET, urlBuilder.getAgentUrl(type, organizationId, profileName), null);
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
     * @throws IOException           if there was a communication problem
     * @throws UnauthorizedException if authentication fails
     */
    public byte[] getAgent(AgentType type, String organizationId) throws IOException, UnauthorizedException {
        return getAgent(type, organizationId, DEFAULT_AGENT_PROFILE);
    }

    public EventSummaryResource getEventSummary(String orgUuid, String traceId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            String eventSummaryUrl = String.format(UrlConstants.EVENT_SUMMARY, orgUuid, traceId);
            is = makeRequest(HttpMethod.GET, eventSummaryUrl, null);
            reader = new InputStreamReader(is);
            EventSummaryResource resource = gson.fromJson(reader, EventSummaryResource.class);
            for (EventResource event : resource.getEvents()) {
                if (event.getCollapsedEvents() != null && !event.getCollapsedEvents().isEmpty()) {
                    getCollapsedEventsDetails(event, orgUuid, traceId);
                } else {
                    EventDetails eventDetails = getEventDetails(orgUuid, traceId, event);
                    event.setEvent(eventDetails.getEvent());
                }
            }
            return resource;
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    private void getCollapsedEventsDetails(EventResource parentEvent, final String orgUuid, final String traceId) throws IOException, UnauthorizedException {
        for (EventResource event : parentEvent.getCollapsedEvents()) {
            EventDetails eventDetails = getEventDetails(orgUuid, traceId, event);
            event.setEvent(eventDetails.getEvent());
            event.setParent(parentEvent);
        }
    }

    private EventDetails getEventDetails(String orgUuid, String traceId, EventResource event)
            throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            String eventDetailsUrl = String.format(UrlConstants.EVENT_DETAILS, orgUuid, traceId, event.getId());
            is = makeRequest(HttpMethod.GET, eventDetailsUrl, null);

            reader = new InputStreamReader(is);
            return gson.fromJson(reader, EventDetails.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    public HttpRequestResource getHttpRequest(String orgUuid, String traceId)
            throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            String httpRequestUrl = String.format(UrlConstants.HTTP_REQUEST, orgUuid, traceId);
            is = makeRequest(HttpMethod.GET, httpRequestUrl, null);

            reader = new InputStreamReader(is);
            return gson.fromJson(reader, HttpRequestResource.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    public StoryResource getStory(String orgUuid, String traceId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            String traceUrl = String.format(UrlConstants.TRACE, orgUuid, traceId);
            is = makeRequest(HttpMethod.GET, traceUrl, null);

            reader = new InputStreamReader(is);

            String inputString = IOUtils.toString(is, "UTF-8");
            StoryResource story = this.gson.fromJson(inputString, StoryResource.class);
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
                        Set<Map.Entry<String, JsonElement>> entries = properties.entrySet();
                        Iterator<Map.Entry<String, JsonElement>> iter = entries.iterator();
                        List<PropertyResource> propertyResources = new ArrayList<>();
                        chapter.setPropertyResources(propertyResources);
                        while (iter.hasNext()) {
                            Map.Entry<String, JsonElement> prop = iter.next();
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
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    public RecommendationResource getRecommendation(String orgUuid, String traceId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            String recommendationUrl = String.format(UrlConstants.RECOMMENDATION, orgUuid, traceId);
            is = makeRequest(HttpMethod.GET, recommendationUrl, null);

            reader = new InputStreamReader(is);
            return gson.fromJson(reader, RecommendationResource.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    public TagsResource getTagsByOrg(String orgUuid) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            String tagsUrl = String.format(UrlConstants.ORG_TAGS, orgUuid);
            is = makeRequest(HttpMethod.GET, tagsUrl, null);

            reader = new InputStreamReader(is);
            return gson.fromJson(reader, TagsResource.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    public TagsResource getTagsByTrace(String orgUuid, String traceId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            String tagsUrl = String.format(UrlConstants.TRACE_TAGS, orgUuid, traceId);
            is = makeRequest(HttpMethod.GET, tagsUrl, null);

            reader = new InputStreamReader(is);
            return gson.fromJson(reader, TagsResource.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    public BaseResponse putTags(String orgUuid, TagsServersResource tagsServersResource) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            String tagsUrl = String.format(UrlConstants.ORG_TAGS, orgUuid);
            is = makeRequest(HttpMethod.PUT, tagsUrl, tagsServersResource);

            reader = new InputStreamReader(is);
            return gson.fromJson(reader, BaseResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    public TagsResource deleteTag(String orgUuid, String traceId, String tag) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        TagRequest tagRequest = new TagRequest(tag);
        try {
            String tagsUrl = String.format(UrlConstants.TRACE_TAGS_DELETE, orgUuid, traceId);
            is = makeRequest(HttpMethod.DELETE, tagsUrl, tagRequest);

            reader = new InputStreamReader(is);
            return gson.fromJson(reader, TagsResource.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    public BaseResponse putStatus(String orgUuid, StatusRequest statusRequest) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            String statusUrl = String.format(UrlConstants.STATUS, orgUuid);
            is = makeRequest(HttpMethod.PUT, statusUrl, statusRequest);

            reader = new InputStreamReader(is);
            return gson.fromJson(reader, BaseResponse.class);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Sets a custom connection timeout for all SDK requests. This value must be set before a call to makeConnection is done.
     *
     * @param timeout Timeout value in milliseconds.
     */
    public void setConnectionTimeout(final int timeout) {
        this.connectionTimeout = timeout;
    }

    /**
     * Set a custom read timeout for all SDK requests. This value must be set before calling makeConnection method in order
     * to take effect.
     *
     * @param timeout TImeout value in milliseconds
     */
    public void setReadTimeout(final int timeout) {
        this.readTimeout = timeout;
    }

    public InputStream makeRequest(HttpMethod method, String path, Object body) throws IOException, UnauthorizedException {
        String url = restApiURL + path;
        HttpURLConnection connection = makeConnection(url, method.toString(), body);

        InputStream is = connection.getInputStream();
        int rc = connection.getResponseCode();
        if (rc >= BAD_REQUEST && rc < SERVER_ERROR) {
            IOUtils.closeQuietly(is);
            throw new UnauthorizedException(rc);
        }
        return is;
    }

    public HttpURLConnection makeConnection(String url, String method, Object body) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(proxy);
        connection.setRequestMethod(method);
        connection.setRequestProperty(RequestConstants.AUTHORIZATION, ContrastSDKUtils.makeAuthorizationToken(user, serviceKey));
        connection.setRequestProperty(RequestConstants.API_KEY, apiKey);
        connection.setUseCaches(false);

        if (connectionTimeout > DEFAULT_CONNECTION_TIMEOUT)
            connection.setConnectTimeout(connectionTimeout);
        if (readTimeout > DEFAULT_READ_TIMEOUT)
            connection.setReadTimeout(readTimeout);

        if (body != null) {
            Gson gson = new Gson();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(gson.toJson(body));
            osw.flush();
            osw.close();
            os.close();
        }

        return connection;
    }

    private static final int BAD_REQUEST = 400;
    private static final int SERVER_ERROR = 500;

    public static final String DEFAULT_API_URL = "https://app.contrastsecurity.com/Contrast/api";
    public static final String LOCALHOST_API_URL = "http://localhost:19080/Contrast/api";
    public static final String DEFAULT_AGENT_PROFILE = "default";
}
