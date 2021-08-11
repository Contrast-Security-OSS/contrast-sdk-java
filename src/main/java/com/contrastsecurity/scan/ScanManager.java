package com.contrastsecurity.scan;

public interface ScanManager {

  ScanManager withOrganization(String id);

  Projects projects();
}
