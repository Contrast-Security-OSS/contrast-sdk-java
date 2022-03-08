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

/** A User in Contrast. */
public class User {

  /**
   * Return the uuid for this user, e.g.:
   *
   * @return the uuid of this user
   */
  public String getUUID() {
    return uuid;
  }

  @SerializedName("user_uid")
  private String uuid;

  /**
   * Return the api_only for this user, e.g.:
   *
   * @return true if this user is an API Only user
   */
  public boolean getAPI() {
    return api;
  }

  @SerializedName("api_only")
  private boolean api;

  /**
   * Return the last_name for this user, e.g.:
   *
   * @return the lastName of this user
   */
  public String getLastName() {
    return lastName;
  }

  @SerializedName("last_name")
  private String lastName;

  /**
   * Return the first_name for this user, e.g.:
   *
   * @return the firstName of this user
   */
  public String getFirstName() {
    return firstName;
  }

  @SerializedName("first_name")
  private String firstName;

  /**
   * Return the last login details of the user.
   *
   * @return the login for the user
   */
  public Login getLogin() {
    return this.login;
  }

  private Login login = null;

  /**
   * Return the signup details of the user.
   *
   * @return the signup for the user
   */
  public SignUp getSignUp() {
    return this.signup;
  }

  private SignUp signup = null;
}
