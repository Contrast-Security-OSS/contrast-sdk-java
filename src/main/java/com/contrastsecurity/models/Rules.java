package com.contrastsecurity.models;

import java.util.List;

public class Rules {

  private List<Rule> rules;

  public List<Rule> getRules() {
    return rules;
  }

  public class Rule {
    private String description;
    private String title;
    private String category;
    private String impact;
    private String likelihood;
    private String enabled;
    private String cwe;
    private String owasp;
    private String confidence;
    private String severity;
    private String serviceLevel;
    private List<String> references;
    private boolean free;
    private String name;

    public String getDescription() {
      return description;
    }

    public String getTitle() {
      return title;
    }

    public String getCategory() {
      return category;
    }

    public String getImpact() {
      return impact;
    }

    public String getLikelihood() {
      return likelihood;
    }

    public String getEnabled() {
      return enabled;
    }

    public String getCwe() {
      return cwe;
    }

    public String getOwasp() {
      return owasp;
    }

    public String getConfidence() {
      return confidence;
    }

    public String getSeverity() {
      return severity;
    }

    public String getServiceLevel() {
      return serviceLevel;
    }

    public List<String> getReferences() {
      return references;
    }

    public boolean isFree() {
      return free;
    }

    public String getName() {
      return name;
    }
  }
}
