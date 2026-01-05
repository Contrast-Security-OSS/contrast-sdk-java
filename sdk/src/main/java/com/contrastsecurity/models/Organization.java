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
