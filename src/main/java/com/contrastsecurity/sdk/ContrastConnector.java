/*
 * Copyright (c) 2013, Contrast Security, Inc.
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
 * Neither the name of the Contrast Security, Inc. nor the names of its contributors may
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

import com.contrastsecurity.sdk.application.App;
import com.contrastsecurity.sdk.application.AppStats;
import com.contrastsecurity.sdk.application.Apps;
import com.contrastsecurity.sdk.coverage.CoverageData;
import com.contrastsecurity.sdk.exception.InitializationException;
import com.contrastsecurity.sdk.http.ContrastRequest;
import com.contrastsecurity.sdk.traces.Findings;
import com.contrastsecurity.sdk.traces.Traces;
import com.contrastsecurity.sdk.traces.status.QueueStatus;
import com.contrastsecurity.sdk.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * ContrastConnector is the primary entry point for communicating with Contrast
 * TeamServer installations.
 * <p/>
 * Using a ContrastConnector you can retrieve stats from your TeamServer
 * instance as follows:
 * <p/>
 * <pre>
 *     ContrastConnector contrast = new ContrastConnector(contrastProperties);
 *
 * </pre>
 *
 * @author kdecker
 */
public class ContrastConnector {
    private static final Logger LOG = LoggerFactory.getLogger(ContrastConnector.class);

    private static final String VALID_EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    public static final String PROPS_USERNAME = "username";
    public static final String PROPS_SERVICEKEY = "servicekey";
    public static final String PROPS_APIKEY = "apikey";
    public static final String PROPS_HOSTURL = "hosturl";

    private JAXBContext xmlContext;
    private Unmarshaller xmlUnmarshaller;
    private String username;
    private String serviceKey;
    private String apiKey;

    private String errorString = "";
    private String contrastHost = "https://www.contrastsecurity.com";
    private boolean boolConnectionSuccess = false;

    /**
     * Instantiate ContrastConnector using a properties object.<br />
     * Required property values:<br />
     * username<br />servicekey<br />apikey<br />
     * Optional property value:<br />hosturl (If none specified, default value of:
     * www.contrastsecurity.com will be used.)<br />
     * <br />
     *
     * @param myProps
     */
    public ContrastConnector(Properties myProps) throws InitializationException {
        username = myProps.getProperty("username");
        serviceKey = myProps.getProperty("servicekey");
        apiKey = myProps.getProperty("apikey");

        if (myProps.containsKey("hosturl")) {
            if (!myProps.getProperty("hosturl").equals("") && myProps.getProperty("hosturl") != null) {
                contrastHost = myProps.getProperty("hosturl");
            }
        }
        initJAXB();
    }

    /**
     * Instantiate ContrastConnector using individual String values (No custom host)<br />
     * All parameters are required to be non-null.
     *
     * @param username   Plain-text Username
     * @param serviceKey Plain-text Service Key
     * @param apiKey     Plain-text API Key
     */
    public ContrastConnector(String username, String serviceKey, String apiKey) throws InitializationException {
        this.username = username;
        this.serviceKey = serviceKey;
        this.apiKey = apiKey;

        initJAXB();
    }

    /**
     * Instantiate ContrastConnector using individual String values and provide a custom host.<br />
     * Parameters username, serviceKey, and apiKey are required.<br />
     * If @param host is null, default value of app.contrastsecurity.com will be used.
     *
     * @param username   Plain-text Username
     * @param serviceKey Plain-text Service Key
     * @param apiKey     Plain-text API Key
     * @param host       Plain-text Host Address (including HTTP:// or HTTPS://)
     */
    public ContrastConnector(String username, String serviceKey, String apiKey, String host) throws InitializationException {
        this.username = username;
        this.serviceKey = serviceKey;
        this.apiKey = apiKey;

        if (host != null && !host.equals("") && !host.equals(contrastHost)) {
            contrastHost = host;
        }

        initJAXB();
    }

    /**
     * Initialize the JAXB Context & Unmarshaller.
     */
    private void initJAXB() throws InitializationException {
        try {
            xmlContext = JAXBContext.newInstance(
                    Apps.class,
                    Traces.class,
                    Findings.class,
                    AppStats.class,
                    CoverageData.class
            );
            xmlUnmarshaller = xmlContext.createUnmarshaller();
        } catch (Exception e) {
            LOG.error("Unable to initialize JAXB Unmarshaller", e);
            throw new InitializationException("Unable to initialize JAXB Unmarshaller", e);
        }
    }

    /**
     * Internal method used to validate that the supplied user credentials are populated.
     *
     * @return boolean True if all required credentials are populated and valid, false if not,
     *         use getErrorString() to get further details of a return value of false.
     */
    private boolean checkCredentials() {
        if (StringUtil.anyNullOrEmpty(username, serviceKey, apiKey)) {
            errorString = "One of the required credentials is not populated";
            return false;
        } else {
            //Username is ASCII email address (Can contain +)
            if (!Charset.forName("UTF-8").newEncoder().canEncode(username)) {
                errorString = "Username must be valid UTF-8 characters only.";
                return false;
            }

            //ServiceKey&APIKey can be any ascii characters
            if (!Charset.forName("US-ASCII").newEncoder().canEncode(serviceKey)) {
                errorString = "Service Key must be valid US-ASCII characters only.";
                return false;
            }

            if (!Charset.forName("US-ASCII").newEncoder().canEncode(apiKey)) {
                errorString = "API Key must be valid US-ASCII characters only.";
                return false;
            }

            return true;
        }
    }

    /**
     * Returns the Status Line of a failed connection.
     *
     * @return String value of the HTTP Response Status Line
     */
    public String getErrorString() {
        return errorString;
    }

