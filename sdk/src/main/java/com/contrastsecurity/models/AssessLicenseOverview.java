package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2025 Contrast Security, Inc.
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
    private int total;
    private int used;
    private int unused;

    @SerializedName("max_expiration_date")
    private long maxExpirationDate;

    /**
     * The total number of Assess licenses for an organization.
     *
     * @return the total number of Assess licenses for an organization
     */
    public int getTotal() {
      return total;
    }

    /**
     * The total number of used Assess licenses for an organization.
     *
     * @return the total number of used Assess licenses for an organization
     */
    public int getUsed() {
      return used;
    }

    /**
     * The total number of unused Assess licenses for an organization.
     *
     * @return the total number of unused Assess licenses for an organization.
     */
    public int getUnused() {
      return unused;
    }

    public long getMaxExpirationDate() {
      return maxExpirationDate;
    }
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
