package com.contrastsecurity.sdk.scan;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.LambdaDslObject;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.contrastsecurity.PactConstants;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.ContrastSDK.Builder;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/** Pact tests for sast-projects provider. */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "sast-projects")
final class ProjectsPactTest {

  @Nested
  final class DefineProject {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      final HashMap<String, Object> params = new HashMap<>();
      params.put("id", "fake-project-id");
      params.put("organizationId", "fake-organization-id");
      return builder
          .given("Projects Exist", params)
          .uponReceiving("define project that does not yet exist")
          .method("POST")
          .pathFromProviderState(
              "/sast/organizations/${organizationId}/projects",
              "/sast/organizations/organization-id/projects")
          .body(new JSONObject().put("name", "quarkus-test-application").put("language", "JAVA"))
          .willRespondWith()
          .status(201)
          .body(
              newJsonBody(
                      project -> {
                        project
                            .valueFromProviderState("id", "${id}", "fake-project-id")
                            .valueFromProviderState(
                                "organizationId", "${organizationId}", "fake-organization-id")
                            .stringValue("name", "quarkus-test-application")
                            .stringType("language", "JAVA");
                      })
                  .build())
          .toPact();
    }

    @Test
    void define_project(final MockServer server) throws IOException {
      final ContrastSDK contrast =
          new Builder("test-user", "test-service-key", "test-api-key")
              .withApiUrl(server.getUrl())
              .build();

      final Projects projects = contrast.scan("organization-id").projects();
      final Project project =
          projects.define().withName("quarkus-test-application").withLanguage("JAVA").create();
      assertThat(project.name()).isEqualTo("quarkus-test-application");
      assertThat(project.language()).isEqualTo("JAVA");
    }
  }

  /** Verifies {@link Projects#} behavior when the project exists. */
  @Nested
  final class FindProjectByName {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      final Map<String, Object> params = new HashMap<>();
      params.put("id", "fake-project-id");
      params.put("organizationId", "fake-organization-id");
      return builder
          .given("Projects Exist", params)
          .uponReceiving("retrieving unique project by name")
          .method("GET")
          .pathFromProviderState(
              "/sast/organizations/${organizationId}/projects",
              "/sast/organizations/organization-id/projects")
          .matchQuery("unique", "true")
          .matchQuery("name", "spring-test-application")
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      o -> {
                        o.minMaxArrayLike("content", 1, 1, ProjectsPactTest::describeProject);
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
      final Projects projects = contrast.scan("organization-id").projects();
      final Optional<Project> project = projects.findByName("spring-test-application");
      final Project expected = createExpectedProject(contrast);
      assertThat(project).contains(expected);
    }
  }

  /** Verifies {@link Projects#findByName(String)} when the project does not exist. */
  @Nested
  final class FindProjectByNameDoesNotExist {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      final HashMap<String, Object> params = new HashMap<>();
      params.put("id", "fake-project-id");
      params.put("organizationId", "fake-organization-id");
      return builder
          .given("Projects Exist", params)
          .uponReceiving("retrieving project that does not exist by name")
          .method("GET")
          .pathFromProviderState(
              "/sast/organizations/${organizationId}/projects",
              "/sast/organizations/organization-id/projects")
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
      final Projects projects = contrast.scan("organization-id").projects();
      final Optional<Project> project = projects.findByName("does-not-exist");
      assertThat(project).isEmpty();
    }
  }

  private static void describeProject(LambdaDslObject project) {
    project
        .valueFromProviderState("id", "${id}", "fake-project-id")
        .valueFromProviderState("organizationId", "${organizationId}", "fake-organization-id")
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
            PactConstants.DATETIME_FORMAT,
            ZonedDateTime.ofInstant(PactConstants.TIMESTAMP_EXAMPLE, ZoneOffset.UTC))
        .numberType("completedScans", 6)
        .stringType("lastScanId", "scan-id")
        .array("includeNamespaceFilters", a -> a.stringType("com.example"))
        .array("excludeNamespaceFilters", a -> a.stringType("org.apache"));
  }

  private static Project createExpectedProject(final ContrastSDK contrast) {
    return new ProjectImpl(contrast)
        .setId("fake-project-id")
        .setOrganizationId("fake-organization-id")
        .setName("spring-test-application")
        .setArchived(false)
        .setLanguage("JAVA")
        .setCritical(1)
        .setHigh(2)
        .setMedium(3)
        .setLow(4)
        .setNote(5)
        .setCompletedScans(6)
        .setLastScanTime(PactConstants.TIMESTAMP_EXAMPLE)
        .setLastScanId("scan-id")
        .setIncludeNamespaceFilters(Collections.singletonList("com.example"))
        .setExcludeNamespaceFilters(Collections.singletonList("org.apache"));
  }
}
