package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.dsl.PactDslWithState;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.contrastsecurity.PactConstants;
import com.contrastsecurity.TestDataConstants;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/** Pact tests for sast-scan-manager provider. */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "sast-scan-manager")
final class ScansPactTest {

  @Nested
  final class CreateScan {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      final HashMap<String, String> params = new HashMap<>();
      params.put("id", "project-id");
      params.put("organizationId", "organization-id");
      return builder
          .given("Projects Exist", params)
          .uponReceiving("upload new code artifact")
          .method("POST")
          .pathFromProviderState(
              "/sast/organizations/${organizationId}/projects/${projectId}/scans",
              "/sast/organizations/organization-id/projects/project-id/scans")
          .body(new JSONObject().put("label", "main").put("codeArtifactId", "code-artifact-id"))
          .willRespondWith()
          .status(201)
          .body(
              newJsonBody(
                      scan -> {
                        scan.valueFromProviderState("id", "${id}", "scan-id");
                        scan.valueFromProviderState("projectId", "${projectId}", "project-id");
                        scan.valueFromProviderState(
                            "organizationId", "${organizationId}", "organization-id");
                        scan.stringType("status", "FAILED");
                        scan.stringType("errorMessage", "scan failed");
                      })
                  .build())
          .toPact();
    }

    @Test
    void create_scan(final MockServer server) throws IOException {
      final ScanClientImpl client = client(server);
      final ScanCreate create = ScanCreate.of("code-artifact-id", "main");
      final ScanInner scan = client.create("project-id", create);
      final ScanInner expected =
          ScanInner.builder()
              .id("scan-id")
              .projectId("project-id")
              .organizationId("organization-id")
              .status(ScanStatus.FAILED)
              .errorMessage("scan failed")
              .build();
      assertThat(scan).isEqualTo(expected);
    }
  }

  @Nested
  final class GetScan {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      return scanExists(builder)
          .uponReceiving("retrieve scan")
          .method("GET")
          .pathFromProviderState(
              "/sast/organizations/${organizationId}/projects/${projectId}/scans/${scanId}",
              "/sast/organizations/organization-id/projects/project-id/scans/scan-id")
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      scan -> {
                        scan.valueFromProviderState("id", "${id}", "scan-id");
                        scan.valueFromProviderState("projectId", "${projectId}", "project-id");
                        scan.valueFromProviderState(
                            "organizationId", "${organizationId}", "organization-id");
                        scan.stringType("status", "FAILED");
                        scan.stringType("errorMessage", "DNS blew up");
                      })
                  .build())
          .toPact();
    }

    @Test
    void get_scan(final MockServer server) throws IOException {
      final ScanClientImpl client = client(server);
      final ScanInner scan = client.get("project-id", "scan-id");

      final ScanInner expected =
          ScanInner.builder()
              .id("scan-id")
              .projectId("project-id")
              .organizationId("organization-id")
              .status(ScanStatus.FAILED)
              .errorMessage("DNS blew up")
              .build();
      assertThat(scan).isEqualTo(expected);
    }
  }

  @Nested
  final class GetScanSummary {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      return scanExists(builder)
          .uponReceiving("retrieve scan summary")
          .method("GET")
          .pathFromProviderState(
              "/sast/organizations/${organizationId}/projects/${projectId}/scans/${id}/summary",
              "/sast/organizations/organization-id/projects/project-id/scans/scan-id/summary")
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      summary -> {
                        summary.stringType("id", "summary-id");
                        summary.valueFromProviderState("scanId", "${id}", "scan-id");
                        summary.valueFromProviderState("projectId", "${projectId}", "project-id");
                        summary.valueFromProviderState(
                            "organizationId", "${organizationId}", "organization-id");
                        summary.numberType("duration", 100);
                        summary.numberType("totalResults", 10);
                        summary.numberType("totalNewResults", 8);
                        summary.numberType("totalFixedResults", 1);
                        summary.datetime(
                            "lastModifiedDate",
                            PactConstants.DATETIME_FORMAT,
                            LAST_MODIFIED_DATETIME);
                        summary.datetime(
                            "createdDate", PactConstants.DATETIME_FORMAT, CREATED_DATETIME);
                      })
                  .build())
          .toPact();
    }

    @Test
    void get_scan_summary(final MockServer server) throws IOException {
      final ContrastSDK contrast =
          new ContrastSDK.Builder("test-user", "test-service-key", "test-api-key")
              .withApiUrl(server.getUrl())
              .build();
      final Gson gson = GsonFactory.create();
      final ScanClientImpl client = new ScanClientImpl(contrast, gson, "organization-id");
      final ScanSummaryInner summary = client.getSummary("project-id", "scan-id");

      final ScanSummaryInner expected =
          ScanSummaryInner.builder()
              .id("summary-id")
              .scanId("scan-id")
              .projectId("project-id")
              .organizationId("organization-id")
              .duration(100)
              .totalResults(10)
              .totalNewResults(8)
              .totalFixedResults(1)
              .createdDate(CREATED_DATETIME)
              .lastModifiedDate(LAST_MODIFIED_DATETIME)
              .build();
      assertThat(summary).isEqualTo(expected);
    }
  }

  @Nested
  final class GetScanSarif {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      return scanExists(builder)
          .uponReceiving("retrieve scan results in SARIF")
          .method("GET")
          .pathFromProviderState(
              "/sast/organizations/${organizationId}/projects/${projectId}/scans/${id}/raw-output",
              "/sast/organizations/organization-id/projects/project-id/scans/scan-id/raw-output")
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      sarif -> {
                        // we will not specify the SARIF specification here
                      })
                  .build())
          .toPact();
    }

    @Test
    void get_scan_sarif(final MockServer server) throws IOException {
      final ContrastSDK contrast =
          new ContrastSDK.Builder("test-user", "test-service-key", "test-api-key")
              .withApiUrl(server.getUrl())
              .build();
      final Gson gson = GsonFactory.create();
      final ScanClientImpl client = new ScanClientImpl(contrast, gson, "organization-id");

      // instead of validating the entire SARIF specification, verify that some JSON is returned
      final JsonObject object;
      try (InputStream is = client.getSarif("project-id", "scan-id");
          Reader reader = new InputStreamReader(is)) {
        object = gson.fromJson(reader, JsonObject.class);
      }
      assertThat(object).isNotNull();
    }
  }

  private static ScanClientImpl client(final MockServer server) {
    final ContrastSDK contrast =
        new ContrastSDK.Builder("test-user", "test-service-key", "test-api-key")
            .withApiUrl(server.getUrl())
            .build();
    final Gson gson = GsonFactory.create();
    return new ScanClientImpl(contrast, gson, "organization-id");
  }

  private static PactDslWithState scanExists(final PactDslWithProvider builder) {
    final HashMap<String, Object> params = new HashMap<>();
    params.put("id", "scan-id");
    params.put("projectId", "project-id");
    params.put("organizationId", "organization-id");
    return builder.given("Scan Exists", params);
  }

  private static final Instant LAST_MODIFIED_DATETIME = TestDataConstants.TIMESTAMP_EXAMPLE;
  private static final Instant CREATED_DATETIME = LAST_MODIFIED_DATETIME.minus(Duration.ofDays(1));
}
