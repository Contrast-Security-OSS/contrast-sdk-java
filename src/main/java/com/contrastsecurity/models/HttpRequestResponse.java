package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HttpRequestResponse {
  private String success;
  private List<String> messages;

  @SerializedName("http_request")
  private HttpRequest httpRequest;

  private String reason;

  public HttpRequestResponse() {}

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

  public HttpRequest getHttpRequest() {
    return httpRequest;
  }

  public void setHttpRequest(HttpRequest httpRequest) {
    this.httpRequest = httpRequest;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
