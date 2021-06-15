package com.contrastsecurity.http;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class ServerFilterForm extends FilterForm {

  public enum ServerExpandValue {
    APPLICATIONS,
    NUM_APPS;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  private String q;
  private boolean includeArchived;
  private List<String> applicationIds;
  private List<String> logLevels;
  private List<String> tags;

  public ServerFilterForm() {
    super();
    this.q = "";
    this.includeArchived = false;
    this.applicationIds = new ArrayList<>();
    this.logLevels = new ArrayList<>();
    this.tags = new ArrayList<>();
  }

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

  public boolean getIncludeArchived() {
    return includeArchived;
  }

  public void setIncludeArchived(boolean includeArchived) {
    this.includeArchived = includeArchived;
  }

  public List<String> getApplicationIds() {
    return applicationIds;
  }

  public void setApplicationIds(List<String> applicationIds) {
    this.applicationIds = applicationIds;
  }

  public List<String> getLogLevels() {
    return logLevels;
  }

  public void setLogLevels(List<String> logLevels) {
    this.logLevels = logLevels;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
    String formString = super.toString();

    List<String> filters = new ArrayList<>();

    if (!StringUtils.isEmpty(q)) {
      filters.add("q=" + q);
    }

    filters.add("includeArchived=" + includeArchived);

    if (!applicationIds.isEmpty()) {
      filters.add("applicationsIds=" + StringUtils.join(applicationIds, ","));
    }

    if (!logLevels.isEmpty()) {
      filters.add("logLevels=" + StringUtils.join(logLevels, ","));
    }

    if (!tags.isEmpty()) {
      filters.add("tags=" + StringUtils.join(tags, ","));
    }

    String result;

    if (!filters.isEmpty()) {
      result = StringUtils.join(filters, "&");
    } else {
      return formString;
    }

    if (!StringUtils.isEmpty(formString)) {
      return formString + "&" + result;
    } else {
      return "?" + result;
    }
  }
}
