package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2025 Contrast Security, Inc.
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

import com.contrastsecurity.http.RuleSeverity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class TraceFilterBodyTest {

  private final Gson gson = new Gson();

  @Test
  public void agentSessionId_should_be_serialized_to_json() {
    // GIVEN a TraceFilterBody with agentSessionId set
    TraceFilterBody body = new TraceFilterBody();
    body.setAgentSessionId("test-session-id-12345");

    // WHEN serialized to JSON
    String json = gson.toJson(body);
    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

    // THEN the JSON should contain the agentSessionId field
    assertThat(jsonObject.has("agentSessionId")).isTrue();
    assertThat(jsonObject.get("agentSessionId").getAsString()).isEqualTo("test-session-id-12345");
  }

  @Test
  public void agentSessionId_should_not_be_serialized_when_null() {
    // GIVEN a TraceFilterBody without agentSessionId set
    TraceFilterBody body = new TraceFilterBody();
    body.setSeverities(Collections.singletonList(RuleSeverity.HIGH));

    // WHEN serialized to JSON
    String json = gson.toJson(body);
    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

    // THEN the JSON should NOT contain the agentSessionId field (Gson excludes null fields)
    assertThat(jsonObject.has("agentSessionId")).isFalse();
  }

  @Test
  public void agentSessionId_should_work_with_other_filters() {
    // GIVEN a TraceFilterBody with multiple fields including agentSessionId
    TraceFilterBody body = new TraceFilterBody();
    body.setAgentSessionId("session-abc-123");
    body.setSeverities(Collections.singletonList(RuleSeverity.CRITICAL));
    body.setAppVersionTags(Collections.singletonList("v1.0.0"));

    // WHEN serialized to JSON
    String json = gson.toJson(body);
    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

    // THEN all fields should be present in the JSON
    assertThat(jsonObject.has("agentSessionId")).isTrue();
    assertThat(jsonObject.get("agentSessionId").getAsString()).isEqualTo("session-abc-123");
    assertThat(jsonObject.has("severities")).isTrue();
    assertThat(jsonObject.has("appVersionTags")).isTrue();
  }
}
