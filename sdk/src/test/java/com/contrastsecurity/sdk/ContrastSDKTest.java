package com.contrastsecurity.sdk;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2026 Contrast Security, Inc.
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.MediaType;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class ContrastSDKTest {

  private ContrastSDK contrastSDK;

  @BeforeEach
  public void setUp() {
    contrastSDK =
        new ContrastSDK.Builder("test_user", "testApiKey", "testServiceKey")
            .withApiUrl("http://localhost:19080/Contrast/api")
            .withUserAgentProduct(UserAgentProduct.of("INTELLIJ_INTEGRATION", "1.0.0"))
            .build();
  }

  @Test
  public void set_custom_timeouts() throws IOException {
    final int connectionTimeout = 1000;
    final int readTimeout = 4000;
    contrastSDK.setConnectionTimeout(connectionTimeout);
    contrastSDK.setReadTimeout(readTimeout);

    URLConnection conn =
        contrastSDK.makeConnection("https://contrastsecurity.com", HttpMethod.GET.toString());

    assertThat(conn.getConnectTimeout()).isEqualTo(connectionTimeout);
    assertThat(conn.getReadTimeout()).isEqualTo(readTimeout);
  }

  @Test
  public void set_custom_timeouts_ignores_negative_values() throws IOException {
    final int connectionTimeout = -10;
    final int readTimeout = -50;

    contrastSDK.setConnectionTimeout(connectionTimeout);
    contrastSDK.setReadTimeout(readTimeout);

    URLConnection conn =
        contrastSDK.makeConnection("https://contrastsecurity.com", HttpMethod.GET.toString());

    assertThat(conn.getConnectTimeout()).isNotEqualTo(connectionTimeout);
    assertThat(conn.getReadTimeout()).isNotEqualTo(readTimeout);
  }

  void builds_default_user_agent() {
    // WHEN build user-agent string with null major product
    final String ua = ContrastSDK.buildUserAgent(null);

    // THEN user-agent string contains only the contrast-sdk-java and Java products
    // does not verify version, because that would lead to a brittle test
    assertThat(ua).matches("contrast-sdk-java/\\d\\.\\d(\\.\\d)?(-SNAPSHOT)? Java/\\d+.*");
  }

  @Test
  void builds_user_agent_with_custom_product() {
    // WHEN build a user-agent string with a custom product provided by the user
    final UserAgentProduct product =
        UserAgentProduct.of("contrast-maven-plugin", "3.2.0", "Apache Maven 3.8.1");
    final String ua = ContrastSDK.buildUserAgent(product);

    // THEN user-agent string contains 3 products contrast-sdk-java
    assertThat(ua)
        .matches(
            "contrast-maven-plugin/3.2.0 \\(Apache Maven 3.8.1\\) contrast-sdk-java/\\d\\.\\d(\\.\\d)?(-SNAPSHOT)? Java/\\d+.*");
  }

  @Test
  void builds_user_agent_with_custom_product_no_version() {
    // WHEN build a user-agent string with a custom product provided by the user
    final UserAgentProduct product = UserAgentProduct.of("contrast-maven-plugin");
    final String ua = ContrastSDK.buildUserAgent(product);

    // THEN user-agent string contains 3 products contrast-sdk-java
    assertThat(ua)
        .matches(
            "contrast-maven-plugin contrast-sdk-java/\\d\\.\\d(\\.\\d)?(-SNAPSHOT)? Java/\\d+.*");
  }

  /** Builds a new {@code HttpURLConnection}, but does not send an outgoing request */
  @Test
  void sets_user_agent_header() throws IOException {
    final HttpURLConnection connection =
        contrastSDK.makeConnection("https://does-not-exist.contrastsecurity.com", "GET");

    final String ua = connection.getRequestProperty("User-Agent");
    assertThat(ua)
        .matches(
            "INTELLIJ_INTEGRATION/1.0.0 contrast-sdk-java/\\d\\.\\d(\\.\\d)?(-SNAPSHOT)? Java/\\d+.*");
  }

  @Nested
  final class MakeRequestToUrl {

    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void before() throws IOException {
      server = HttpServer.create();
      server.setExecutor(Executors.newSingleThreadExecutor());
      server.bind(new InetSocketAddress("localhost", 0), 0);
      server.start();
      baseUrl = "http://localhost:" + server.getAddress().getPort();
    }

    @AfterEach
    void after() {
      server.stop(0);
    }

    @Test
    void returns_response_body_on_success() throws IOException {
      server.createContext(
          "/data",
          exchange -> {
            final byte[] body = "response-body".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
              os.write(body);
            }
          });

      try (InputStream is = contrastSDK.makeRequestToUrl(HttpMethod.GET, baseUrl + "/data")) {
        assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8)).isEqualTo("response-body");
      }
    }

    @Test
    void throws_on_error_response() {
      server.createContext(
          "/data",
          exchange -> {
            exchange.sendResponseHeaders(400, -1);
            exchange.close();
          });

      assertThatThrownBy(
              () -> contrastSDK.makeRequestToUrl(HttpMethod.GET, baseUrl + "/data"))
          .isInstanceOf(HttpResponseException.class);
    }
  }

  @Nested
  final class MakeRequestWithBodyToUrl {

    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void before() throws IOException {
      server = HttpServer.create();
      server.setExecutor(Executors.newSingleThreadExecutor());
      server.bind(new InetSocketAddress("localhost", 0), 0);
      server.start();
      baseUrl = "http://localhost:" + server.getAddress().getPort();
    }

    @AfterEach
    void after() {
      server.stop(0);
    }

    @Test
    void sends_body_and_returns_response() throws IOException {
      final String[] receivedBody = {null};
      server.createContext(
          "/submit",
          exchange -> {
            receivedBody[0] =
                new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            final byte[] response = "ok".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
              os.write(response);
            }
          });

      try (InputStream is =
          contrastSDK.makeRequestWithBodyToUrl(
              HttpMethod.POST, baseUrl + "/submit", "{\"key\":\"value\"}", MediaType.JSON)) {
        assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8)).isEqualTo("ok");
      }
      assertThat(receivedBody[0]).isEqualTo("{\"key\":\"value\"}");
    }

    @Test
    void omits_body_when_null() throws IOException {
      final String[] receivedBody = {null};
      server.createContext(
          "/submit",
          exchange -> {
            receivedBody[0] =
                new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            final byte[] response = "ok".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
              os.write(response);
            }
          });

      try (InputStream is =
          contrastSDK.makeRequestWithBodyToUrl(
              HttpMethod.POST, baseUrl + "/submit", null, MediaType.JSON)) {
        assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8)).isEqualTo("ok");
      }
      assertThat(receivedBody[0]).isEmpty();
    }

    @Test
    void throws_on_error_response() {
      server.createContext(
          "/submit",
          exchange -> {
            exchange.sendResponseHeaders(400, -1);
            exchange.close();
          });

      assertThatThrownBy(
              () ->
                  contrastSDK.makeRequestWithBodyToUrl(
                      HttpMethod.POST, baseUrl + "/submit", "{}", MediaType.JSON))
          .isInstanceOf(HttpResponseException.class);
    }
  }
}
