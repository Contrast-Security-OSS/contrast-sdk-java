package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class LibraryStats {

  // Inner Class for Stats
  public class Stats {
    /**
     * The LOC of all libraries
     *
     * @return LOC of all libraries
     */
    public String getLOCshorthand() {
      return this.locShorthand;
    }

    @SerializedName("library_loc_shorthand")
    private String locShorthand;

    /**
     * The total count of libraries
     *
     * @return the total count of libraries
     */
    public int getTotal() {
      return this.total;
    }

    @SerializedName("total")
    private int total = 0;

    /**
     * The count of vulnerable libraries
     *
     * @return the count of vulnerable libraries
     */
    public int getVulnerableCount() {
      return this.vulnerableCount;
    }

    @SerializedName("vulnerables")
    private int vulnerableCount = 0;

    /**
     * The total vulnerability count of libraries
     *
     * @return the total vulnerability count of libraries
     */
    public int getTotalVulns() {
      return this.totalVulns;
    }

    @SerializedName("total_vulnerabilities")
    private int totalVulns = 0;

    /**
     * The count of stale libraries
     *
     * @return the count of stale libraries
     */
    public int getStaleCount() {
      return this.stale;
    }

    @SerializedName("stale")
    private int stale = 0;
  }

  /**
   * Return the library stats
   *
   * @return the library stats
   */
  public Stats getStats() {
    return stats;
  }

  @SerializedName("stats")
  private Stats stats;
}
