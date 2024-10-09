package com.contrastsecurity.http;

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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryFilterForm extends FilterForm {

  public enum LibraryExpandValues {
    VULNS,
    APPS,
    SERVERS,
    MANIFEST,
    STATUS,
    SKIP_LINKS;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  public enum LibraryQuickFilterType {
    ALL,
    VULNERABLE,
    VIOLATION,
    PRIVATE,
    PUBLIC,
    HIGH_RISK;

    @Override
    public String toString() {
      return name();
    }
  }

  private List<String> apps;
  private List<Long> servers;
  private List<String> tags;
  private String q;
  private List<String> languages;
  private List<String> licenses;
  private List<String> grades;
  private LibraryQuickFilterType quickFilter;
  private boolean includeUsed;
  private boolean includeUnused;

  public LibraryFilterForm() {
    super();

    this.apps = new ArrayList<>();
    this.servers = new ArrayList<>();
    this.tags = new ArrayList<>();
    this.q = "";
    this.languages = new ArrayList<>();
    this.licenses = new ArrayList<>();
    this.grades = new ArrayList<>();
    this.quickFilter = null;
    this.includeUsed = false;
    this.includeUnused = false;
  }

  public List<String> getApps() {
    return apps;
  }

  public void setApps(List<String> apps) {
    this.apps = apps;
  }

  public List<Long> geServers() {
    return servers;
  }

  public void setServers(List<Long> servers) {
    this.servers = servers;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

  public List<String> getLanguages() {
    return languages;
  }

  public void setLanguages(List<String> languages) {
    this.languages = languages;
  }

  public List<String> getLicenses() {
    return licenses;
  }

  public void setLicenses(List<String> licenses) {
    this.licenses = licenses;
  }

  public List<String> getGrades() {
    return grades;
  }

  public void setGrades(List<String> grades) {
    this.grades = grades;
  }

  public LibraryQuickFilterType getQuickFilter() {
    return quickFilter;
  }

  public void setQuickFilter(LibraryQuickFilterType quickFilter) {
    this.quickFilter = quickFilter;
  }

  public boolean isIncludeUsed() {
    return includeUsed;
  }

  public void setIncludeUsed(boolean includeUsed) {
    this.includeUsed = includeUsed;
  }

  public boolean isIncludeUnused() {
    return includeUnused;
  }

  public void setIncludeUnused(boolean includeUnused) {
    this.includeUnused = includeUnused;
  }

  @Override
  public String toString() {
    String formString = super.toString();

    List<String> filters = new ArrayList<>();

    if (!apps.isEmpty()) {
      filters.add("apps=" + String.join(",", apps));
    }

    if (!servers.isEmpty()) {
      filters.add(
          "servers=" + servers.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    if (!tags.isEmpty()) {
      filters.add("tags=" + String.join(",", tags));
    }

    if (q != null && q.isEmpty()) {
      filters.add("q=" + q);
    }

    if (!languages.isEmpty()) {
      filters.add("languages=" + String.join(",", languages));
    }

    if (!licenses.isEmpty()) {
      filters.add("licenses=" + String.join(",", licenses));
    }

    if (!grades.isEmpty()) {
      filters.add("grades=" + String.join(",", grades));
    }

    if (quickFilter != null) {
      filters.add("quickFilter=" + quickFilter.toString());
    }

    filters.add("includeUsed=" + includeUsed);
    filters.add("includeUnused=" + includeUnused);

    String result;

    if (!filters.isEmpty()) {
      result = String.join("&", filters);
    } else {
      return formString;
    }

    if (formString != null && !formString.isEmpty()) {
      return formString + "&" + result;
    } else {
      return "?" + result;
    }
  }
}
