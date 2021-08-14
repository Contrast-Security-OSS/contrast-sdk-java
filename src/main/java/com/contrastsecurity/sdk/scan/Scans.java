package com.contrastsecurity.sdk.scan;

import java.io.IOException;

public interface Scans {

  Scan.Definition define();

  Scan get(String id) throws IOException;
}
