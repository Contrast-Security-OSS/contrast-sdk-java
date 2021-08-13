package com.contrastsecurity.sdk.scan;

public interface Scans {

  Scan.Definition define();

  Scan get(String id);
}
