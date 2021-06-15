package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class NotificationResource {
  @SerializedName("message")
  protected String message;

  public String getMessage() {
    return message;
  }
  ;

  @SerializedName("read")
  protected boolean read;

  public boolean getRead() {
    return read;
  }
  ;
}
