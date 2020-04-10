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
@RequiredArgsConstructor
@AllArgsConstructor
public class SecurityCheckForm {

    /**
     * The name of the application to be verified
     * @param applicationName New value for application name.
     * @return The name of the application.
     */
    @SerializedName("application_name")
    @NonNull
    private String applicationName;

    /**
     * The language of the agent used to instrument the application
     * @param agentLanguage New value for agent language.
     * @return the agent language of the application
     */
    @SerializedName("agent_language")
    @NonNull
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
    private SecurityCheck.Origin origin = SecurityCheck.Origin.OTHER;

    /**
     * Filter the application's vulnerabilities using start date in milliseconds.
     * Do not set if you want to verify against all of the application's vulnerabilities.
     * @param startDate New start date.
     * @return The start date filter
     */
    @SerializedName("start_date")
    private Long startDate;
}
