package com.contrastsecurity.http;

public class Query extends FilterForm{

  /**
   * Returns the query into url parameters
   * @return the query into url parameters
   */
  public String toQuery() {
    return super.toString();
  }
}
