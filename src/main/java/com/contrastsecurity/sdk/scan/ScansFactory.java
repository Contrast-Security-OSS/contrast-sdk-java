package com.contrastsecurity.sdk.scan;

/** Factory for {@link Scans} */
interface ScansFactory {

  /**
   * @param projectId ID of the project in which to manage scans
   * @return new {@link Scans}
   */
  Scans create(String projectId);
}
