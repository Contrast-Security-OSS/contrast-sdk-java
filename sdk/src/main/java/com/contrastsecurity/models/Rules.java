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
    private List<String> languages;
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

    public List<String> getLanguages() {
      return languages;
    }

    public boolean isFree() {
      return free;
    }

    public String getName() {
      return name;
    }
  }
}
