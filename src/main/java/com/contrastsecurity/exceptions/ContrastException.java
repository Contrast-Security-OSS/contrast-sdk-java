package com.contrastsecurity.exceptions;

/**
 * Generic {@link RuntimeException} thrown by Contrast code to indicate an unexpected error has
 * occurred.
 */
public class ContrastException extends RuntimeException {

  /** @see RuntimeException#RuntimeException(String) */
  public ContrastException(final String message) {
    super(message);
  }

  /** @see RuntimeException#RuntimeException(String, Throwable) */
  public ContrastException(final String message, final Throwable inner) {
    super(message, inner);
  }
}
