package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class Scores {

  public Integer getGrade() {
    return this.grade;
  }

  private Integer grade = 0;

  public String getLetterGrade() {
    return this.letterGrade;
  }

  @SerializedName("letter_grade")
  private String letterGrade = "";

  public ScoreMetric getPlatformScore() {
    return this.platform;
  }

  private ScoreMetric platform = null;

  public ScoreMetric getSecurityScore() {
    return this.security;
  }

  private ScoreMetric security = null;

  class ScoreMetric {
    public Integer getGrade() {
      return this.grade;
    }

    private Integer grade = 0;

    public String getLetterGrade() {
      return this.letterGrade;
    }

    @SerializedName("letter_grade")
    private String letterGrade = "";
  }
}
