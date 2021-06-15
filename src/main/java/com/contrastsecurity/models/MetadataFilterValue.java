package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class MetadataFilterValue {

  @SerializedName("count")
  protected int count = 0;

  public int getCount() {
    return count;
  }
  ;

  // sub field values?

  @SerializedName("value")
  protected String value;

  public String getValue() {
    return value;
  }
  ;
}
