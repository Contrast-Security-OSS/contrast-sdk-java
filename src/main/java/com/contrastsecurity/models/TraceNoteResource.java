package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TraceNoteResource {

  @SerializedName("note")
  protected String note;

  public String getNote() {
    return note;
  }
  ;

  @SerializedName("creator")
  protected String creator;

  public String getCreator() {
    return creator;
  }
  ;

  @SerializedName("creation")
  protected String creation;

  public String getCreation() {
    return creation;
  }
  ;

  public List<NgTraceNoteReadOnlyPropertyResource> getProperties() {
    return properties;
  }

  @SerializedName("properties")
  private List<NgTraceNoteReadOnlyPropertyResource> properties;

  public class NgTraceNoteReadOnlyPropertyResource {

    @SerializedName("name")
    protected String name;

    public String getName() {
      return name;
    }
    ;

    @SerializedName("value")
    protected String value;

    public String getValue() {
      return value;
    }
    ;
  }
}
