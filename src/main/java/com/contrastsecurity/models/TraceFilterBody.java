package com.contrastsecurity.models;

import com.contrastsecurity.http.RuleSeverity;
import com.contrastsecurity.http.ServerEnvironment;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraceFilterBody {
  private List<String> appVersionTags;
  private String applicationId;
  private Date startDate;
  private Date endDate;
  private List<ServerEnvironment> environments;
  private List<String> filterTags;
  private String filterText;
  private List<TraceMetadataFilter> metadataFilters;
  private List<String> modules;
  private VulnerabilityQuickFilterType quickFilter;
  private List<String> servers;
  private List<RuleSeverity> severities;
  private TraceTimestampField timestampFilter;
  private boolean tracked;
  private boolean untracked;
  private List<String> urls;
  private List<String> vulnTypes;
}
