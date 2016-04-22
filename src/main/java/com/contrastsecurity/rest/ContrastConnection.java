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
package com.contrastsecurity.rest;

import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.models.*;
import com.contrastsecurity.sdk.http.RequestConstants;
import com.contrastsecurity.sdk.http.UrlBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.sun.deploy.net.HttpRequest.CONTENT_LENGTH;

/**
 * Entry point for using the Contrast REST API. Make an instance of this class
 * and call methods. Easy!
 */
public class ContrastConnection {

    private String apiKey;
    private String serviceKey;
    private String user;
    private String restApiURL;
    private UrlBuilder urlBuilder;

    /**
     * Create a ContrastConnection object that will attempt to use the Contrast installation
     * in the
     *
     * @param user       Username (e.g., joe@acme.com)
     * @param serviceKey User service key
     * @param apiKey     API Key
     * @param restApiURL the base Contrast API URL
     * @throws IllegalArgumentException if the API URL is malformed
     */
    public ContrastConnection(String user, String serviceKey, String apiKey, String restApiURL) throws IllegalArgumentException {
        this.user = user;
        this.serviceKey = serviceKey;
        this.apiKey = apiKey;
        this.restApiURL = restApiURL;

        validateUrl();

        this.urlBuilder = UrlBuilder.getInstance();
    }

    /**
     * Create a ContrastConnection object that attempts to use the Contrast SaaS service.
     */
    public ContrastConnection(String user, String serviceKey, String apiKey) {
        this.user = user;
        this.serviceKey = serviceKey;
        this.apiKey = apiKey;
        this.restApiURL = DEFAULT_API_URL;

        validateUrl();

        this.urlBuilder = UrlBuilder.getInstance();
    }

    /**
     * Get summary information about a single app.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @return an ApplicationSummary representing the object whose ID was passed in
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Application getApplication(String organizationId, String appId) throws IOException, UnauthorizedException, ResourceNotFoundException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeSimpleRequest(GET_REQUEST, this.urlBuilder.getSingleApplicationUrl(organizationId, appId));
            reader = new InputStreamReader(is);

            return new Gson().fromJson(reader, Application.class);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get the list of applications being monitored by Contrast.
     *
     * @param organizationId the ID of the organization
     * @return a List of Application objects that are being monitored
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public List<Application> getApplications(String organizationId) throws UnauthorizedException, IOException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = makeSimpleRequest(GET_REQUEST, urlBuilder.getApplicationsUrl(organizationId));
            reader = new InputStreamReader(is);

            Type type = new TypeToken<List<Application>>() {}.getType();

            return new Gson().fromJson(reader, type);
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
     * @return a List of Library objects for the given app
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public Coverage getCoverage(String organizationId, String appId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            Type urisType = new TypeToken<List<URIEntry>>() {
            }.getType();
            is = makeSimpleRequest(GET_REQUEST, urlBuilder.getCoverageUrl(organizationId, appId));
            reader = new InputStreamReader(is);
            Coverage coverage = new Coverage();
            coverage.setUris(new Gson().fromJson(reader, urisType));
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);

            return coverage;
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Return the libraries of the monitored Contrast application.
     *
     * @param organizationId the ID of the organization
     * @param appId          the ID of the application
     * @return a List of Library objects for the given app
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public List<Library> getLibraries(String organizationId, String appId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            Type libsType = new TypeToken<List<Library>>() {
            }.getType();
            is = makeSimpleRequest(GET_REQUEST, urlBuilder.getLibrariesUrl(organizationId, appId));
            reader = new InputStreamReader(is);

            return new Gson().fromJson(reader, libsType);
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
     * @return a List of Trace objects representing the vulnerabilities
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public List<Trace> getTraces(String organizationId, String appId) throws IOException, UnauthorizedException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            Type libsType = new TypeToken<List<Trace>>() {
            }.getType();
            is = makeSimpleRequest(GET_REQUEST, urlBuilder.getTracesUrl(organizationId, appId));
            reader = new InputStreamReader(is);

            return new Gson().fromJson(reader, libsType);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * @param appId      the ID of the application
     * @param conditions a name=value pair querystring of trace conditions
     * @return the HTTP response code of the given query
     * @throws UnauthorizedException if the Contrast account failed to authorize
     * @throws IOException           if there was a communication problem
     */
    public int checkForTrace(String appId, String conditions) throws IOException, UnauthorizedException {
        HttpURLConnection connection = makeConnection("/s/traces/exists", POST_REQUEST);
        connection.setRequestProperty(RequestConstants.APPLICATION, appId);
        connection.setRequestProperty(CONTENT_LENGTH, Integer.toString(conditions.getBytes().length));
        connection.setDoOutput(true);

        InputStream is = null;
        OutputStream os = null;
        try {
            is = connection.getInputStream();
            os = connection.getOutputStream();
            os.write(conditions.getBytes());

            List<String> lines = IOUtils.readLines(is, CharEncoding.UTF_8);
            if (lines == null || lines.size() != 1) {
                throw new IOException("Issue reading lines: " + (lines != null ? lines.size() : "null"));
            }
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }

        int rc = connection.getResponseCode();
        if (rc >= BAD_REQUEST && rc < SERVER_ERROR) {
            throw new UnauthorizedException(rc);
        }
        return rc;
    }

