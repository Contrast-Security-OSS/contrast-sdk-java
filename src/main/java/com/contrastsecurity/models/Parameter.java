package com.contrastsecurity.models;

public class Parameter {
  private String parameter;
  private boolean tracked;

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public boolean isTracked() {
    return tracked;
  }

  public void setTracked(boolean tracked) {
    this.tracked = tracked;
  }
}
