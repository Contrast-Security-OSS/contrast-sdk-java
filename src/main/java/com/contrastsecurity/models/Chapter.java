package com.contrastsecurity.models;

import java.util.List;
import java.util.Map;

public class Chapter {
  private String type;
  private String introText;
  private String introTextFormat;
  private Map<String, String> introTextVariables;
  private String body;
  private String bodyFormat;
  private Map<String, String> bodyFormatVariables;
  private List<PropertyResource> propertyResources;

  public Chapter() {}

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getIntroText() {
    return introText;
  }

  public void setIntroText(String introText) {
    this.introText = introText;
  }

  public String getIntroTextFormat() {
    return introTextFormat;
  }

  public void setIntroTextFormat(String introTextFormat) {
    this.introTextFormat = introTextFormat;
  }

  public Map<String, String> getIntroTextVariables() {
    return introTextVariables;
  }

  public void setIntroTextVariables(Map<String, String> introTextVariables) {
    this.introTextVariables = introTextVariables;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getBodyFormat() {
    return bodyFormat;
  }

  public void setBodyFormat(String bodyFormat) {
    this.bodyFormat = bodyFormat;
  }

  public Map<String, String> getBodyFormatVariables() {
    return bodyFormatVariables;
  }

  public void setBodyFormatVariables(Map<String, String> bodyFormatVariables) {
    this.bodyFormatVariables = bodyFormatVariables;
  }

  public List<PropertyResource> getPropertyResources() {
    return propertyResources;
  }

  public void setPropertyResources(List<PropertyResource> propertyResources) {
    this.propertyResources = propertyResources;
  }
}
