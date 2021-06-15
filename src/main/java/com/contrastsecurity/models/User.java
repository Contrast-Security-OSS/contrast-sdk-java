/*
 * Copyright (c) 2014, Contrast Security, LLC.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * Neither the name of the Contrast Security, LLC. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.contrastsecurity.models;

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
