package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class RouteCoverageMetadataLabelValues {

  @SerializedName("label")
  protected String label;

  public String getLabel() {
    return label;
  }
  ;

  public void setLabel(String label) {
    this.label = label;
  }
  ;

  public List<String> getValues() {
    return values;
  }

  @SerializedName("values")
  private List<String> values = new ArrayList<String>();
}
