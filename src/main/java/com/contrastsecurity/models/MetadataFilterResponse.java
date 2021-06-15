package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MetadataFilterResponse {

  public List<MetadataFilterGroup> getFilters() {
    return filters;
  }

  @SerializedName("filters")
  private List<MetadataFilterGroup> filters;
}
