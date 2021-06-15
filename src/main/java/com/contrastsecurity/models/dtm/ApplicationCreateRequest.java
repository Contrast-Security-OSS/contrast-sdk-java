package com.contrastsecurity.models.dtm;

import com.contrastsecurity.models.AgentType;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ApplicationCreateRequest {
  @SerializedName("name")
  @NonNull
  private String appName;

  @SerializedName("language")
  @NonNull
  private AgentType appLanguage;

  @SerializedName("path")
  private String appPath;

  @SerializedName("short_name")
  private String appShortName;

  public ApplicationCreateRequest(String appName, AgentType appLanguage) {
    this.appName = appName;
    this.appLanguage = appLanguage;
  }
}
