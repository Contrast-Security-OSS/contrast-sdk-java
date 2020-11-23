package com.contrastsecurity.http;

import com.contrastsecurity.models.AgentType;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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

    public static SecurityCheckForm securityCheckForm(String applicationId) {
        return new SecurityCheckForm(applicationId, null);
    }

    public SecurityCheckForm(String applicationId, HashMap<String, Object> options) {
        this.applicationId = applicationId;
        this.jobStartTime = Long.valueOf(options.get("jobStartTime").toString());
    }

    public static SecurityCheckForm securityCheckForm(String applicationId, AgentType agentLanguage) {
        return new SecurityCheckForm(applicationId, agentLanguage, null);
    }

    public SecurityCheckForm(String applicationId, AgentType agentLanguage,  HashMap<String, Object> options) {
        this.applicationId = applicationId;
        this.agentLanguage = agentLanguage;
        this.jobStartTime = Long.valueOf(options.get("jobStartTime").toString());
    }

}
