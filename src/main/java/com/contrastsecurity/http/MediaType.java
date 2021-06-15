package com.contrastsecurity.http;

public enum MediaType {
  JSON("application/json");

  private String type;

  MediaType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
