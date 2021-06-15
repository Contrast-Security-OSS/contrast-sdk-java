package com.contrastsecurity.models;

import java.util.List;

public class StoryResponse {
  private String success;
  private List<String> messages;
  private Story story;

  public StoryResponse() {}

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

  public Story getStory() {
    return story;
  }

  public void setStory(Story story) {
    this.story = story;
  }
}
