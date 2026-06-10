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
package com.contrastsecurity.sdk.graph;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.JSON;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "adr-explorer-aggregator")
final class ContrastGraphApiImplPactTest {

  private static final String ORG_ID = "119844af-42ff-4293-b06b-81d426e9a4a9";
  private static final String APP_ID = "123e4567-e89b-12d3-a456-426614174000";
  private static final String INCIDENT_ID = "INC-2025-1";
  private static final String LIBRARY_HASH =
      "3a42c503953909637f78dd8c99b3b85ddde362415585afc11901bdefe8349102";
  private static final String AGENT_INSTANCE_ID = "abc1234";

  private ContrastGraphApiImpl client(final MockServer server) {
    ContrastSDK sdk =
        new ContrastSDK.Builder("user", "serviceKey", "apiKey")
            .withApiUrl(server.getUrl() + "/Contrast/api")
            .build();
    return new ContrastGraphApiImpl(sdk, JSON.getGson());
  }

  @Nested
  final class SearchGraph {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      return builder
          .given("graph data exists for organization")
          .uponReceiving("search graph with filters")
          .method("POST")
          .path("/api/v2/organizations/" + ORG_ID + "/contrast-graph")
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      body -> {
                        body.array(
                            "nodes",
                            nodes ->
                                nodes.object(
                                    node -> {
                                      node.stringValue("id", "node-1");
                                      node.stringValue("nodeType", "APPLICATION");
                                      node.stringValue("name", "test-app");
                                    }));
                        body.array("edges", edges -> {});
                        body.array(
                            "graphAssets",
                            assets ->
                                assets.object(
                                    asset -> {
                                      asset.stringValue("assetType", "APPLICATION");
                                      asset.stringValue("nodeId", "node-1");
                                      asset.stringValue("assetName", "test-app");
                                      asset.stringValue("assetId", APP_ID);
                                    }));
                      })
                  .build())
          .toPact();
    }

    @Test
    void searchGraph(final MockServer server) throws IOException {
      ContrastGraphRequest request = new ContrastGraphRequest();
      request.setFilters(new RequestFilters());

      ContrastGraphResponse response = client(server).searchGraph(ORG_ID, request);

      assertThat(response.getNodes()).isNotEmpty();
      assertThat(response.getNodes().get(0).getId()).isEqualTo("node-1");
      assertThat(response.getNodes().get(0).getNodeType()).isEqualTo("APPLICATION");
      assertThat(response.getGraphAssets()).isNotEmpty();
      assertThat(response.getGraphAssets().get(0).getAssetId()).isEqualTo(APP_ID);
    }
  }

  @Nested
  final class GetIncidentGraph {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      return builder
          .given("incident exists", Collections.singletonMap("incidentId", INCIDENT_ID))
          .uponReceiving("get incident graph")
          .method("GET")
          .path(
              "/api/v2/organizations/"
                  + ORG_ID
                  + "/contrast-graph/incidents/"
                  + INCIDENT_ID)
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      body -> {
                        body.array(
                            "nodes",
                            nodes ->
                                nodes.object(
                                    node -> {
                                      node.stringValue("id", "incident-node-1");
                                      node.stringValue("nodeType", "ACTION");
                                    }));
                        body.array("edges", edges -> {});
                      })
                  .build())
          .toPact();
    }

    @Test
    void getIncidentGraph(final MockServer server) throws IOException {
      ContrastGraphResponse response = client(server).getIncidentGraph(ORG_ID, INCIDENT_ID);

      assertThat(response.getNodes()).isNotEmpty();
      assertThat(response.getNodes().get(0).getId()).isEqualTo("incident-node-1");
      assertThat(response.getNodes().get(0).getNodeType()).isEqualTo("ACTION");
    }
  }

  @Nested
  final class GetFacets {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      return builder
          .given("graph data exists for organization")
          .uponReceiving("get nodeTypes facets")
          .method("POST")
          .path("/api/v2/organizations/" + ORG_ID + "/contrast-graph/facets/nodeTypes")
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      body ->
                          body.array(
                              "nodeTypes",
                              facets ->
                                  facets.object(
                                      facet -> {
                                        facet.stringValue("value", "APPLICATION");
                                        facet.numberValue("count", 5);
                                      })))
                  .build())
          .toPact();
    }

    @Test
    void getFacets(final MockServer server) throws IOException {
      FacetsResponse response = client(server).getFacets(ORG_ID, "nodeTypes", new RequestFilters());

      assertThat(response.getNodeTypes()).isNotEmpty();
      assertThat(response.getNodeTypes().get(0).getValue()).isEqualTo("APPLICATION");
      assertThat(response.getNodeTypes().get(0).getCount()).isEqualTo(5);
    }
  }

  @Nested
  final class GetApplicationLibraries {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      return builder
          .given("application libraries exist")
          .uponReceiving("get application libraries without filter")
          .method("POST")
          .path(
              "/api/v2/organizations/"
                  + ORG_ID
                  + "/contrast-graph/applications/"
                  + APP_ID
                  + "/libraries")
          .query("agentReportingInstanceId=" + AGENT_INSTANCE_ID)
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      body -> {
                        body.array(
                            "dependencies",
                            deps ->
                                deps.object(
                                    dep -> {
                                      dep.stringValue("name", "express");
                                      dep.stringValue("version", "4.18.2");
                                      dep.stringValue("hash", LIBRARY_HASH);
                                    }));
                        body.array("cves", cves -> {});
                      })
                  .build())
          .toPact();
    }

    @Test
    void getApplicationLibraries(final MockServer server) throws IOException {
      ApplicationLibrariesResponse response =
          client(server).getApplicationLibraries(ORG_ID, APP_ID, AGENT_INSTANCE_ID, null);

      assertThat(response.getDependencies()).isNotEmpty();
      assertThat(response.getDependencies().get(0).getName()).isEqualTo("express");
      assertThat(response.getDependencies().get(0).getVersion()).isEqualTo("4.18.2");
      assertThat(response.getDependencies().get(0).getHash()).isEqualTo(LIBRARY_HASH);
    }
  }

  @Nested
  final class GetApplicationLibraryDetails {

    @Pact(consumer = "contrast-sdk")
    RequestResponsePact pact(final PactDslWithProvider builder) {
      return builder
          .given("library exists")
          .uponReceiving("get library details by hash")
          .method("GET")
          .path(
              "/api/v2/organizations/"
                  + ORG_ID
                  + "/contrast-graph/applications/"
                  + APP_ID
                  + "/libraries/"
                  + LIBRARY_HASH)
          .willRespondWith()
          .status(200)
          .body(
              newJsonBody(
                      body -> {
                        body.stringValue("name", "express");
                        body.stringValue("language", "node");
                        body.stringValue("version", "4.18.2");
                        body.stringValue("fileName", "express-4.18.2.tgz");
                        body.stringValue("hash", LIBRARY_HASH);
                      })
                  .build())
          .toPact();
    }

    @Test
    void getApplicationLibraryDetails(final MockServer server) throws IOException {
      LibraryDetailsResponse response =
          client(server).getApplicationLibraryDetails(ORG_ID, APP_ID, LIBRARY_HASH);

      assertThat(response.getName()).isEqualTo("express");
      assertThat(response.getVersion()).isEqualTo("4.18.2");
      assertThat(response.getHash()).isEqualTo(LIBRARY_HASH);
      assertThat(response.getLanguage()).isEqualTo("node");
    }
  }
}
