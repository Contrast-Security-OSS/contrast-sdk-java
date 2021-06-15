package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class Card {

  /**
   * Returns the Card body snippet as a LinkedTreeMap
   *
   * @return Snippet as LinkedTreeMap
   */
  public Object getBody() {
    return body;
  }

  private Object body;

  /**
   * Returns the Card header snippet as a LinkedTreeMap
   *
   * @return Snippet as LinkedTreeMap
   */
  public Object getHeader() {
    return header;
  }

  private Object header;

  /**
   * Hidden status of the Card
   *
   * @return hidden status
   */
  public boolean getIsHidden() {
    return isHidden;
  }

  @SerializedName("is_hidden")
  private boolean isHidden;

  /**
   * Severity level of the Card
   *
   * @return severity level
   */
  public String getSeverity() {
    return severity;
  }

  private String severity;

  /**
   * Card title
   *
   * @return title
   */
  public String getTitle() {
    return title;
  }

  private String title;

  /**
   * Trace id the Card belongs to
   *
   * @return Trace id
   */
  public String getTraceId() {
    return traceId;
  }
  // @SerializedName("trace_id")
  private String traceId;
}
