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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

/** Pact tests for sast-code-artifacts provider. */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "sast-code-artifacts")
final class CodeArtifactsPactTest {

  private Path jar;

  /**
   * Creates a test jar for the test to upload as a code artifact
   *
   * @param tmp temporary directory
   */
  @BeforeEach
  void before(@TempDir final Path tmp) throws IOException {
    jar = tmp.resolve("spring-test-application.jar");
    try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jar.toFile()))) {
      jos.putNextEntry(new ZipEntry("HelloWorld.class"));
    }
  }

  /** Verifies the code artifact upload behavior. */
  @Nested
  final class UploadCodeArtifact {

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
              "/sast/organizations/${organizationId}/projects/${projectId}/code-artifacts",
              "/sast/organizations/organization-id/projects/project-id/code-artifacts")
          .withFileUpload(
              "filename",
              jar.getFileName().toString(),
              "application/java-archive",
              Files.readAllBytes(jar))
          .willRespondWith()
          .status(201)
          .body(
              newJsonBody(
                      o -> {
                        o.stringType("id", "code-artifact-id");
                        o.valueFromProviderState("projectId", "${projectId}", "project-id");
                        o.valueFromProviderState(
                            "organizationId", "${organizationId}", "organization-id");
                        o.stringType("filename", jar.getFileName().toString());
                        o.datetime(
                            "createdTime",
                            PactConstants.DATETIME_FORMAT,
                            TestDataConstants.TIMESTAMP_EXAMPLE);
                      })
                  .build())
          .toPact();
    }

    @Test
    void upload_code_artifact(final MockServer server) throws IOException {
      final ContrastSDK contrast =
          new ContrastSDK.Builder("test-user", "test-service-key", "test-api-key")
              .withApiUrl(server.getUrl())
              .build();
      final CodeArtifacts codeArtifacts =
          contrast.scan("organization-id").codeArtifacts("project-id");
      final CodeArtifact codeArtifact = codeArtifacts.upload(jar);

      final CodeArtifactInner expected =
          CodeArtifactInner.builder()
              .id("code-artifact-id")
              .projectId("project-id")
              .organizationId("organization-id")
              .filename(jar.getFileName().toString())
              .createdTime(TestDataConstants.TIMESTAMP_EXAMPLE)
              .build();
      final CodeArtifactInner inner = ((CodeArtifactImpl) codeArtifact).toInner();
      assertThat(inner).isEqualTo(expected);
    }
  }
}
