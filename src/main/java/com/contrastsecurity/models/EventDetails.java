package com.contrastsecurity.models;

import java.util.List;

public class EventDetails {
  private boolean success;
  private List<String> messages;
  private Event event;

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public boolean getSuccess() {
    return this.success;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  public List<String> getMessages() {
    return this.messages;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public Event getEvent() {
    return this.event;
  }
}
