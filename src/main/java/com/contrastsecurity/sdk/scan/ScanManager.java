package com.contrastsecurity.sdk.scan;

public interface ScanManager {

  ScanManager withOrganization(String id);

  Projects projects();
}
