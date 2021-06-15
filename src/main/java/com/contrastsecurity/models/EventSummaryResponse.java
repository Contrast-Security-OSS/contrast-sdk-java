package com.contrastsecurity.models;

import java.util.List;

public class EventSummaryResponse {
  private boolean success;
  private List<String> messages;
  private String risk;
  private boolean showEvidence;
  private boolean showEvents;
  private List<EventResource> events;

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

  public void setRisk(String risk) {
    this.risk = risk;
  }

  public String getRisk() {
    return this.risk;
  }

  public void setShowEvidence(boolean showEvidence) {
    this.showEvidence = showEvidence;
  }

  public boolean getShowEvidence() {
    return this.showEvidence;
  }

  public void setShowEvents(boolean showEvents) {
    this.showEvents = showEvents;
  }

  public boolean getShowEvents() {
    return this.showEvents;
  }

  public void setEvents(List<EventResource> events) {
    this.events = events;
  }

  public List<EventResource> getEvents() {
    return this.events;
  }
}
