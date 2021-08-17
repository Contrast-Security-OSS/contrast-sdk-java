package com.contrastsecurity.sdk.scan;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.contrastsecurity.PactConstants;
import com.contrastsecurity.TestDataConstants;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.google.gson.Gson;
import java.io.IOException;
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
  final class DefineScan {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      final HashMap<String, Object> params = new HashMap<>();
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
                        scan.stringType("id", "scan-id");
                        scan.stringType("projectId", "project-id");
                        scan.stringType("organizationId", "organization-id");
                        scan.stringType("status", "FAILED");
                        scan.stringType("errorMessage", "scan failed");
                      })
                  .build())
          .toPact();
    }

    @Test
    void define_scan(final MockServer server) throws IOException {
      final ContrastSDK contrast =
          new ContrastSDK.Builder("test-user", "test-service-key", "test-api-key")
              .withApiUrl(server.getUrl())
              .build();
      final Scans scans = contrast.scan("organization-id").scans("project-id");
      final ScanImpl scan =
          (ScanImpl)
              scans
                  .define()
                  .withExistingCodeArtifact("code-artifact-id")
                  .withLabel("main")
                  .create();
      final ScanInner expected =
          ScanInner.builder()
              .id("scan-id")
              .projectId("project-id")
              .organizationId("organization-id")
              .status(ScanStatus.FAILED)
              .errorMessage("scan failed")
              .build();
      assertThat(scan.toInner()).isEqualTo(expected);
    }
  }

  @Nested
  final class GetScan {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      final HashMap<String, Object> params = new HashMap<>();
      params.put("id", "project-id");
      params.put("organizationId", "organization-id");
      return builder
          .given("Projects Exist", params)
          .uponReceiving("retrieve scan")
          .method("GET")
          .pathFromProviderState(
              "/sast/organizations/${organizationId}/projects/${projectId}/scans/${scanId}",
              "/sast/organizations/organization-id/projects/project-id/scans/scan-id")
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      summary -> {
                        summary.stringType("id", "scan-id");
                        summary.stringType("projectId", "project-id");
                        summary.stringType("organizationId", "organization-id");
                        summary.stringType("status", "FAILED");
                        summary.stringType("errorMessage", "DNS blew up");
                      })
                  .build())
          .toPact();
    }

    @Test
    void get_scan(final MockServer server) throws IOException {
      final ContrastSDK contrast =
          new ContrastSDK.Builder("test-user", "test-service-key", "test-api-key")
              .withApiUrl(server.getUrl())
              .build();
      final Gson gson = GsonFactory.create();
      final ScanClientImpl client = new ScanClientImpl(contrast, gson, "organization-id");
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
      final HashMap<String, Object> params = new HashMap<>();
      params.put("id", "project-id");
      params.put("organizationId", "organization-id");
      return builder
          .given("Projects Exist", params)
          .uponReceiving("retrieve scan summary")
          .method("GET")
          .pathFromProviderState(
              "/sast/organizations/${organizationId}/projects/${projectId}/scans/${scanId}/summary",
              "/sast/organizations/organization-id/projects/project-id/scans/scan-id/summary")
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      summary -> {
                        summary.stringType("id", "summary-id");
                        summary.stringType("scanId", "scan-id");
                        summary.stringType("projectId", "project-id");
                        summary.stringType("organizationId", "organization-id");
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

  private static final Instant LAST_MODIFIED_DATETIME = TestDataConstants.TIMESTAMP_EXAMPLE;
  private static final Instant CREATED_DATETIME = LAST_MODIFIED_DATETIME.minus(Duration.ofDays(1));
}
