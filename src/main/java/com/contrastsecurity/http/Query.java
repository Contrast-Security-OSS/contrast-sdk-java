package com.contrastsecurity.http;

import com.contrastsecurity.exceptions.QueryException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class Query<T extends Query<T>> {
  private EnumSet<?> expand;
  private int limit;
  private int offset;
  private Date startDate;
  private Date endDate;
  private String sort;

  public Query() {
    this.expand = null;
    this.limit = 0;
    this.offset = 0;
    this.startDate = null;
    this.endDate = null;
    this.sort = "";
  }

  public Query(
      EnumSet<?> expand, int limit, int offset, Date startDate, Date endDate, String sort) {
    this.expand = expand;
    this.limit = limit;
    this.offset = offset;
    this.startDate = startDate;
    this.endDate = endDate;
    this.sort = sort;
  }

  public EnumSet<?> getExpand() {
    return expand;
  }

  public T setExpand(EnumSet<?> expand) {
    this.expand = expand;
    return self();
  }

  public int getLimit() {
    return limit;
  }

  public T setLimit(int limit) {
    this.limit = limit;
    return self();
  }

  public int getOffset() {
    return offset;
  }

  public T setOffset(int offset) {
    this.offset = offset;
    return self();
  }

  public Date getStartDate() {
    return startDate;
  }

  public T setStartDate(Date startDate) {
    this.startDate = startDate;
    return self();
  }

  public Date getEndDate() {
    return endDate;
  }

  public T setEndDate(Date endDate) {
    this.endDate = endDate;
    return self();
  }

  public String getSort() {
    return sort;
  }

  public T setSort(String sort) {
    this.sort = sort;
    return self();
  }

  @SuppressWarnings("unchecked")
  private T self() {
    return (T) this;
  }

  /**
   * Returns the query into url parameters
   *
   * @return the query into url parameters
   * @throws QueryException when an exception occurs converting to url parameters
   */
  public String toQuery() throws QueryException {
    List<String> filters = new ArrayList<>();

    if (expand != null && !expand.isEmpty()) {
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

    if (!StringUtils.isEmpty(sort)) {
      filters.add("sort=" + sort);
    }

    if (!filters.isEmpty()) {
      return "?" + StringUtils.join(filters, "&");
    } else {
      return "";
    }
  }
}
