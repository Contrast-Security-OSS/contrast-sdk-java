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

import java.util.List;

/** Base class for api calls with users */
public class Users {

  /**
   * Return the number of users
   *
   * @return the count of users
   */
  public Integer getCount() {
    return count;
  }

  private Integer count;

  /**
   * Return the user objects
   *
   * @return a list of users
   */
  public List<User> getUsers() {
    return users;
  }

  private List<User> users;
}
