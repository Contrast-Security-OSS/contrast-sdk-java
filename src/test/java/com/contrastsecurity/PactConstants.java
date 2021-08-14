package com.contrastsecurity;

/** Constants to reuse in Pact tests. */
public final class PactConstants {

  /**
   * Datetime format string to use for Pact datetime matchers. Matches both ISO8601 datetime formats
   * returned by the scan API.
   */
  public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX";

  /** static members only */
  private PactConstants() {}
}
