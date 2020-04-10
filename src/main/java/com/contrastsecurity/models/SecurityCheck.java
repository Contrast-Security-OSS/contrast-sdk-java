package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SecurityCheck {
    public enum Origin {
        OTHER,
        JENKINS
    }

    /**
     * The ID of the security check
     * @return the ID of the security check.
     */
    @SerializedName("id")
    private Long id;

    /**
     * The name of the application verified.
     * @return the name of the application
     */
    @SerializedName("application_name")
    private String applicationName;

    /**
     * The ID of the application verified.
     * @return the ID of the application.
     */
    @SerializedName("application_id")
    private String applicationId;

    /**
     * The origin of where the security check was made from.
     * @return the origin of the security check.
     */
    @SerializedName("origin")
    private Origin origin;

    /**
     * The result of the security check
     * true = the application passed all job outcome policies.
     * false = the application failed a job outcome policy.
     * @return the result of the security check.
     */
    @SerializedName("result")
    private boolean pass;


    /**
     * The job outcome policy that the application failed.
     * null if the application passed all job outcome policies.
     * @reutnr The job outcome policy that the application failed.
     */
    @SerializedName("job_outcome_policy")
    private JobOutcomePolicy jobOutcomePolicy;
}
