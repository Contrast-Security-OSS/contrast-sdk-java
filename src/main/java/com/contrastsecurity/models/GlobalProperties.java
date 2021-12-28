package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class GlobalProperties {
  private String version;
  private String mode;
  private String build;

  @SerializedName("internal_version")
  private String internalVersion;

  @SerializedName("max_failed_attempts")
  private int maxFailedAttempts;

  private String timezone;

  @SerializedName("date_format")
  private String dateFormat;

  @SerializedName("time_format")
  private String timeFormat;

  @SerializedName("teamserver_url")
  private String teamServerUrl;

  public String getVersion() {
      return version;
  }

  public String getMode() {
      return mode;
  }

  public String getBuild() {
      return build;
  }

  public String getInternalVersion() {
      return internalVersion;
  }

  public int getMaxFailedAttempts() {
      return maxFailedAttempts;
  }

  public String getTimezone() {
      return timezone;
  }

  public String getDateFormat() {
      return dateFormat;
  }

  public String getTimeFormat() {
      return timeFormat;
  }

  public String getTeamServerUrl() {
      return teamServerUrl;
  }
}
