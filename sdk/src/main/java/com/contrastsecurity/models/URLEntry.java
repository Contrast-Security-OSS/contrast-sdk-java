package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2024 Contrast Security, Inc.
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

/** A URI that's been observed under monitoring. */
public class URLEntry {

  /**
   * Return the URL
   *
   * @return the URL
   */
  public String getUrl() {
    return url;
  }

  private String url;

  public boolean getVulnerable() {
    return vulnerable;
  }

  private boolean vulnerable = false;

  /**
   * Return the last time this URI was observed under monitoring.
   *
   * @return the last time this URI was observed under monitoring
   */
  public Long getLastTimeSeen() {
    return lastTimeSeen;
  }

  @SerializedName("last_time_seen")
  private Long lastTimeSeen;
}
