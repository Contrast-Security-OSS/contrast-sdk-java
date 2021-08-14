package com.contrastsecurity;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/** Constants to reuse in Pact tests. */
public final class TestDataConstants {

  /**
   * Timestamp to use in Pact testing examples. Uses a date far in the past to easily distinguish
   * this from production data.
   */
  public static final Instant TIMESTAMP_EXAMPLE =
      OffsetDateTime.of(1955, 11, 12, 22, 4, 0, 0, ZoneOffset.UTC).toInstant();

  private TestDataConstants() {}
}
