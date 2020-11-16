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
     * Where the request is being made
     * Default: OTHER
     * @param origin New origin for the security check
     * @returnThe origin of the security check
     */
    @SerializedName("origin")
    private String origin = "OTHER";

    /**
     * The job start time of the jenkins job
     * @param jobStartTime New value for job start time.
     * @return the start time of the jenkins job
     */
    @SerializedName("job_start_time")
    private Long jobStartTime;

    public SecurityCheckForm(String applicationId) {
        this.applicationId = applicationId;
    }

    public SecurityCheckForm(String applicationName, AgentType agentLanguage) {
        this.applicationName = applicationName;
        this.agentLanguage = agentLanguage;
    }

    public SecurityCheckForm(String applicationId, Long jobStartTime) {
        this.applicationId = applicationId;
        this.jobStartTime = jobStartTime;
    }
}
