package com.contrastsecurity.http;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
        return SEPARATOR + organizationId + SEPARATOR + APPLICATIONS_URL + SEPARATOR + appId + SEPARATOR + COVERAGE_URL;
    }

    public String getLibrariesUrl(String organizationId, String appId) {
        return SEPARATOR + organizationId + SEPARATOR + APPLICATIONS_URL + SEPARATOR + appId + SEPARATOR + LIBRARIES_URL + buildExpand("manifest", "servers", "cve");
    }

    public String getTracesUrl(String organizationId, String appId) {
        return SEPARATOR + organizationId + SEPARATOR + TRACES_URL + SEPARATOR + appId;
    }

    public String getTracesByDatesUrl(String organizationaId, String appId, Date startDate, Date endDate) {
        //936a8014-10d5-4c76-bc96-6f710dbfcc8b/traces/3da856f4-c508-48b8-95a9-514eddefcbf3/filter/tags/empty-tags/search?endDate=1461346140000&sort=-lastTimeSeen&startDate=1459746000000

        ArrayList<String> params = new ArrayList<>();

        if (startDate != null) {
            params.add("startDate=" + startDate.getTime());
        }

        if (endDate != null) {
            params.add("endDate=" + endDate.getTime());
        }

        return SEPARATOR + organizationaId + SEPARATOR + TRACES_URL + SEPARATOR + appId + SEPARATOR + FILTER_QUERY + SEPARATOR +
               TAGS_QUERY + SEPARATOR + EMPTY_TAGS_QUERY + SEPARATOR + SEARCH_QUERY + SEPARATOR + StringUtils.join(params, AND_SEPARATOR);
    }

    // ----------------- UTILITIES --------------------------
    private String buildExpand(List<String> values) {
        return QUERY_SEPARATOR + EXPAND_PARAM + EQUALS_SEPARATOR + StringUtils.join(values, COMMA_DELIMITER);
    }

    private String buildExpand(String... values) {
        return QUERY_SEPARATOR + EXPAND_PARAM + EQUALS_SEPARATOR + StringUtils.join(values, COMMA_DELIMITER);
    }

    public static List<String> getSeverityList(String severity) {
        return SEVERITIES.subList(SEVERITIES.indexOf(severity), SEVERITIES.size());
    }

    private static final String ENGINE_JAVA_URL = "/%s/engine/%s/java/";
    private static final String ENGINE_DOTNET_URL = "/%s/engine/%s/.net/";

    private static final String TRACES_URL = "traces";
    private static final String COVERAGE_URL = "sitemap/activity/entries";
    private static final String APPLICATIONS_URL = "applications";
    private static final String LIBRARIES_URL = "libraries";

    private static final String EXPAND_PARAM = "expand";

    private static final String FILTER_QUERY = "filter";

    private static final String SEARCH_QUERY = "search";
    private static final String TAGS_QUERY = "tags";
    private static final String EMPTY_TAGS_QUERY = "empty-tags";

    private static final String COMMA_DELIMITER = ",";
    private static final String SEPARATOR = "/";
    private static final String QUERY_SEPARATOR = "?";
    private static final String EQUALS_SEPARATOR = "=";
    private static final String AND_SEPARATOR = "&";

    public static final List<String> SEVERITIES = Arrays.asList("Note", "Low", "Medium", "High", "Critical");
}
