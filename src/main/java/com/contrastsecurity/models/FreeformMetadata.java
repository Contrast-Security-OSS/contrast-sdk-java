package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class FreeformMetadata extends MetadataEntity {

  public FreeformMetadata() {
    type = MetadataType.STRING;
  }

  @SerializedName("fieldValue")
  protected String fieldValue;

  public String getFieldValue() {
    return fieldValue;
  }
  ;
}
