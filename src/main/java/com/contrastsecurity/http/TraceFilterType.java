package com.contrastsecurity.http;

import lombok.Getter;

public enum TraceFilterType {
  MODULES("modules"),
  WORKFLOW("workflow"),
  SERVERS("servers"),
  TIME("time"),
  URL("url"),
  VULNTYPE("vulntype"),
  SERVER_ENVIRONMENT("server-environment"),
  APP_VERSION_TAGS("appversiontags");

  @Getter private String label;

  TraceFilterType(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label.toLowerCase();
  }
}
