package com.contrastsecurity.sdk.scan;

/** Describes the possible states that a scan can have */
public enum ScanStatus {
  WAITING,
  RUNNING,
  CANCELLED,
  COMPLETED,
  FAILED
}
