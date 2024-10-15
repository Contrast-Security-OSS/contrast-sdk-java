package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2024 Contrast Security, Inc.
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
import au.com.dius.pact.consumer.dsl.LambdaDslObject;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.contrastsecurity.PactConstants;
import com.contrastsecurity.TestDataConstants;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.ContrastSDK.Builder;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.google.gson.Gson;
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
  final class CreateProject {

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
          .body(
              new JSONObject()
                  .put("name", "quarkus-test-application")
                  .put("language", "JAVA")
                  .put("includeNamespaceFilters", Collections.singletonList("com.acme"))
                  .put("excludeNamespaceFilters", Collections.singletonList("org.apache")))
          .willRespondWith()
          .status(201)
          .body(
              newJsonBody(
                      project ->
                          project
                              .valueFromProviderState("id", "${id}", "fake-project-id")
                              .valueFromProviderState(
                                  "organizationId", "${organizationId}", "fake-organization-id")
                              .stringValue("name", "quarkus-test-application")
                              .stringType("language", "JAVA")
                              .booleanType("archived", false)
                              .datetime(
                                  "lastScanTime",
                                  PactConstants.DATETIME_FORMAT,
                                  TestDataConstants.TIMESTAMP_EXAMPLE)
                              .numberType("completedScans", 1)
                              .stringType("lastScanId", "scan-id")
                              .numberType("critical", 2)
                              .numberType("high", 3)
                              .numberType("medium", 4)
                              .numberType("low", 5)
                              .numberType("note", 6)
                              .array(
                                  "includeNamespaceFilters", array -> array.stringType("com.acme"))
                              .array(
                                  "excludeNamespaceFilters",
                                  array -> array.stringType("org.apache")))
                  .build())
          .toPact();
    }

    @Test
    void create_project(final MockServer server) throws IOException {
      final ProjectClient client = client(server);
      final ProjectCreate create =
          ProjectCreate.builder()
              .name("quarkus-test-application")
              .language("JAVA")
              .includeNamespaceFilters(Collections.singletonList("com.acme"))
              .excludeNamespaceFilters(Collections.singletonList("org.apache"))
              .build();
      final ProjectInner project = client.create(create);

      final ProjectInner expected =
          ProjectInner.builder()
              .id("fake-project-id")
              .organizationId("fake-organization-id")
              .name("quarkus-test-application")
              .language("JAVA")
              .lastScanId("scan-id")
              .lastScanTime(TestDataConstants.TIMESTAMP_EXAMPLE)
              .completedScans(1)
              .critical(2)
              .high(3)
              .medium(4)
              .low(5)
              .note(6)
              .includeNamespaceFilters(Collections.singletonList("com.acme"))
              .excludeNamespaceFilters(Collections.singletonList("org.apache"))
              .build();
      assertThat(project).isEqualTo(expected);
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
      final ProjectClient client = client(server);
      final Optional<ProjectInner> project = client.findByName("spring-test-application");
      final ProjectInner expected =
          ProjectInner.builder()
              .id("fake-project-id")
              .organizationId("fake-organization-id")
              .name("spring-test-application")
              .archived(false)
              .language("JAVA")
              .critical(1)
              .high(2)
              .medium(3)
              .low(4)
              .note(5)
              .completedScans(6)
              .lastScanTime(TestDataConstants.TIMESTAMP_EXAMPLE)
              .lastScanId("scan-id")
              .includeNamespaceFilters(Collections.singletonList("com.example"))
              .excludeNamespaceFilters(Collections.singletonList("org.apache"))
              .build();
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
      final ProjectClient client = client(server);
      final Optional<ProjectInner> project = client.findByName("does-not-exist");
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
            ZonedDateTime.ofInstant(TestDataConstants.TIMESTAMP_EXAMPLE, ZoneOffset.UTC))
        .numberType("completedScans", 6)
        .stringType("lastScanId", "scan-id")
        .array("includeNamespaceFilters", a -> a.stringType("com.example"))
        .array("excludeNamespaceFilters", a -> a.stringType("org.apache"));
  }

  private static ProjectClient client(final MockServer server) {
    final ContrastSDK contrast =
        new Builder("test-user", "test-service-key", "test-api-key")
            .withApiUrl(server.getUrl())
            .build();
    final Gson gson = GsonFactory.create();
    return new ProjectClientImpl(contrast, gson, "organization-id");
  }
}
