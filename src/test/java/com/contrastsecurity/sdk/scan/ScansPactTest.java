package com.contrastsecurity.sdk.scan;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.scan.Scan.Status;
import java.io.IOException;
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
    RequestResponsePact pact(final PactDslWithProvider builder) throws IOException {
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
      final Scan scan =
          scans.define().withExistingCodeArtifact("code-artifact-id").withLabel("main").create();
      final ScanImpl.Value value =
          ScanImpl.Value.builder()
              .id("scan-id")
              .projectId("project-id")
              .organizationId("organization-id")
              .status(Status.FAILED)
              .errorMessage("scan failed")
              .build();
      final ScanImpl expected = new ScanImpl(contrast, value);
      assertThat(scan).isEqualTo(expected);
    }
  }
}
