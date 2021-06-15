package com.contrastsecurity.http;

import lombok.Getter;

public enum ServerEnvironment {
  DEVELOPMENT("Development"),
  QA("QA"),
  PRODUCTION("Production");

  @Getter private String label;

  ServerEnvironment(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label.toLowerCase();
  }
}
