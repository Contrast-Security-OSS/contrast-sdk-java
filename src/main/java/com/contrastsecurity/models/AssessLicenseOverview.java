package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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

public class AssessLicenseOverview {

  // Inner Class with the actual details returned for a Group
  public class Details {
    /**
     * The total number of Assess licenses for an Organization.
     *
     * @return the total number of Assess licenses for an Organization
     */
    public int getTotal() {
      return this.total;
    }

    @SerializedName("total")
    private int total = 0;

    /**
     * The total number of USED Assess licenses for an Organization.
     *
     * @return the total number of USED Assess licenses for an Organization
     */
    public int getUsed() {
      return this.used;
    }

    @SerializedName("used")
    private int used = 0;
  }

  /**
   * Return the details of the Licensing
   *
   * @return details of the Licensing
   */
  public Details getDetails() {
    return details;
  }

  @SerializedName("breakdown")
  private Details details;
}
