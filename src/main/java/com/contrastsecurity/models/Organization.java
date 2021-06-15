package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class Organization {

  public String getDateFormat() {
    return dateFormat;
  }

  @SerializedName("date_format")
  private String dateFormat;

  public String getName() {
    return name;
  }

  private String name;

  public String getOrgUuid() {
    return orgUuid;
  }

  @SerializedName("organization_uuid")
  private String orgUuid;

  public String getShortName() {
    return shortName;
  }

  @SerializedName("shortname")
  private String shortName;

  public String getTimeFormat() {
    return timeFormat;
  }

  @SerializedName("time_format")
  private String timeFormat;

  public String getTimeZone() {
    return timeZone;
  }

  @SerializedName("timezone")
  private String timeZone;
}
