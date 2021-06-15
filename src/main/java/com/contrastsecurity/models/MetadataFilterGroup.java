package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MetadataFilterGroup {

  @SerializedName("label")
  protected String label;

  public String getLabel() {
    return label;
  }
  ;

  @SerializedName("id")
  protected String id;

  public String getId() {
    return id;
  }
  ;

  // fieldType STRING,NUMERIC,CONTACT_NAME,PHONE,EMAIL,PERSON_OF_CONTACT

  public List<MetadataFilterValue> getValues() {
    return values;
  }

  @SerializedName("values")
  private List<MetadataFilterValue> values;
}
