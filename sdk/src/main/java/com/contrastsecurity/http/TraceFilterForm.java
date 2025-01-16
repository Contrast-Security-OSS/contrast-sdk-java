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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraceFilterForm {

  public void setVulnTypes(List<String> vulnTypes) {
    this.vulnTypes = vulnTypes;
  }

  public enum ApplicationExpandValues {
    SCORES,
    TRACE_BREAKDOWN,
    LICENSE;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  public enum LibrariesExpandValues {
    VULNS;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  public enum TraceExpandValue {
    CARD,
    EVENTS,
    NOTES,
    REQUEST,
    APPLICATION,
    SERVERS;

    public String toURIString() {
      return name().toLowerCase();
    }

    @Override
    public String toString() {
      return toURIString();
    }
  }

  private String filterText;
  private Date startDate;
  private Date endDate;
  private List<String> filterTags;
  private EnumSet<RuleSeverity> severities;
  private List<String> status;
  private List<String> vulnTypes;
  private List<String> appVersionTags;
  private List<Long> serverIds;
  private EnumSet<ServerEnvironment> environments;
  private List<String> urls;
  private List<String> modules;
  private EnumSet<TraceExpandValue> expand;
  private int limit;
  private int offset;
  private String sort;
  private Boolean tracked;
  private Boolean untracked;

  public TraceFilterForm() {
    this.filterText = "";
    this.startDate = null;
    this.endDate = null;
    this.filterTags = null;
    this.severities = null;
    this.status = null;
    this.vulnTypes = null;
    this.appVersionTags = null;
    this.serverIds = null;
    this.environments = null;
    this.urls = null;
    this.modules = null;
    this.expand = null;
    this.limit = -1;
    this.offset = -1;
    this.sort = "";
    this.tracked = true;
    this.untracked = false;
  }

  /**
   * Translate the filters to one or more URL query arguments
   *
   * @return The URL query string
   * @throws UnsupportedEncodingException if any of the filter arguments cannot be encoded
   */
  public String toQuery() throws UnsupportedEncodingException {
    List<String> filters = new ArrayList<>();

    if ((filterText != null && !filterText.isEmpty())) {
      filters.add(filterText);
    }

    if (expand != null && !expand.isEmpty()) {
      filters.add(
          "expand="
              + expand.stream()
                  .map(TraceExpandValue::toURIString)
                  .collect(Collectors.joining(",")));
    }

    if (startDate != null) {
      filters.add("startDate=" + startDate.getTime());
    }

    if (endDate != null) {
      filters.add("endDate=" + endDate.getTime());
    }

    if (filterTags != null && !filterTags.isEmpty()) {
      filters.add("filterTags=" + String.join(",", filterTags));
    }

    if (severities != null && !severities.isEmpty()) {
      filters.add(
          "severities="
              + severities.stream()
                  .map(RuleSeverity::toURIString)
                  .collect(Collectors.joining(",")));
    }

    if (status != null) {
      filters.add("status=" + String.join(",", status));
    }

    if (vulnTypes != null && !vulnTypes.isEmpty()) {
      filters.add("vulnTypes=" + String.join(",", vulnTypes));
    }

    if (appVersionTags != null && !appVersionTags.isEmpty()) {
      filters.add("appVersionTags=" + URLEncoder.encode(String.join(",", appVersionTags), "UTF-8"));
    }

    if (environments != null && !environments.isEmpty()) {
      filters.add(
          "environments="
              + environments.stream()
                  .map(ServerEnvironment::toURIString)
                  .collect(Collectors.joining(",")));
    }

    if (serverIds != null && !serverIds.isEmpty()) {
      filters.add(
          "servers=" + serverIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    if (urls != null && !urls.isEmpty()) {
      filters.add("urls=" + String.join(",", urls));
    }

    if (modules != null && !modules.isEmpty()) {
      filters.add("modules=" + String.join(",", modules));
    }

    if ((sort != null && !sort.isEmpty())) {
      filters.add("sort=" + sort);
    }

    if (limit > -1) {
      filters.add("limit=" + limit);
    }

    if (offset > -1) {
      filters.add("offset=" + offset);
    }

    filters.add("tracked=" + tracked);
    filters.add("untracked=" + untracked);

    if (!filters.isEmpty()) {
      return "?" + String.join("&", filters);
    } else {
      return "";
    }
  }

  @Override
  public String toString() {
    try {
      return toQuery();
    } catch (UnsupportedEncodingException uee) {
      return "Unable to encode filters";
    }
  }
}
