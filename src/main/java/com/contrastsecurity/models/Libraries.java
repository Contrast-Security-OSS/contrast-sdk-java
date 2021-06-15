package com.contrastsecurity.models;

import java.util.List;

/** Base class for api calls for libraries. */
public class Libraries {

  public String getAverageScoreLetter() {
    return averageScoreLetter;
  }

  private String averageScoreLetter = null;

  public Integer getAverageScore() {
    return averageScore;
  }

  private Integer averageScore = null;

  public Integer getAverageMonths() {
    return averageMonths;
  }

  private Integer averageMonths = null;

  public List<Library> getLibraries() {
    return libraries;
  }

  private List<Library> libraries;
}
