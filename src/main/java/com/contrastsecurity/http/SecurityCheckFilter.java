package com.contrastsecurity.http;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SecurityCheckFilter {
  public enum QueryBy {
    APP_VERSION_TAG,
    START_DATE
  }

  /**
   * The criteria to query vulnerabilities by
   */
  @SerializedName("query_by")
  private QueryBy queryBy;

  /**
   * AppVersionTags to match
   */
  @SerializedName("app_version_tags")
  private List<String> appVersionTags;

  /**
   * startDate to match
   */
  @SerializedName("start_date")
  private Long startDate;
}
