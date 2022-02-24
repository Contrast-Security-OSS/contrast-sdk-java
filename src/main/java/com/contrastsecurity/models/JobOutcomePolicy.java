package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
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

import com.contrastsecurity.http.RuleSeverity;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/** A security policy used for verifying an application */
@Getter
public class JobOutcomePolicy {
  public enum Outcome {
    SUCCESS,
    FAIL,
    UNSTABLE
  }

  /**
   * The ID of the job outcome policy.
   *
   * @return The ID of the job outcome policy.
   */
  @SerializedName("policy_id")
  private long id;

  /**
   * The name of the job outcome policy.
   *
   * @return The name of the job outcome policy.
   */
  @SerializedName("name")
  private String name;

  /**
   * Whether the threshold was set of all rules.
   *
   * @return whether the threshold was set for all rules.
   */
  @SerializedName("all_rules")
  private boolean allRules;

  /**
   * The threshold for all rules.
   *
   * @return The threshold for all rules.
   */
  @SerializedName("all_rules_threshold")
  private long allRulesThreshold;

  /**
   * List of specific rules and their thresholds.
   *
   * @return List of rules and their thresholds.
   */
  @SerializedName("rules")
  private Map<String, Long> rules;

  /**
   * List of vulnerability statuses that were considered.
   *
   * @return List of vulnerability statuses that were considered.
   */
  @SerializedName("status_filter")
  private List<String> statusFilter;

  /**
   * List of severities and their thresholds.
   *
   * @return List of severities and their thresholds.
   */
  @SerializedName("severities")
  private Map<RuleSeverity, Long> severities;

  /**
   * Result of the job if the application fails the job outcome policy.
   *
   * @return result of the job if the application fails the job outcome policy.
   */
  @SerializedName("outcome")
  private Outcome outcome;
}
