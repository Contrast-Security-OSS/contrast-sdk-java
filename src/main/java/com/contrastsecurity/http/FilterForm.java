package com.contrastsecurity.http;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterForm {

    public enum ExpandValues {
        VULNS, APPS, MANIFEST, CVE, SERVERS;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private List<String> expand;
    private int limit;
    private int offset;
    private Date startDate;
    private Date endDate;
    private List<String> severities;
    private String sort;
    private String status;

    public FilterForm() {
        this.expand = new ArrayList<>();
        this.limit = 0;
        this.offset = 0;
        this.startDate = null;
        this.endDate = null;
        this.severities = new ArrayList<>();
        this.sort = "";
        this.status = "";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getExpand() {
        return expand;
    }

    public void setExpand(List<String> expand) {
        this.expand = expand;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getSeverities() {
        return severities;
    }

    public void setSeverities(List<String> severities) {
        this.severities = severities;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String toString() {
        List<String> filters = new ArrayList<>();

        if (!expand.isEmpty()) {
            filters.add("expand=" + StringUtils.join(expand, ","));
        }

        if (limit > 0) {
            filters.add("limit=" + limit);
        }

        if (offset > 0) {
            filters.add("offset=" + offset);
        }

        if (startDate != null) {
            filters.add("startDate=" + startDate.getTime());
        }

        if (endDate != null) {
            filters.add("endDate=" + endDate.getTime());
        }

        if (!severities.isEmpty()) {
            filters.add("severities=" + StringUtils.join(severities, ","));
        }

        if (!StringUtils.isEmpty(sort)) {
            filters.add("sort=" + sort);
        }

        if (!StringUtils.isEmpty(status)) {
            filters.add("status=" + status);
        }

        if (!filters.isEmpty()) {
            return "?" + StringUtils.join(filters, "&");
        } else {
            return "";
        }
    }
}
