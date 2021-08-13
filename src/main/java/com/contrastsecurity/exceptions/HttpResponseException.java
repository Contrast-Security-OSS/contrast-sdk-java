package com.contrastsecurity.exceptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class HttpResponseException extends ContrastException {

  public static HttpResponseException fromConnection(
      final HttpURLConnection connection, final String message) throws IOException {
    final int code = connection.getResponseCode();
    final String status = connection.getResponseMessage();
    if (!connection.getDoInput()) {
      return new HttpResponseException(code, status, message);
    }
    final String body;
    try (InputStream is =
            code >= HttpURLConnection.HTTP_BAD_REQUEST
                ? connection.getErrorStream()
                : connection.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      final byte[] buffer = new byte[4096];
      int read;
      while ((read = is.read(buffer)) > 0) {
        bos.write(buffer, 0, read);
      }
      body = bos.toString(StandardCharsets.UTF_8.name());
    }
    return new HttpResponseException(code, status, message, body);
  }

  private final int code;
  private final String status;
  private final String body;

  public HttpResponseException(final int code, final String status, final String message) {
    super(message);
    this.code = code;
    this.status = status;
    this.body = null;
  }

  public HttpResponseException(
      final int code, final String status, final String message, final String body) {
    super(message);
    this.code = code;
    this.status = status;
    this.body = body;
  }

  public int getCode() {
    return code;
  }

  public String getStatus() {
    return status;
  }

  public String getBody() {
    return body;
  }

  @Override
  public String toString() {
    final StringBuilder sb =
        new StringBuilder()
            .append(code)
            .append(" ")
            .append(status)
            .append("\n")
            .append(getMessage());
    if (body != null) {
      sb.append("\n\n").append(body);
    }
    return sb.toString();
  }
}
