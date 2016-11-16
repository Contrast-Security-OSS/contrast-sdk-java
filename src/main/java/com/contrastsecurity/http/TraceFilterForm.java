package com.contrastsecurity.http;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

@Getter
@Setter
public class TraceFilterForm {

    public enum ApplicationExpandValues {
        SCORES, TRACE_BREAKDOWN, LICENSE;

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
        CARD, EVENTS, NOTES, REQUEST, APPLICATION, SERVERS;

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
    private List<String> servers;
    private EnumSet<ServerEnvironment> environments;
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
        this.servers = null;
        this.environments = null;
        this.expand = null;
        this.limit = -1;
        this.offset = -1;
        this.sort = "";
    }

    @Override
    public String toString() {
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

        if (severities != null && !severities.isEmpty()) {
            filters.add("severities=" + StringUtils.join(severities, ","));
        }

        if (filterTags != null && !filterTags.isEmpty()) {
            filters.add("filterTags=" + StringUtils.join(filterTags, ","));
        }

        if (servers != null && !servers.isEmpty()) {
            filters.add("servers=" + StringUtils.join(servers, ","));
        }

        if (StringUtils.isNotEmpty(sort)) {
            filters.add("sort=" + sort);
        }

        if (status != null) {
            filters.add("status=" + StringUtils.join(status, ","));
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
}
