package com.contrastsecurity.http;

import com.contrastsecurity.models.AgentType;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Filter;

public class UrlBuilder {

    private static UrlBuilder instance = new UrlBuilder();

    private UrlBuilder() {}

    public static UrlBuilder getInstance() {
        return instance;
    }

    public String getApplicationUrl(String organizationId, String appId) {
        return String.format("/ng/%s/applications/%s", organizationId, appId);
    }

    public String getApplicationsUrl(String organizationId) {
        return String.format("/ng/%s/applications?%s", organizationId, "base=false");
    }

    public String getCoverageUrl(String organizationId, String appId) {
        return String.format("/ng/%s/applications/%s/coverage", organizationId, appId);
    }

    public String getLibrariesUrl(String organizationId, String appId) {
        return String.format("/ng/%s/applications/%s/libraries%s", organizationId, appId, buildExpand("manifest", "servers", "cve"));
    }

    public String getTracesUrl(String organizationId, String appId) {
        return String.format("/ng/%s/traces/%s/filter/tags/empty-tags/search", organizationId, appId);
    }

    public String getTracesWithFilterUrl(String organizationalId, String appId, FilterForm form) {
        return String.format("/ng/%s/traces/%s/filter/tags/empty-tags/search?%s", organizationalId, appId, form.toString());
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