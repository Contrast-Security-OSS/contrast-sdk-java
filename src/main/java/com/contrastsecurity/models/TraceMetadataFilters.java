package com.contrastsecurity.models;

import java.util.Arrays;
import java.util.List;

public class TraceMetadataFilters {
  private List<TraceMetadataFilter> metadataFilters;

  public TraceMetadataFilters(TraceMetadataFilter... filters) {
    this.metadataFilters = Arrays.asList(filters);
  }

  public TraceMetadataFilters(List<TraceMetadataFilter> filters) {
    this.metadataFilters = filters;
  }

  public void setMetadataFilters(List<TraceMetadataFilter> filters) {
    this.metadataFilters = filters;
  }

  public List<TraceMetadataFilter> getMetadataFilters() {
    return this.metadataFilters;
  }
}