    /**
     * Download a contrast.jar agent associated with this account. The user should save
     * this byte array to a file named 'contrast.jar'. This signature takes a parameter
     * which contains the name of the saved engine profile to download.
     *
     * @param profileName    the name of the saved engine profile to download,
     * @param organizationId the ID of the organization,
     * @return a byte[] array of the contrast.jar file contents, which the user should
     * @throws IOException if there was a communication problem
     */
    public byte[] getAgent(AgentType type, String organizationId, String profileName) throws IOException {
        String url = restApiURL;

        if (AgentType.JAVA.equals(type)) {
            url += String.format(ENGINE_JAVA_URL, organizationId, profileName);
        } else if (AgentType.DOTNET.equals(type)) {
            url += String.format(ENGINE_DOTNET_URL, organizationId, profileName);
        }
        HttpURLConnection connection = makeConnection(url, GET_REQUEST);
        InputStream is = null;
        try {
            is = connection.getInputStream();

            return IOUtils.toByteArray(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Download a Contrast agent associated with this account and the platform passed in.
     */
    public byte[] getAgent(AgentType type, String organizationId) throws IOException {
        return getAgent(type, organizationId, DEFAULT_AGENT_PROFILE);
    }

    private InputStream makeSimpleRequest(String method, String path) throws MalformedURLException, IOException, UnauthorizedException {
        String url = restApiURL + path;
        HttpURLConnection connection = makeConnection(url, method);
        InputStream is = connection.getInputStream();
        int rc = connection.getResponseCode();
        if (rc >= BAD_REQUEST && rc < SERVER_ERROR) {
            IOUtils.closeQuietly(is);
            throw new UnauthorizedException(rc);
        }
        return is;
    }

    private HttpURLConnection makeConnection(String url, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty(RequestConstants.AUTHORIZATION, makeAuthorizationToken());
        connection.setRequestProperty(RequestConstants.API_KEY, apiKey);
        connection.setUseCaches(false);
        return connection;
    }

    private String makeAuthorizationToken() throws IOException {
        String token = user + ":" + serviceKey;
        return Base64.encodeBase64String(token.getBytes(CharEncoding.US_ASCII)).trim(); // "ASCII"
    }

    private void validateUrl() throws IllegalArgumentException {
        URL u;
        try {
            u = new URL(restApiURL);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
        if (!u.getProtocol().startsWith("http")) {
            throw new IllegalArgumentException("Invalid protocol");
        }
    }

    public static void main(String[] args) throws UnauthorizedException, IOException {
        ContrastConnection conn = new ContrastConnection("contrast_admin", "demo", "demo", LOCALHOST_API_URL);

        List<Application> apps = conn.getApplications("a64b1f3e-a0b5-4e8b-900f-6ae263711d6d");

        for (Application app : apps) {
            System.out.println(app.getName() + " / " + app.getLanguage());
        }

        Gson gson = new Gson();
        System.out.println(gson.toJson(apps));

        //System.out.println(gson.toJson(conn.getCoverage("a64b1f3e-a0b5-4e8b-900f-6ae263711d6d", "aecfb08d-dbff-4665-b4ff-43930bffc81a")));
        //System.out.println(gson.toJson(conn.getTraces("a64b1f3e-a0b5-4e8b-900f-6ae263711d6d", "aecfb08d-dbff-4665-b4ff-43930bffc81a")));
        //System.out.println(gson.toJson(conn.getLibraries("a64b1f3e-a0b5-4e8b-900f-6ae263711d6d", "aecfb08d-dbff-4665-b4ff-43930bffc81a")));
        //System.out.println(gson.toJson(conn.getAgent(AgentType.JAVA, "a64b1f3e-a0b5-4e8b-900f-6ae263711d6d")));
        //System.out.println(gson.toJson(conn.getAgent(AgentType.DOTNET, "a64b1f3e-a0b5-4e8b-900f-6ae263711d6d")));
    }


    private static final String GET_REQUEST = "GET";
    private static final String POST_REQUEST = "POST";

    private static final int BAD_REQUEST = 400;
    private static final int SERVER_ERROR = 500;

    private static final String ENGINE_JAVA_URL = "/%s/engine/%s/java/";
    private static final String ENGINE_DOTNET_URL = "/%s/engine/%s/.net/";
    private static final String TRACES_URL = "/traces";
    private static final String APPLICATIONS_URL = "/applications";
    private static final String DEFAULT_API_URL = "https://app.contrastsecurity.com/Contrast/api";
    private static final String LOCALHOST_API_URL = "http://localhost:19080/Contrast/api";
    private static final String DEFAULT_AGENT_PROFILE = "default";
}
