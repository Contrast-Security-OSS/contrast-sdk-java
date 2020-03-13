package com.contrastsecurity.models;

import java.util.List;

public class JobOutcomePolicy {

    public enum Direction {
        INCLUDE,
        EXCLUDE
    }

    public enum Outcome {
        SUCCESS,
        FAIL,
        UNSTABLE
    }

    private long id;
    private String applicationName;
    private List<ApplicationImportance> importances;
    private boolean allApplications;
    private List<String> rules;
    private Direction direction;
    private String status; //Potential to turn this into an enum
    private List<JobOutcomePolicySeverity> vulnerabilitySeverities;
    private Outcome outcome;
    private boolean enabled;

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

    public List<ApplicationImportance> getImportances() {
        return importances;
    }

    public void setImportances(List<ApplicationImportance> importances) {
        this.importances = importances;
    }

    public boolean isAllApplications() {
        return allApplications;
    }

    public void setAllApplications(boolean allApplications) {
        this.allApplications = allApplications;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JobOutcomePolicySeverity> getVulnerabilitySeverities() {
        return vulnerabilitySeverities;
    }

    public void setVulnerabilitySeverities(List<JobOutcomePolicySeverity> vulnerabilitySeverities) {
        this.vulnerabilitySeverities = vulnerabilitySeverities;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
