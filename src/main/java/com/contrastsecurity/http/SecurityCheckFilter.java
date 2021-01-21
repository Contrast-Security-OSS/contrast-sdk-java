package com.contrastsecurity.http;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityCheckFilter {
  public enum QueryBy {
    APP_VERSION_TAG,
    START_DATE,
    NONE
  }

  /**
   * The criteria to query vulnerabilities by
   */
  @SerializedName("query_by")
  private QueryBy queryBy = QueryBy.NONE;

  /**
   * AppVersionTags to match
   */
  @SerializedName("app_version_tags")
  private String[] appVersionTags;

  /**
   * startDate to match
   */
  @SerializedName("start_date")
  private Long startDate;
}
