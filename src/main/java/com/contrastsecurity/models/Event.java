package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Event {
  @SerializedName("object")
  private String fObject;

  private String method;
  private List<Parameter> parameters;
  private List<Stacktrace> stacktraces;

  @SerializedName("class")
  private String clazz;

  @SerializedName("object_tracked")
  private boolean objectTracked;

  @SerializedName("return")
  private String fReturn;

  @SerializedName("return_tracked")
  private boolean returnTracked;

  @SerializedName("last_custom_frame")
  private int lastCustomFrame;

  public String getfObject() {
    return fObject;
  }

  public void setfObject(String fObject) {
    this.fObject = fObject;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public List<Stacktrace> getStacktraces() {
    return stacktraces;
  }

  public void setStacktraces(List<Stacktrace> stacktraces) {
    this.stacktraces = stacktraces;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  public boolean isObjectTracked() {
    return objectTracked;
  }

  public void setObjectTracked(boolean objectTracked) {
    this.objectTracked = objectTracked;
  }

  public String getfReturn() {
    return fReturn;
  }

  public void setfReturn(String fReturn) {
    this.fReturn = fReturn;
  }

  public boolean isReturnTracked() {
    return returnTracked;
  }

  public void setReturnTracked(boolean returnTracked) {
    this.returnTracked = returnTracked;
  }

  public int getLastCustomFrame() {
    return lastCustomFrame;
  }

  public void setLastCustomFrame(int lastCustomFrame) {
    this.lastCustomFrame = lastCustomFrame;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }
}
