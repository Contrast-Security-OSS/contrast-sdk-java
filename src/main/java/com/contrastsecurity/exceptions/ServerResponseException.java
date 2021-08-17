package com.contrastsecurity.exceptions;

/** Thrown when the Contrast API returns a response that the SDK does not understand. */
public class ServerResponseException extends ContrastException {

  /** @see RuntimeException#RuntimeException(String) */
  public ServerResponseException(final String message) {
    super(message);
  }

  /** @see RuntimeException#RuntimeException(String, Throwable) */
  public ServerResponseException(final String message, final Throwable inner) {
    super(message, inner);
  }
}
