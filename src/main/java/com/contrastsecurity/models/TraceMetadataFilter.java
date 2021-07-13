package com.contrastsecurity.models;

import java.util.Arrays;
import java.util.List;

public class TraceMetadataFilter {
  private String fieldID;
  private List<String> values;

  public TraceMetadataFilter(String fieldID, String... values) {
    this.fieldID = fieldID;
    this.values = Arrays.asList(values);
  }

  public TraceMetadataFilter(String fieldID, List<String> values) {
    this.fieldID = fieldID;
    this.values = values;
  }

  public String getFieldID() {
    return this.fieldID;
  }

  public void setFieldID(String fieldID) {
    this.fieldID = fieldID;
  }

  public List<String> getValues() {
    return this.values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }
}
