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
import java.util.List;

/** An application library. */
public class Library {

  /**
   * Return the filename for this library.
   *
   * @return the simple name of the library, like 'log4j-2.1.4.jar'.
   */
  public String getFilename() {
    return fileName;
  }

  @SerializedName("file_name")
  private String fileName;

  /**
   * Return the version of this library according to the library authority like Maven Central or
   * NuGet.
   *
   * @return the version of this library
   */
  public String getVersion() {
    return version;
  }

  private String version;

  public List<Application> getApplications() {
    return apps;
  }

  private List<Application> apps;

  /**
   * Return the number of classes in this library.
   *
   * @return the number of classes in this library
   */
  public int getClassCount() {
    return classCount;
  }

  @SerializedName("class_count")
  private int classCount;

  /**
   * Return the number of classes used by this library. Right now, this only returns the maximum
   * number of classes used by any one instance of the running application. In the future, this will
   * be changed to represent the total number of distinct classes used across all instances of the
   * running application.
   *
   * @return the maximum number of classes used in any instance of this library
   */
  public int getClassedUsed() {
    return classesUsed;
  }

  @SerializedName("classes_used")
  private int classesUsed;

  /**
   * Return the blob of MANIFEST.MF in plaintext.
   *
   * @return the plaintext MANIFEST.MF file in one String
   */
  public String getManifest() {
    return manifest;
  }

  private String manifest;

  @SerializedName("library_id")
  private long libraryId;

  private String grade;
  private String hash;
  private String group;

  @SerializedName("file_version")
  private String fileVersion;

  @SerializedName("app_id")
  private String appId;

  @SerializedName("app_name")
  private String appName;

  @SerializedName("app_context_path")
  private String appContextPath;

  @SerializedName("app_language")
  private String appLanguage;

  @SerializedName("latest_version")
  private String latestVersion;

  @SerializedName("release_date")
  private long releaseDate;

  @SerializedName("latest_release_date")
  private long latestReleaseDate;

  @SerializedName("total_vulnerabilities")
  private int totalVulnerabilities;

  @SerializedName("high_vulnerabilities")
  private int highVulnerabilities;

  private boolean custom;

  @SerializedName("lib_score")
  private double libScore;

  @SerializedName("months_outdated")
  private int monthsOutdated;

  public long getLibraryId() {
    return libraryId;
  }

  public String getGrade() {
    return grade;
  }

  public String getHash() {
    return hash;
  }

  public String getFileName() {
    return fileName;
  }

  public String getGroup() {
    return group;
  }

  public String getFileVersion() {
    return fileVersion;
  }

  public String getLatestVersion() {
    return latestVersion;
  }

  public long getReleaseDate() {
    return releaseDate;
  }

  public long getLatestReleaseDate() {
    return latestReleaseDate;
  }

  public int getTotalVulnerabilities() {
    return totalVulnerabilities;
  }

  public int getHighVulnerabilities() {
    return highVulnerabilities;
  }

  public boolean getCustom() {
    return custom;
  }

  public double getLibScore() {
    return libScore;
  }

  public String getAppLanguage() {
    return appLanguage;
  }

  public int getMonthsOutdated() {
    return monthsOutdated;
  }

  public String getAppId() {
    return appId;
  }

  public String getAppName() {
    return appName;
  }

  public String getAppContextPath() {
    return appContextPath;
  }
}
