package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Organizations {
  public Long getCount() {
    return count;
  }

  private Long count = null;

  public List<Organization> getOrganizations() {
    return organizations;
  }

  private List<Organization> organizations;

  public Organization getOrganization() {
    return organization;
  }

  private Organization organization;

  public List<Organization> getOrgDisabled() {
    return orgDisabled;
  }

  @SerializedName("org_disabled")
  private List<Organization> orgDisabled;
}
