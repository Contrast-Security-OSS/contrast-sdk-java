package com.contrastsecurity.models;

import java.util.Map;

public class CustomRecommendation {

  private String text;
  private String formattedText;
  private Map<String, String> formattedTextVariables;

  public CustomRecommendation() {}

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getFormattedText() {
    return formattedText;
  }

  public void setFormattedText(String formattedText) {
    this.formattedText = formattedText;
  }

  public Map<String, String> getFormattedTextVariables() {
    return formattedTextVariables;
  }

  public void setFormattedTextVariables(Map<String, String> formattedTextVariables) {
    this.formattedTextVariables = formattedTextVariables;
  }
}
