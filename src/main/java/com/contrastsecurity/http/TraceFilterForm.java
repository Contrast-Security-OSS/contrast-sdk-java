package com.contrastsecurity.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

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

    @Override
    public String toString() {
      return name().toLowerCase();
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
  }

  /**
   * Translate the filters to one or more URL query arguments
   *
   * @return The URL query string
   * @throws UnsupportedEncodingException if any of the filter arguments cannot be encoded
   */
  public String toQuery() throws UnsupportedEncodingException {
    List<String> filters = new ArrayList<>();

    if (StringUtils.isNotEmpty(filterText)) {
      filters.add(filterText);
    }

    if (expand != null && !expand.isEmpty()) {
      filters.add("expand=" + StringUtils.join(expand, ","));
    }

    if (startDate != null) {
      filters.add("startDate=" + startDate.getTime());
    }

    if (endDate != null) {
      filters.add("endDate=" + endDate.getTime());
    }

    if (filterTags != null && !filterTags.isEmpty()) {
      filters.add("filterTags=" + StringUtils.join(filterTags, ","));
    }

    if (severities != null && !severities.isEmpty()) {
      filters.add("severities=" + StringUtils.join(severities, ","));
    }

    if (status != null) {
      filters.add("status=" + StringUtils.join(status, ","));
    }

    if (vulnTypes != null && !vulnTypes.isEmpty()) {
      filters.add("vulnTypes=" + StringUtils.join(vulnTypes, ","));
    }

    if (appVersionTags != null && !appVersionTags.isEmpty()) {
      filters.add(
          "appVersionTags=" + URLEncoder.encode(StringUtils.join(appVersionTags, ","), "UTF-8"));
    }

    if (environments != null && !environments.isEmpty()) {
      filters.add("environments=" + StringUtils.join(environments, ","));
    }

    if (serverIds != null && !serverIds.isEmpty()) {
      filters.add("servers=" + StringUtils.join(serverIds, ","));
    }

    if (urls != null && !urls.isEmpty()) {
      filters.add("urls=" + StringUtils.join(urls, ","));
    }

    if (modules != null && !modules.isEmpty()) {
      filters.add("modules=" + StringUtils.join(modules, ","));
    }

    if (StringUtils.isNotEmpty(sort)) {
      filters.add("sort=" + sort);
    }

    if (limit > -1) {
      filters.add("limit=" + limit);
    }

    if (offset > -1) {
      filters.add("offset=" + offset);
    }

    if (!filters.isEmpty()) {
      return "?" + StringUtils.join(filters, "&");
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
