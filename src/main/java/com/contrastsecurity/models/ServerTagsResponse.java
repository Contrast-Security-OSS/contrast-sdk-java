package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ServerTagsResponse {

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

  public List<String> getTags() {
    return tags;
  }

  @SerializedName("tags")
  private List<String> tags;
}
