package com.contrastsecurity.http;

import lombok.Getter;

public enum TraceFilterKeycode {
  ALL_ISSUES("00001"),
  CRITICAL_HIGH_SEVERITIES("00002"),
  CURRENT_WEEK("00003"),
  HIGH_CONFIDENCE("00004"),
  OPEN_TRACES("00005"),
  APP_ID("appId"),
  SERVER_ID("serverId"),
  URL("url"),
  RULE_NAME("ruleName");

  @Getter private String label;

  TraceFilterKeycode(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label.toLowerCase();
  }
}
