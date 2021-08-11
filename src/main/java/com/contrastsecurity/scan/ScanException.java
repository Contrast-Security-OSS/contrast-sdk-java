package com.contrastsecurity.scan;

import java.util.Objects;

/** Indicates a condition that prevents Contrast Scan from completing an analysis */
public class ScanException extends RuntimeException {

  private final Scan scan;

  /**
   * @param scan the scan that cannot be completed
   * @param message exception message
   */
  public ScanException(final Scan scan, final String message) {
    super(message);
    this.scan = Objects.requireNonNull(scan);
  }

  /** @return the scan that cannot be completed */
  public final Scan getScan() {
    return scan;
  }
}
