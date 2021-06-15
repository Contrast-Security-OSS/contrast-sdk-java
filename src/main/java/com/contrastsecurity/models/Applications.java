package com.contrastsecurity.models;

import java.util.List;

/** Base class for api calls returning one or more method */
public class Applications {

  public Integer getCount() {
    return count;
  }

  private Integer count = null;

  public List<Application> getApplications() {
    return applications;
  }

  private List<Application> applications;

  public Application getApplication() {
    return application;
  }

  private Application application;
}
