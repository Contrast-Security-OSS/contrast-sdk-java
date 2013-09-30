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

import com.contrastsecurity.sdk.coverage.CoverageData;
import com.contrastsecurity.sdk.exception.InitializationException;
import com.contrastsecurity.sdk.traces.Findings;
import com.contrastsecurity.sdk.traces.Traces;
import com.contrastsecurity.sdk.traces.status.QueueStatus;
import com.contrastsecurity.sdk.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

    private static final String SERVICE_APP_LIST_URL = "/Contrast/s/api/app/list";
    private static final String SERVICE_APP_DATA_URL = "/Contrast/s/api/app/stats/";
    private static final String SERVICE_APP_TRACES_URL = "/Contrast/s/api/traces/";
    private static final String SERVICE_COVERAGE_URL = "/Contrast/s/api/app/coverage/";
    private static final String SERVICE_TRACE_DETAIL_URL = "/Contrast/s/api/traces/trace/";
    private static final String SERVICE_QUEUE_STATUS_URL = "/Contrast/s/api/status/";
    private static final String VALID_EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

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
     * If failure is returned, access connection response code via getErrorString().
     *
     * @return true = Successful connection, false = Failed connection, use getErrorString()
     *         to obtain a reason for any return of false, use getErrorString() to get further
     *         details of a return value of false.
     */
    public boolean testConnection() {
        if (!checkCredentials()) {
            return false;
        }

        if (xmlContext == null) {
            errorString = "Error initializing JAXB Context & Unmarshaller";
            return false;
        } else {
            errorString = "";
        }

        HttpURLConnection conn = getConnection(null, 0);
        try {
            boolConnectionSuccess = false;
            if (conn == null) {
                boolConnectionSuccess = false;
            } else {
                int statusCode = conn.getResponseCode();
                if (statusCode != 200) {
                    boolConnectionSuccess = false;
                    errorString = conn.getResponseMessage();
                } else {
                    boolConnectionSuccess = true;
                }
            }
        } catch (IOException ioe) {
            boolConnectionSuccess = false;
            errorString = ioe.getMessage();
        }

        return boolConnectionSuccess;
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
        HttpURLConnection conn = getConnection(null, 0);
        InputStream is = null;
        if (conn == null) {
            return null;
        }

        try {
            int statusCode = conn.getResponseCode();
            if (statusCode != 200) {
                errorString = conn.getResponseMessage();
                return null;
            }
            is = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            Apps apps = (Apps) xmlUnmarshaller.unmarshal(reader);
            return apps;
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
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
        HttpURLConnection conn = getConnection(appId, 1);
        InputStream is = null;
        try {
            if (conn == null) {
                return null;
            }
            int statusCode = conn.getResponseCode();
            if (statusCode != 200) {
                errorString = conn.getResponseMessage();
                return null;
            }
            is = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            Traces traces = (Traces) xmlUnmarshaller.unmarshal(reader);
            return traces;
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
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
        HttpURLConnection conn = getConnection(traceID, 2);
        InputStream is = null;
        try {
            int statusCode = conn.getResponseCode();
            if (statusCode != 200) {
                errorString = conn.getResponseMessage();
                return null;
            }

            InputStreamReader reader = new InputStreamReader(is);
            Unmarshaller xmlUnmarshaller = xmlContext.createUnmarshaller();
            Findings traceList = (Findings) xmlUnmarshaller.unmarshal(reader);
            return traceList;
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
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
        HttpURLConnection conn = getConnection(appId, 3);
        InputStream is = null;

        try {
            int statusCode = conn.getResponseCode();
            if (statusCode != 200) {
                errorString = conn.getResponseMessage();
                return null;
            }
            is = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            AppStats appStats = (AppStats) xmlUnmarshaller.unmarshal(reader);
            return appStats;
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        }
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
        HttpURLConnection conn = getConnection(null, 5);
        InputStream is = null;

        try {
            int statusCode = conn.getResponseCode();
            if (statusCode != 200) {
                errorString = conn.getResponseMessage();
                return null;
            }
            is = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            QueueStatus coverage = (QueueStatus) xmlUnmarshaller.unmarshal(reader);
            return coverage;
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        }
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
        HttpURLConnection conn = getConnection(applicationId, 4);
        InputStream is = null;

        try {
            int statusCode = conn.getResponseCode();
            if (statusCode != 200) {
                errorString = conn.getResponseMessage();
                return null;
            }
            is = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            CoverageData coverage = (CoverageData) xmlUnmarshaller.unmarshal(reader);
            return coverage;
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        }
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

    /**
     * Internal method to establish HTTP Connection and obtain the HTTP Response.
     *
     * @param add  String for additional URL pieces: App ID, Trace ID (Plain-text)
     * @param type Integer which type of query to make (0 = App list, 1 = Trace list, 2 = Trace Details)
     * @return HttpResponse The HTTP Response of the attempted connection
     */
    private HttpURLConnection getConnection(String add, int type) {

        if (!checkCredentials()) {
            return null;
        } else {
            errorString = "";
        }

        String url;
        try {
            if (type == 0) {
                url = contrastHost + SERVICE_APP_LIST_URL;
            } else if (type == 1) {
                url = contrastHost + SERVICE_APP_TRACES_URL + add;
            } else if (type == 2) {
                url = contrastHost + SERVICE_TRACE_DETAIL_URL + add;
            } else if (type == 3) {
                url = contrastHost + SERVICE_APP_DATA_URL + add;
            } else if (type == 4) {
                url = contrastHost + SERVICE_COVERAGE_URL + add;
            } else if (type == 5) {
                url = contrastHost + SERVICE_QUEUE_STATUS_URL + add;
            } else {
                url = contrastHost + SERVICE_APP_LIST_URL;
            }
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestProperty("Authorization", Base64.encodeBase64String((username + ":" + serviceKey).getBytes()).trim());
            conn.setRequestProperty("API-Key", apiKey);
            conn.connect();

            return conn;
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        }
    }
}
