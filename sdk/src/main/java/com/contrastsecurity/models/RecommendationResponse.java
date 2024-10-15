package com.contrastsecurity.models;

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

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecommendationResponse {
  private boolean success;
  private List<String> messages;

  @SerializedName("recommendation")
  private Recommendation recommendation;

  private String owasp;
  private String cwe;

  @SerializedName("custom_recommendation")
  private CustomRecommendation customRecommendation;

  @SerializedName("rule_references")
  private RuleReferences ruleReferences;

  @SerializedName("custom_rule_references")
  private CustomRuleReferences customRuleReferences;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public List<String> getMessages() {
    return messages;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  public Recommendation getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(Recommendation recommendation) {
    this.recommendation = recommendation;
  }

  public String getOwasp() {
    return owasp;
  }

  public void setOwasp(String owasp) {
    this.owasp = owasp;
  }

  public String getCwe() {
    return cwe;
  }

  public void setCwe(String cwe) {
    this.cwe = cwe;
  }

  public CustomRecommendation getCustomRecommendation() {
    return customRecommendation;
  }

  public void setCustomRecommendation(CustomRecommendation customRecommendation) {
    this.customRecommendation = customRecommendation;
  }

  public RuleReferences getRuleReferences() {
    return ruleReferences;
  }

  public void setRuleReferences(RuleReferences ruleReferences) {
    this.ruleReferences = ruleReferences;
  }

  public CustomRuleReferences getCustomRuleReferences() {
    return customRuleReferences;
  }

  public void setCustomRuleReferences(CustomRuleReferences customRuleReferences) {
    this.customRuleReferences = customRuleReferences;
  }
}
