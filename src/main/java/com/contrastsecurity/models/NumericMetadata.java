package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class NumericMetadata extends MetadataEntity {

  public NumericMetadata() {
    type = MetadataType.NUMERIC;
  }

  @SerializedName("fieldValue")
  private Long fieldValue;

  public Long getFieldValue() {
    return fieldValue;
  }
  ;
}
