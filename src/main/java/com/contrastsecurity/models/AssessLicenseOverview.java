package com.contrastsecurity.models;

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
