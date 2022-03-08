package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 Contrast Security, Inc.
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

public class SignUp {

  /**
   * Return the signup time for this user, e.g.:
   *
   * @return the signUp date for this user
   */
  public long getDate() {
    return date;
  }

  @SerializedName("signup_date")
  private long date;

  /**
   * Return the signup accepted terms for this user, e.g.:
   *
   * @return the acceptedTerms for this user
   */
  public boolean getAcceptedTerms() {
    return acceptedTerms;
  }

  @SerializedName("accept_terms")
  private boolean acceptedTerms;
}
