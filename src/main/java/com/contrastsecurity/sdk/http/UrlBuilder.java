package com.contrastsecurity.sdk.http;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public class UrlBuilder {

    private static UrlBuilder instance = new UrlBuilder();

    private UrlBuilder() {}

    public static UrlBuilder getInstance() {
        return instance;
    }

    public String getSingleApplicationUrl(String organizationId, String appId) {
        return SEPARATOR + organizationId + SEPARATOR + APPLICATIONS_URL + SEPARATOR + appId;
    }

    public String getApplicationsUrl(String organizationId) {
        return SEPARATOR + organizationId + SEPARATOR + APPLICATIONS_URL;
    }

    public String getCoverageUrl(String organizationId, String appId) {
        return SEPARATOR + organizationId + APPLICATIONS_URL + SEPARATOR + appId + SEPARATOR + COVERAGE_URL;
    }

    public String getLibrariesUrl(String organizationId, String appId) {
        return SEPARATOR + organizationId + APPLICATIONS_URL + SEPARATOR + appId + SEPARATOR + LIBRARIES_URL + buildExpand("manifest", "servers", "cve");
    }

    public String getTracesUrl(String organizationId, String appId) {
        return SEPARATOR + organizationId + TRACES_URL + SEPARATOR + appId;
    }


    // ----------------- UTILITIES --------------------------
    private String buildExpand(List<String> values) {
        return QUERY_SEPARATOR + EXPAND_PARAM + EQUALS_SEPARATOR + StringUtils.join(values, COMMA_DELIMITER);
    }

    private String buildExpand(String... values) {
        return QUERY_SEPARATOR + EXPAND_PARAM + EQUALS_SEPARATOR + StringUtils.join(values, COMMA_DELIMITER);
    }



    private static final String ENGINE_JAVA_URL = "/%s/engine/%s/java/";
    private static final String ENGINE_DOTNET_URL = "/%s/engine/%s/.net/";

    private static final String TRACES_URL = "traces";
    private static final String COVERAGE_URL = "sitemap/activity/entries";
    private static final String APPLICATIONS_URL = "applications";
    private static final String LIBRARIES_URL = "libraries";

    private static final String EXPAND_PARAM = "expand";

    private static final String COMMA_DELIMITER = ",";
    private static final String SEPARATOR = "/";
    private static final String QUERY_SEPARATOR = "?";
    private static final String EQUALS_SEPARATOR = "=";

}
