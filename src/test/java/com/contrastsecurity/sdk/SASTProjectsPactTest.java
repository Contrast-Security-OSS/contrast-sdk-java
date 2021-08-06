package com.contrastsecurity.sdk;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.models.Project;
import com.contrastsecurity.sdk.ContrastSDK.Builder;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/** Pact tests for sast-projects provider. */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "sast-projects")
final class SASTProjectsPactTest {

  /**
   * Verifies {@link ContrastSDK#findProjectByName(String, String)} behavior when the project
   * exists.
   */
  @Nested
  final class FindProjectByName {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact projects(final PactDslWithProvider builder) {
      return builder
          .given("Projects Exist")
          .uponReceiving("retrieving unique project by name")
          .method("GET")
          .path("/sast/organizations/organization-id/projects")
          .matchQuery("unique", "true")
          .matchQuery("name", "spring-test-application")
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      o -> {
                        o.minMaxArrayLike(
                            "content",
                            1,
                            1,
                            project ->
                                project
                                    .stringValue("id", "project-id")
                                    .stringValue("organizationId", "organization-id")
                                    .stringValue("name", "spring-test-application")
                                    .booleanType("archived", false)
                                    .stringType("language", "JAVA")
                                    .numberType("critical", 1)
                                    .numberType("high", 2)
                                    .numberType("medium", 3)
                                    .numberType("low", 4)
                                    .numberType("note", 5)
                                    .datetime(
                                        "lastScanTime",
                                        "yyyy-MM-dd'T'HH:mm:ss'Z'",
                                        LAST_SCAN_TIME_EXAMPLE.toZonedDateTime())
                                    .numberType("completedScans", 6)
                                    .stringType("lastScanId", "scan-id")
                                    .array(
                                        "includeNamespaceFilters", a -> a.stringType("com.example"))
                                    .array(
                                        "excludeNamespaceFilters",
                                        a -> a.stringType("org.apache")));
                        o.numberType("totalElements", 1);
                      })
                  .build())
          .toPact();
    }

    @Test
    void retrieve_project_by_name(final MockServer server)
        throws UnauthorizedException, IOException {
      final ContrastSDK contrast =
          new Builder("test-user", "test-service-key", "test-api-key")
              .withApiUrl(server.getUrl())
              .build();
      final Project project =
          contrast.findProjectByName("organization-id", "spring-test-application");
      final Project expected =
          Project.builder()
              .id("project-id")
              .organizationId("organization-id")
              .name("spring-test-application")
              .archived(false)
              .language("JAVA")
              .critical(1)
              .high(2)
              .medium(3)
              .low(4)
              .note(5)
              .completedScans(6)
              .lastScanTime(LAST_SCAN_TIME_EXAMPLE)
              .lastScanId("scan-id")
              .includeNamespaceFilters(Collections.singleton("com.example"))
              .excludeNamespaceFilters(Collections.singleton("org.apache"))
              .build();
      assertThat(project).isEqualTo(expected);
    }
  }

  /**
   * Verifies {@link ContrastSDK#findProjectByName(String, String)} when the project does not exist.
   */
  @Nested
  final class FindProjectByNameDoesNotExist {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact projects(final PactDslWithProvider builder) {
      return builder
          .given("Projects Exist")
          .uponReceiving("retrieving project that does not exist by name")
          .method("GET")
          .path("/sast/organizations/organization-id/projects")
          .matchQuery("unique", "true")
          .matchQuery("name", "does-not-exist")
          .willRespondWith()
          .status(200)
          .body(newJsonBody(o -> o.numberValue("totalElements", 0)).build())
          .toPact();
    }

    @Test
    void retrieve_project_does_not_exist_by_name(final MockServer server)
        throws UnauthorizedException, IOException {
      final ContrastSDK contrast =
          new Builder("test-user", "test-service-key", "test-api-key")
              .withApiUrl(server.getUrl())
              .build();
      final Project project = contrast.findProjectByName("organization-id", "does-not-exist");
      assertThat(project).isNull();
    }
  }

  private static final OffsetDateTime LAST_SCAN_TIME_EXAMPLE =
      OffsetDateTime.of(1955, 11, 12, 22, 4, 0, 0, ZoneOffset.UTC);
}
