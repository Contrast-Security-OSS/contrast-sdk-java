package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GenericResponse {

  @SerializedName("success")
  protected String success;

  public String getSuccess() {
    return success;
  }
  ;

  public List<String> getMessages() {
    return messages;
  }

  @SerializedName("messages")
  private List<String> messages;
}
