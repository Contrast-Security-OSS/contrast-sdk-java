package com.contrastsecurity.http;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class LibraryFilterForm extends FilterForm {

  public enum LibraryExpandValues {
    VULNS,
    APPS,
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
      filters.add("apps=" + StringUtils.join(apps, ","));
    }

    if (!servers.isEmpty()) {
      filters.add("servers=" + StringUtils.join(servers, ","));
    }

    if (!tags.isEmpty()) {
      filters.add("tags=" + StringUtils.join(tags, ","));
    }

    if (!StringUtils.isEmpty(q)) {
      filters.add("q=" + q);
    }

    if (!languages.isEmpty()) {
      filters.add("languages=" + StringUtils.join(languages, ","));
    }

    if (!licenses.isEmpty()) {
      filters.add("licenses=" + StringUtils.join(licenses, ","));
    }

    if (!grades.isEmpty()) {
      filters.add("grades=" + StringUtils.join(grades, ","));
    }

    if (quickFilter != null) {
      filters.add("quickFilter=" + quickFilter.toString());
    }

    filters.add("includeUsed=" + includeUsed);
    filters.add("includeUnused=" + includeUnused);

    String result;

    if (!filters.isEmpty()) {
      result = StringUtils.join(filters, "&");
    } else {
      return formString;
    }

    if (StringUtils.isNotEmpty(formString)) {
      return formString + "&" + result;
    } else {
      return "?" + result;
    }
  }
}
