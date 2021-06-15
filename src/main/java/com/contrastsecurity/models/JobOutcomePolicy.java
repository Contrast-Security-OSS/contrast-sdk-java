package com.contrastsecurity.models;

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
