package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2026 Contrast Security, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
