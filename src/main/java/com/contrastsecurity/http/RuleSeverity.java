package com.contrastsecurity.http;

import lombok.Getter;

public enum RuleSeverity {
  NOTE("Note"),
  LOW("Low"),
  MEDIUM("Medium"),
  HIGH("High"),
  CRITICAL("Critical");

  @Getter private String label;

  RuleSeverity(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label.toUpperCase();
  }
}
