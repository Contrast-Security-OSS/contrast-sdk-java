package com.contrastsecurity.http;

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

import java.util.ArrayList;
import java.util.List;

public class ServerFilterForm extends FilterForm {

  public enum ServerExpandValue {
    APPLICATIONS,
    NUM_APPS;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  public enum ServerQuickFilterType {
    ALL,
    PROTECTED,
    UNPROTECTED,
    ONLINE,
    OFFLINE,
    OUT_OF_DATE;

    @Override
    public String toString() {
      return name();
    }
  }

  private String q;
  private boolean includeArchived;
  private List<String> applicationIds;
  private List<String> logLevels;
  private List<String> tags;
  private ServerQuickFilterType quickFilter;

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

  public ServerQuickFilterType getQuickFilter() {
    return quickFilter;
  }

  public void setQuickFilter(ServerQuickFilterType quickFilter) {
    this.quickFilter = quickFilter;
  }

  @Override
  public String toString() {
    String formString = super.toString();

    List<String> filters = new ArrayList<>();

    if (!(q == null || q.isEmpty())) {
      filters.add("q=" + q);
    }

    filters.add("includeArchived=" + includeArchived);

    if (!applicationIds.isEmpty()) {
      filters.add("applicationsIds=" + String.join(",", applicationIds));
    }

    if (!logLevels.isEmpty()) {
      filters.add("logLevels=" + String.join(",", logLevels));
    }

    if (!tags.isEmpty()) {
      filters.add("tags=" + String.join(",", tags));
    }

    if (quickFilter != null) {
      filters.add("quickFilter=" + quickFilter.toString());
    }

    String result;

    if (!filters.isEmpty()) {
      result = String.join("&", filters);
    } else {
      return formString;
    }

    if (!(formString == null || formString.isEmpty())) {
      return formString + "&" + result;
    } else {
      return "?" + result;
    }
  }
}
