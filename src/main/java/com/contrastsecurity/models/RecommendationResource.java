/*******************************************************************************
 * Copyright (c) 2017 Contrast Security.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License.
 *
 * The terms of the GNU GPL version 3 which accompanies this distribution
 * and is available at https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * Contributors:
 *     Contrast Security - initial API and implementation
 *******************************************************************************/
package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendationResource {
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
