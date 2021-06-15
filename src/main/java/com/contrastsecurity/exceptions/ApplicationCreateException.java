package com.contrastsecurity.exceptions;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class ApplicationCreateException extends Exception {
  private final int rc;
  private final String responseMessage;

  public ApplicationCreateException(int rc, String responseMessage) {
    super("Recieved Response code: " + rc + " with message: " + responseMessage);
    this.rc = rc;
    this.responseMessage = responseMessage;
  }

  @Getter(AccessLevel.NONE)
  private static final long serialVersionUID = -9049287248312255189L;
}
