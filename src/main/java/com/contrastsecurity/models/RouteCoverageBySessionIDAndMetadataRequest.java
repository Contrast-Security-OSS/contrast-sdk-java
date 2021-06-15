package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class RouteCoverageBySessionIDAndMetadataRequest {

  @SerializedName("sessionID")
  protected String sessionID;

  public String getSessionID() {
    return sessionID;
  }
  ;

  public List<RouteCoverageMetadataLabelValues> getValues() {
    return metadata;
  }

  @SerializedName("metadata")
  private List<RouteCoverageMetadataLabelValues> metadata =
      new ArrayList<RouteCoverageMetadataLabelValues>();
}
