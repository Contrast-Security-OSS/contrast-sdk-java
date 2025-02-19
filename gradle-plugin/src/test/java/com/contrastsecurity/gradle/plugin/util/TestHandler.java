package com.contrastsecurity.gradle.plugin.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class TestHandler implements HttpHandler {

  final byte[] response;

  public TestHandler(final byte[] response) {
    this.response = response;
  }

  @Override
  public void handle(final HttpExchange exchange) throws IOException {
    exchange.sendResponseHeaders(200, response.length);
    OutputStream os = exchange.getResponseBody();
    os.write(response);
    os.close();
  }
}
