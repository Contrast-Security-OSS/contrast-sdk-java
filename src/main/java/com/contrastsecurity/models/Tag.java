package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

/** An Tag to delete. */
public class Tag {
  @SerializedName("tag")
  private String name;

  public String getName() {
    if (name != null) {
      return name;
    } else {
      return "testFailure";
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public Tag(String name) {
    this.name = name;
  }

  public Tag() {
    this.name = "";
  }
}
