package com.contrastsecurity.http;

import com.contrastsecurity.models.AgentType;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class UrlBuilder {

    private static UrlBuilder instance = new UrlBuilder();

    private UrlBuilder() {
    }

    public static UrlBuilder getInstance() {
        return instance;
    }

    public String getProfileOrganizationsUrl() {
        return "/ng/profile/organizations";
    }

    public String getProfileDefaultOrganizationUrl() {
        return "/ng/profile/organizations/default";
    }

    public String getApplicationUrl(String organizationId, String appId, EnumSet<FilterForm.ApplicationExpandValues> expandValues) {
        return String.format("/ng/%s/applications/%s%s", organizationId, appId, buildExpand(expandValues));
    }

    public String getApplicationsUrl(String organizationId) {
        return String.format("/ng/%s/applications?%s", organizationId, "base=false");
    }

    public String getCoverageUrl(String organizationId, String appId) {
        return String.format("/ng/%s/applications/%s/coverage", organizationId, appId);
    }

    public String getLibrariesUrl(String organizationId, String appId, EnumSet<FilterForm.LibrariesExpandValues> expandValues) {
        return String.format("/ng/%s/applications/%s/libraries%s", organizationId, appId, buildExpand(expandValues));
    }

    public String getServersUrl(String organizationId, FilterForm form) {
        String formString = form == null ? "" : form.toString();
        return String.format("/ng/%s/servers%s", organizationId, formString);
    }

    public String getServersFilterUrl(String organizationId, FilterForm form) {
        String formString = form == null ? "" : form.toString();
        return String.format("/ng/%s/servers/filter%s", organizationId, formString);
    }

    public String getAllTracesUrl(String organizationId, String appId, EnumSet<FilterForm.TraceExpandValue> expandValues) {
        return String.format("/ng/%s/traces/%s/filter/workflow/00001/search%s", organizationId, appId, buildExpand(expandValues));
    }

    public String getTracesByApplicationUrl(String organizationId, String appId, TraceFilterForm form) {
        String formString = form == null ? "" : form.toString();
        return String.format("/ng/%s/traces/%s/filter/%s", organizationId, appId, formString);
    }

    public String getTraceListingUrl(String organizationId, String appId, TraceFilterType traceFilterType) {
        return String.format("/ng/%s/traces/%s/filter/%s/listing", organizationId, appId, traceFilterType.toString());
    }

    public String getTracesWithFilterUrl(String organizationId, String appId, TraceFilterType traceFilterType, TraceFilterKeycode traceFilterKeycode, TraceFilterForm form) {
        String formString = form == null ? "" : form.toString();
        return String.format("/ng/%s/traces/%s/filter/%s/%s/search%s", organizationId, appId, traceFilterType.toString(), traceFilterKeycode.toString(), formString);
    }

    public String getRules(String organizationId) {
        return String.format("/ng/%s/rules", organizationId);
    }

    public String getAgentUrl(AgentType type, String organizationId, String profileName) {
        String url = "";

        if (AgentType.JAVA.equals(type)) {
            url += String.format("/ng/%s/agents/%s/java?jvm=1_6", organizationId, profileName);
        } else if (AgentType.JAVA1_5.equals(type)) {
            url += String.format("/ng/%s/agents/%s/java?jvm=1_5", organizationId, profileName);
        } else if (AgentType.DOTNET.equals(type)) {
            url += String.format("/ng/%s/agents/%s/dotnet", organizationId, profileName);
        } else if (AgentType.NODE.equals(type)) {
            url += String.format("/ng/%s/agents/%s/node", organizationId, profileName);
        }

        return url;
    }

    // ----------------- UTILITIES --------------------------
    private String buildExpand(String... values) {
        if (values == null || values.length == 0) {
            return "";
        }

        return QUERY_SEPARATOR + EXPAND_PARAM + EQUALS_SEPARATOR + StringUtils.join(values, COMMA_DELIMITER);
    }

    private String buildExpand(EnumSet<?> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }

        return QUERY_SEPARATOR + EXPAND_PARAM + EQUALS_SEPARATOR + StringUtils.join(values, COMMA_DELIMITER);
    }

    public static List<String> getSeverityList(String severity) {
        return SEVERITIES.subList(SEVERITIES.indexOf(severity), SEVERITIES.size());
    }

    private static final String EXPAND_PARAM = "expand";

    private static final String COMMA_DELIMITER = ",";
    private static final String QUERY_SEPARATOR = "?";
    private static final String EQUALS_SEPARATOR = "=";
    private static final String AND_SEPARATOR = "&";

    public static final List<String> SEVERITIES = Arrays.asList("Note", "Low", "Medium", "High", "Critical");
}
