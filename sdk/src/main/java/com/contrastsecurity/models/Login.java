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

public class Login {

  /**
   * Return the host address for this login, e.g.:
   *
   * @return the host of this login
   */
  public String getHost() {
    return host;
  }

  @SerializedName("last_host_address")
  private String host;

  /**
   * Return the last login time for this login, e.g.:
   *
   * @return the lastLogin of this login
   */
  public long getLastLogin() {
    return lastLogin;
  }

  @SerializedName("last_login_time")
  private long lastLogin;

  /**
   * Return the number of failed attempts for this login, e.g.:
   *
   * @return the failedAttempts of this login
   */
  public int getFailedAttempts() {
    return failedAttempts;
  }

  @SerializedName("failed_attempts")
  private int failedAttempts;
}
