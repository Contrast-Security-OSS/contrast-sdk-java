package com.contrastsecurity.exceptions;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 Contrast Security, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.assertj.core.api.Assertions.assertThat;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for {@link HttpResponseException}.
 *
 * <p>Verifies that the static factory on the exception is capable of capturing debug information
 * from the response. Uses a test HTTP server to serve responses to build real instances of {@code
 * HttpURLConnection}.
 */
final class HttpResponseExceptionTest {

  private HttpServer server;

  @BeforeEach
  void before() {
    try {
      server = HttpServer.create();
    } catch (final IOException e) {
      throw new IllegalStateException("failed to create new server", e);
    }
    final InetSocketAddress address = new InetSocketAddress("localhost", 0);
    server.setExecutor(Executors.newSingleThreadExecutor());
    try {
      server.bind(address, 0);
    } catch (final IOException e) {
      throw new IllegalStateException("failed to bind server to a port", e);
    }
    server.start();
    // spin wait for the operating system to assign a port before returning
    while (server.getAddress().getPort() <= 0) {
      Thread.yield();
    }
  }

  @Test
  void failed_response_no_body() throws IOException {
    // GIVEN response fails with a failure status code and no body
    final int code = 400;
    server.createContext(
        "/fails",
        exchange -> {
          exchange.sendResponseHeaders(code, -1);
          exchange.close();
        });

    // WHEN build new exception from connection
    final HttpURLConnection connection = connect();
    final String message = "Failure Message";
    final HttpResponseException exception =
        HttpResponseException.fromConnection(connection, message);

    // THEN there is no body in the exception details
    assertThat(exception.getCode()).isEqualTo(code);
    assertThat(exception.getStatus()).isEqualTo("Bad Request");
    final String expectedMessage = message + "\nGET /fails\n\n400 Bad Request";
    assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    assertThat(exception.getBody()).isNull();
  }

  @CsvSource({"400, Bad Request", "200, OK"})
  @ParameterizedTest
  void failed_response_with_body(final int code, final String status) throws IOException {
    // GIVEN response includes a body
    final String body = "Someone ripped the hard drive out";
    server.createContext(
        "/fails",
        exchange -> {
          final byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
          exchange.sendResponseHeaders(code, bytes.length);
          try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
          }
          exchange.close();
        });

    // WHEN build new exception from connection
    final HttpURLConnection connection = connect();
    final HttpResponseException exception =
        HttpResponseException.fromConnection(connection, "Failure Message");

    // THEN includes the body in the exception details
    assertThat(exception.getCode()).isEqualTo(code);
    assertThat(exception.getStatus()).isEqualTo(status);
    final String expectedMessage =
        "Failure Message\nGET /fails\n\n" + code + " " + status + "\n\n" + body;
    assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    assertThat(exception.getBody()).isEqualTo(body);
  }

  @CsvSource({"401,Unauthorized", "403,Forbidden"})
  @ParameterizedTest
  void unauthorized_exception(final int code, final String status) throws IOException {
    // GIVEN response fails with a failure status code and no body
    server.createContext(
        "/fails",
        exchange -> {
          exchange.sendResponseHeaders(code, -1);
          exchange.close();
        });

    // WHEN build new exception from connection
    final HttpURLConnection connection = connect();
    final String message = "Failure Message";
    final HttpResponseException exception =
        HttpResponseException.fromConnection(connection, message);

    // THEN builds an UnauthorizedException
    assertThat(exception).isInstanceOf(UnauthorizedException.class);
    assertThat(exception.getCode()).isEqualTo(code);
    assertThat(exception.getStatus()).isEqualTo(status);
    final String expectedMessage = "Failure Message\nGET /fails\n\n" + code + " " + status;
    assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    assertThat(exception.getBody()).isNull();
  }

  @Test
  void resource_not_found_exception() throws IOException {
    // GIVEN response fails with a failure status code and no body
    server.createContext(
        "/fails",
        exchange -> {
          exchange.sendResponseHeaders(404, -1);
          exchange.close();
        });

    // WHEN build new exception from connection
    final HttpURLConnection connection = connect();
    final String message = "Failure Message";
    final HttpResponseException exception =
        HttpResponseException.fromConnection(connection, message);

    // THEN builds a ResourceNotFoundException
    assertThat(exception).isInstanceOf(ResourceNotFoundException.class);
    assertThat(exception.getCode()).isEqualTo(404);
    assertThat(exception.getStatus()).isEqualTo("Not Found");
    assertThat(exception.getMessage()).isEqualTo("Failure Message\nGET /fails\n\n404 Not Found");
    assertThat(exception.getBody()).isNull();
  }

  private HttpURLConnection connect() throws IOException {
    final URL url;
    try {
      url =
          new URL(
              "http", server.getAddress().getHostName(), server.getAddress().getPort(), "/fails");
    } catch (final MalformedURLException e) {
      throw new AssertionError("This should never happen", e);
    }
    return (HttpURLConnection) url.openConnection();
  }

  @AfterEach
  void after() {
    if (server != null) {
      server.stop(0);
    }
  }
}
