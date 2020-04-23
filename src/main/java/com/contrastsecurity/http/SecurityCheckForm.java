package com.contrastsecurity.http;

import com.contrastsecurity.models.AgentType;
import com.contrastsecurity.models.SecurityCheck;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Form that is used making security checks
 */
@Getter
@Setter
@AllArgsConstructor
public class SecurityCheckForm {

    /**
     * The ID of the application to be verified
     * @param applicationId New value for application id.
     * @return The ID of the application
     */
    @SerializedName("application_id")
    private String applicationId;

    /**
     * The name of the application to be verified
     * @param applicationName New value for application name.
     * @return The name of the application.
     */
    @SerializedName("application_name")
    private String applicationName;

    /**
     * The language of the agent used to instrument the application
     * @param agentLanguage New value for agent language.
     * @return the agent language of the application
     */
    @SerializedName("agent_language")
    private AgentType agentLanguage;

    /**
     * Filter the application's vulnerabilities using app version tags.
     * Do not set if you want to verify against all of the application's vulnerabilities.
     * @param appVersionTags New appVersionTags.
     * @return The appVersionTags filter
     */
    @SerializedName("app_version_tags")
    private List<String> appVersionTags;

    /**
     * Where the request is being made
     * Default: OTHER
     * @param origin New origin for the security check
     * @returnThe origin of the security check
     */
    @SerializedName("origin")
    private String origin = "OTHER";

    /**
     * Filter the application's vulnerabilities using start date in milliseconds.
     * Do not set if you want to verify against all of the application's vulnerabilities.
     * @param startDate New start date.
     * @return The start date filter
     */
    @SerializedName("start_date")
    private Long startDate;

    public SecurityCheckForm(String applicationId) {
        this.applicationId = applicationId;
    }

    public SecurityCheckForm(String applicationName, AgentType agentLanguage) {
        this.applicationName = applicationName;
        this.agentLanguage = agentLanguage;
    }
}
