package com.contrastsecurity.exceptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Thrown when the Contrast API returns a response that indicates an error has occurred. Captures
 * the HTTP status line and body.
 *
 * <p>Typically, HTTP status codes in the 4XX and 5XX ranges indicate an error; however, in some
 * legacy cases, the Contrast API may return a successful status code (e.g. 200) with an error body.
 * In this case, the Contrast Java SDK still throws this exception.
 */
public class HttpResponseException extends ContrastException {

  /**
   * Static factory method that captures the status line and body of the given {@code
   * HttpURLConnection} to create a new exception.
   *
   * @param connection connection from which to derive status line and body information
   * @param message error message provided by the caller
   * @return new {@code HttpResponseException}
   * @throws IOException when fails to read the status line or body of the response
   */
  public static HttpResponseException fromConnection(
      final HttpURLConnection connection, final String message) throws IOException {
    final int code = connection.getResponseCode();
    final ExceptionFactory factory = factoryFromResponseCode(code);
    final String body = readBody(connection, code);
    final String status = connection.getResponseMessage();
    return factory.create(code, status, message, body);
  }

  /**
   * Maps HTTP response codes to the constructor for the appropriate concrete type.
   *
   * @param code response code
   * @return factory for creating a new concrete type of {@code HttpResponseException}
   */
  private static ExceptionFactory factoryFromResponseCode(final int code) {
    switch (code) {
      case 401:
      case 403:
        return UnauthorizedException::new;
      case 404:
        return ResourceNotFoundException::new;
      default:
        return HttpResponseException::new;
    }
  }

  /**
   * Reads the response body in its entirety. Error responses should always be relatively small, so
   * they should fit into memory.
   *
   * @return body as a string, or {@code null}
   */
  private static String readBody(final HttpURLConnection connection, final int code)
      throws IOException {
    final String body;
    try (InputStream is =
            code >= HttpURLConnection.HTTP_BAD_REQUEST
                ? connection.getErrorStream()
                : connection.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      // check if there is a body to read
      if (is == null) {
        body = null;
      } else {
        // read the entire body, because error responses should always be small and fit into memory
        final byte[] buffer = new byte[4096];
        int read;
        while ((read = is.read(buffer)) > 0) {
          bos.write(buffer, 0, read);
        }
        body = bos.toString(StandardCharsets.UTF_8.name());
      }
    }
    return body;
  }

  /** Functional interface that describes the constructor shared by this class and its subclasses */
  private interface ExceptionFactory {
    HttpResponseException create(int code, String status, String message, String body);
  }

  private final int code;
  private final String status;
  private final String body;

  /**
   * Constructor.
   *
   * @param code code from the status line e.g. 400
   * @param status message from the status line e.g. Bad Request
   * @param message error message provided by the caller
   */
  public HttpResponseException(final int code, final String status, final String message) {
    this(code, status, message, null);
  }

  /**
   * Constructor.
   *
   * @param code code from the status line e.g. 400
   * @param status message from the status line e.g. Bad Request
   * @param message error message provided by the caller
   * @param body the body of the response, or {@code null} if there is no such body
   */
  public HttpResponseException(
      final int code, final String status, final String message, final String body) {
    super(message);
    this.code = code;
    this.status = status;
    this.body = body;
  }

  /** @return code from the status line e.g. 400 */
  public int getCode() {
    return code;
  }

  /** @return message from the status line e.g. Bad Request */
  public String getStatus() {
    return status;
  }

  /** @return the body of the response, or {@code null} if there is no such body */
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
