package com.contrastsecurity.http;

import com.contrastsecurity.models.SecurityCheck;

public class SecurityCheckForm {
    private String applicationName;
    private String appVersion;
    private long timestamp;
    private SecurityCheck.Origin origin;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public SecurityCheck.Origin getOrigin() {
        return origin;
    }

    public void setOrigin(SecurityCheck.Origin origin) {
        this.origin = origin;
    }
}
