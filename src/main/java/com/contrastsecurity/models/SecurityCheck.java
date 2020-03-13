package com.contrastsecurity.models;

public class SecurityCheck {

    public enum Origin {
        OTHER,
        JENKINS
    }

    private long id;
    private String applicationName;
    private String applicationId;
    private Origin origin;
    private String result;
    private JobOutcomePolicy jobOutcomePolicy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public JobOutcomePolicy getJobOutcomePolicy() {
        return jobOutcomePolicy;
    }

    public void setJobOutcomePolicy(JobOutcomePolicy jobOutcomePolicy) {
        this.jobOutcomePolicy = jobOutcomePolicy;
    }
}
