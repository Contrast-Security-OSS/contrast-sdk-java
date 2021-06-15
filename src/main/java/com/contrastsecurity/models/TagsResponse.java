package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TagsResponse {
  private String success;
  private List<String> messages;

  @SerializedName("tags")
  private List<String> tags;

  public String getSuccess() {
    return success;
  }

  public void setSuccess(String success) {
    this.success = success;
  }

  public List<String> getMessages() {
    return messages;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }
}