    /**
     * Returns the String used to validate emails against.
     *
     * @return String
     */
    public String getValidEmailRegex() {
        return VALID_EMAIL_REGEX;
    }

    /**
     * Query Contrast API to obtain the Application listing for the specified user account.
     *
     * @return com.aspectsecurity.contrast.javalib.obj.ContrastAppList A class containing an
     *         ArrayList of ContrastAppData objects - Trace Listing & Application Statistics
     *         not populated. A return of null indicates an error. Use getErrorString() to
     *         retrieve a String error message;
     */
    public Apps getAppList() {
        return sendRequest(ContrastRequest.APP_LIST, null);
    }

    /**
     * Query Contrast API to obtain the Trace listing for the specified Application (@param appId).
     *
     * @param appId String for APP ID, obtained from ContrastAppData object. (Plain-text)
     * @return com.aspectsecurity.contrast.javalib.obj.ContrastTraceList A class containing an
     *         ArrayList of ContrastTraceData objects. A return of null indicates an error. Use
     *         getErrorString() to retrieve a String error message;
     */
    public Traces getTraceList(String appId) {
        return sendRequest(ContrastRequest.APP_TRACES, appId);
    }

    /**
     * Query Contrast API to obtain the details of a specific TraceID (@param traceID)
     *
     * @param traceID String of TraceID to be looked up
     * @return com.aspectsecurity.contrast.javalib.obj.Findings A class containing an
     *         ArrayList of Trace Findings which can be iterated over to obtain Trace Details.
     *         A return of null indicates an error. Use getErrorString() to retrieve a
     *         String error message;
     */
    public Findings getTraceDetails(String traceID) {
        return sendRequest(ContrastRequest.TRACE_DETAIL, traceID);
    }

    /**
     * Query Contrast API to obtain the Contrast statistics of a specific Application (@param appId)
     *
     * @param appId String of Application ID to be looked up
     * @return com.aspectsecurity.contrast.javalib.obj.AppStats A class containing the
     *         statistical data obtained and correlated by Contrast about a specific application.
     *         A return of null indicates an error. Use getErrorString() to retrieve a
     *         String error message;
     */
    public AppStats getAppStats(String appId) {
        return sendRequest(ContrastRequest.APP_DATA, appId);
    }

    /**
     * Query Contrast API to obtain the status of the queues
     *
     * @return com.aspectsecurity.contrast.api.status.QueueStatus A class containing the
     *         status of the queues that represent the "busy-ness" of the server.
     *         A return of null indicates an error. Use getErrorString() to retrieve a
     *         String error message;
     */
    public QueueStatus getQueueStatus() {
        return sendRequest(ContrastRequest.QUEUE_STATUS, null);
    }

    /**
     * Query Contrast API to obtain the Contrast coverage of a specific Application (@param applicationId)
     *
     * @param applicationId String of Application ID to be looked up
     * @return CoverageData A class containing the
     *         per-class level coverage data obtained and correlated by Contrast about a specific application.
     *         A return of null indicates an error. Use getErrorString() to retrieve a
     *         String error message;
     */
    public CoverageData getAppCoverage(String applicationId) {
        return sendRequest(ContrastRequest.COVERAGE, applicationId);
    }

    /**
     * Query Contrast API to obtain all data about all applications available to the authenticated user.
     * This will *NOT* populate specific trace details. To obtain these, request them on an as-needed basis.
     *
     * @return com.aspectsecurity.contrast.javalib.obj.Apps A class containing an
     *         ArrayList of ContrastAppData objects
     */
    public Apps getAllAppData() {
        Apps newList = null;

        try {
            newList = getAppList();
            for (App ad : newList.getApps()) {
                ad.setAppStats(getAppStats(ad.getID()));
                ad.setTraceList(getTraceList(ad.getID()));
            }
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        }

        return newList;
    }

    /**
     * Provides an accessor to allow populating Statistics and Trace data for a single
     * application.
     *
     * @param app com.aspectsecurity.contrast.javalib.obj.App
     * @return com.aspectsecurity.contrast.javalib.obj.App
     */
    public App getSingleAppData(App app) {
        app.setAppStats(getAppStats(app.getID()));
        app.setTraceList(getTraceList(app.getID()));
        return app;
    }

    <T> T sendRequest(ContrastRequest req, String add) {
        if (!checkCredentials()) {
            return null;
        } else {
            errorString = "";
        }

        InputStream is = null;

        try {
            URL url = new URL(contrastHost + req.getUrl() + (req.isParm() ? add : ""));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(req.getMethod().toString());
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestProperty("Authorization", Base64.encodeBase64String((username + ":" + serviceKey).getBytes()).trim());
            conn.setRequestProperty("API-Key", apiKey);
            conn.connect();

            if (conn.getResponseCode() != 200) {
                errorString = conn.getResponseMessage();
                return null;
            }

            is = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            T out = (T) xmlUnmarshaller.unmarshal(reader);

            return out;
        } catch (MalformedURLException e) {
            LOG.error("Invalid URL - Please verify the hostname is correct for your Contrast TeamServer instance.", e);
            errorString = "Invalid URL - Please verify the hostname is correct for your Contrast TeamServer instance.";
            return null;
        } catch (IOException e) {
            LOG.error("Unable to connect to " + contrastHost + ". Please check your network connection and try again.", e);
            errorString = "Unable to connect to " + contrastHost + ". Please check your network connection and try again.";
            return null;
        } catch (JAXBException e) {
            LOG.error("Malformed response was received.", e);
            errorString = "A malformed response was received from the server. Please notify your system administrator.";
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
