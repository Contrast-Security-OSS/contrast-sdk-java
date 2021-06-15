package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StatusRequest {
  private List<String> traces;
  private String status;
  private String substatus;

  @SerializedName("comment_preference")
  private boolean commentPreference;

  private String note;

  public StatusRequest(List<String> traces, String status, boolean commentPreference) {
    this.traces = traces;
    this.status = status;
    this.commentPreference = commentPreference;
  }

  public StatusRequest(List<String> traces, String status, boolean commentPreference, String note) {
    this.traces = traces;
    this.status = status;
    this.commentPreference = commentPreference;
    this.note = note;
  }

  public StatusRequest(
      List<String> traces,
      String status,
      String substatus,
      boolean commentPreference,
      String note) {
    this.traces = traces;
    this.status = status;
    this.substatus = substatus;
    this.commentPreference = commentPreference;
    this.note = note;
  }

  public StatusRequest() {}

  public List<String> getTraces() {
    return traces;
  }

  public void setTraces(List<String> traces) {
    this.traces = traces;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSubstatus() {
    return substatus;
  }

  public void setSubstatus(String substatus) {
    this.substatus = substatus;
  }

  public boolean isCommentPreference() {
    return commentPreference;
  }

  public void setCommentPreference(boolean commentPreference) {
    this.commentPreference = commentPreference;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
