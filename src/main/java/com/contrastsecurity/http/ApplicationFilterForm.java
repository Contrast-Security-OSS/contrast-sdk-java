package com.contrastsecurity.http;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationFilterForm extends FilterForm {

  public enum ApplicationExpandValues {
    COMPLIANCE_POLICY,
    COVERAGE,
    LICENSE,
    MODULES,
    PRODUCTION_PROTECTED,
    SCORES,
    SKIP_LINKS,
    TECHNOLOGIES,
    TRACE_BREAKDOWN;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  public enum ApplicationQuickFilterType {
    ALL("all"),
    ONLINE("online"),
    OFFLINE("offline"),
    MERGED("merged"),
    LICENSED("licensed"),
    UNLICENSED("unlicensed"),
    HIGH_RISK("high-risk"),
    INCOMPLETE_PROTECTION("incomplete-protection"),
    VIOLATION("violation"),
    ARCHIVED("archived");

    private final String name;

    ApplicationQuickFilterType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  private String filterText;
  private String filterAppCode;
  private List<String> filterServers;
  private List<String> filterTechs;
  private List<String> filterTags;
  private List<String> filterCompliance;
  private List<String> filterLanguages;
  private EnumSet<ServerEnvironment> environment;
  private EnumSet<RuleSeverity> filterVulnSeverities;
  private boolean includeArchived;
  private boolean includeOnlyLicensed;
  private boolean includeMerged;
  private ApplicationQuickFilterType quickFilter;

  public ApplicationFilterForm() {
    super();
    this.filterText = "";
    this.filterAppCode = "";
    this.filterServers = new ArrayList<>();
    this.filterTechs = new ArrayList<>();
    this.filterTags = new ArrayList<>();
    this.filterLanguages = new ArrayList<>();
    this.filterCompliance = new ArrayList<>();
    this.environment = null;
    this.filterVulnSeverities = null;
    this.includeArchived = false;
    this.includeOnlyLicensed = false;
    this.quickFilter = null;
    this.includeMerged = true;
  }

  public String getFilterText() {
    return filterText;
  }

  public void setFilterText(String filterText) {
    this.filterText = filterText;
  }

  public String getFilterAppCode() {
    return filterAppCode;
  }

  public void setFilterAppCode(String filterAppCode) {
    this.filterAppCode = filterAppCode;
  }

  public List<String> getFilterServers() {
    return filterServers;
  }

  public void setFilterServers(List<String> filterServers) {
    this.filterServers = filterServers;
  }

  public List<String> getFilterTechs() {
    return filterTechs;
  }

  public void setFilterTechs(List<String> filterTechs) {
    this.filterTechs = filterTechs;
  }

  public List<String> getFilterTags() {
    return filterTags;
  }

  public void setFilterTags(List<String> filterTags) {
    this.filterTags = filterTags;
  }

  public List<String> getFilterLanguages() {
    return filterLanguages;
  }

  public void setFilterLanguages(List<String> filterLanguages) {
    this.filterLanguages = filterLanguages;
  }

  public List<String> getFilterCompliance() {
    return filterCompliance;
  }

  public void setFilterCompliance(List<String> filterCompliance) {
    this.filterCompliance = filterCompliance;
  }

  public EnumSet<ServerEnvironment> getEnvironment() {
    return environment;
  }

  public void setEnvironment(EnumSet<ServerEnvironment> environment) {
    this.environment = environment;
  }

  public EnumSet<RuleSeverity> getFilterVulnSeverities() {
    return filterVulnSeverities;
  }

  public void setFilterVulnSeverities(EnumSet<RuleSeverity> filterVulnSeverities) {
    this.filterVulnSeverities = filterVulnSeverities;
  }

  public boolean getIncludeArchived() {
    return includeArchived;
  }

  public void setIncludeArchived(boolean includeArchived) {
    this.includeArchived = includeArchived;
  }

  public boolean isIncludeOnlyLicensed() {
    return includeOnlyLicensed;
  }

  public void setIncludeOnlyLicensed(boolean includeOnlyLicensed) {
    this.includeOnlyLicensed = includeOnlyLicensed;
  }

  public ApplicationQuickFilterType getQuickFilter() {
    return quickFilter;
  }

  public void setQuickFilter(ApplicationQuickFilterType quickFilter) {
    this.quickFilter = quickFilter;
  }

  public boolean isIncludeMerged() {
    return includeMerged;
  }

  public void setIncludeMerged(boolean includeMerged) {
    this.includeMerged = includeMerged;
  }

  @Override
  public String toString() {
    String formString = super.toString();

    List<String> filters = new ArrayList<>();

    if ((filterText != null && !filterText.isEmpty())) {
      filters.add("filterText=" + filterText);
    }

    if ((filterAppCode != null && !filterAppCode.isEmpty())) {
      filters.add("filterAppCode=" + filterAppCode);
    }

    if (!filterServers.isEmpty()) {
      filters.add("filterServers=" + String.join(",", filterServers));
    }

    if (!filterTechs.isEmpty()) {
      filters.add("filterTechs=" + String.join(",", filterTechs));
    }

    if (!filterTags.isEmpty()) {
      filters.add("filterTags=" + String.join(",", filterTags));
    }

    if (!filterCompliance.isEmpty()) {
      filters.add("filterCompliance=" + String.join(",", filterCompliance));
    }

    if (!filterLanguages.isEmpty()) {
      filters.add("filterLanguages=" + String.join(",", filterLanguages));
    }

    if (environment != null && !environment.isEmpty()) {
      final Set<String> environments =
          environment.stream().map(ServerEnvironment::toURIString).collect(Collectors.toSet());
      filters.add("environments=" + String.join(",", environments));
    }

    if (filterVulnSeverities != null && !filterVulnSeverities.isEmpty()) {
      final Set<String> severities =
          filterVulnSeverities.stream().map(Object::toString).collect(Collectors.toSet());
      filters.add("filterVulnSeverities=" + String.join(",", severities));
    }

    filters.add("includeArchived=" + includeArchived);
    filters.add("includeOnlyLicensed=" + includeOnlyLicensed);
    filters.add("includeMerged=" + includeMerged);

    if (quickFilter != null) {
      filters.add("quickFilter=" + quickFilter.toString());
    }

    String result;

    if (!filters.isEmpty()) {
      result = String.join("&", filters);
    } else {
      return formString;
    }

    if ((formString != null && !formString.isEmpty())) {
      return formString + "&" + result;
    } else {
      return "?" + result;
    }
  }
}
