package com.contrastsecurity.http;

import com.contrastsecurity.exceptions.QueryException;

public class Query extends FilterForm {

  /**
   * Returns the query into url parameters
   *
   * @return the query into url parameters
   * @throws QueryException when an exception occurs converting to url parameters
   */
  public String toQuery() throws QueryException {
    return super.toString();
  }
}
