package com.contrastsecurity.http;

public enum MediaType {
  JSON("application/json; charset=UTF-8");

  private final String type;

  MediaType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
