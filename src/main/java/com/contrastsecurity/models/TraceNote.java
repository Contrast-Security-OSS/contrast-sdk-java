package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class TraceNote {

  public long getCreation() {
    return creation;
  }

  private long creation;

  public String getCreator() {
    return creator;
  }

  private String creator;

  public long getLastModification() {
    return lastModification;
  }

  @SerializedName("last_modification")
  private long lastModification;

  public String getLastUpdater() {
    return lastUpdater;
  }

  @SerializedName("last_updater")
  private String lastUpdater;

  public String getNote() {
    return note;
  }

  private String note;

  public String getId() {
    return id;
  }

  private String id;
}
