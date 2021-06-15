package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class TraceFilter {

  public String getKeycode() {
    return keycode;
  }

  public void setKeycode(String keycode) {
    this.keycode = keycode;
  }

  private String keycode;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  private String label;

  public Object getDetails() {
    return details;
  }

  private Object details;

  public long getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  private long count;

  @SerializedName("new_group")
  private boolean newGroup;

  public boolean getNewGroup() {
    return newGroup;
  }

  public void setNewGroup(boolean newGroup) {
    this.newGroup = newGroup;
  }

  @Override
  public String toString() {
    return keycode;
  }
}
