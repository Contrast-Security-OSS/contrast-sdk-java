package com.contrastsecurity.models.dtm;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class AttestationCreateRequest {
  @SerializedName("vulnerabilityStatuses")
  @NonNull
  private String[] vulnerabilityStatuses;

  @SerializedName("vulnerabilitySeverities")
  @NonNull
  private String[] vulnerabilitySeverities;

  @SerializedName("vulnerabilityTypes")
  @NonNull
  private String[] vulnerabilityTypes;

  @SerializedName("vulnerabilityTags")
  @NonNull
  private String[] vulnerabilityTags;

  @SerializedName("serverEnvironments")
  @NonNull
  private String[] serverEnvironments;

  @SerializedName("serverTags")
  @NonNull
  private String[] serverTags;

  @SerializedName("complianceReports")
  @NonNull
  private String[] complianceReports;

  @SerializedName("showVulnerabilitiesDetails")
  @NonNull
  private boolean showVulnerabilitiesDetails;

  public AttestationCreateRequest(
      String[] vulnerabilityStatuses,
      String[] vulnerabilitySeverities,
      String[] vulnerabilityTypes,
      String[] vulnerabilityTags,
      String[] serverEnvironments,
      String[] serverTags,
      String[] complianceReports,
      boolean showVulnerabilitiesDetails) {
    this.vulnerabilityStatuses = vulnerabilityStatuses;
    this.vulnerabilitySeverities = vulnerabilitySeverities;
    this.vulnerabilityTypes = vulnerabilityTypes;
    this.vulnerabilityTags = vulnerabilityTags;
    this.serverEnvironments = serverEnvironments;
    this.serverTags = serverTags;
    this.complianceReports = complianceReports;
    this.showVulnerabilitiesDetails = showVulnerabilitiesDetails;
  }
}
