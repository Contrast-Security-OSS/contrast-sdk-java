package com.contrastsecurity.models;

public class EventItem extends EventModel {

  private String type;
  private String value;
  private boolean isStacktrace;

  public EventItem() {}

  public EventItem(EventResource parent, String type, String value, boolean isStacktrace) {
    super();
    this.type = type;
    this.value = value;
    this.isStacktrace = isStacktrace;
    this.parent = parent;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public boolean isStacktrace() {
    return isStacktrace;
  }

  public void setStacktrace(boolean isStacktrace) {
    this.isStacktrace = isStacktrace;
  }
}
