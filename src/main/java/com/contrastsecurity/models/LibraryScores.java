package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LibraryScores {

  // Inner Class for Scores
  public class Score {

    public String getGrade() {
      return this.grade;
    }

    @SerializedName("grade")
    private String grade;

    public int getGradeCount() {
      return this.count;
    }

    @SerializedName("count")
    private int count;
  }

  /**
   * The average grade of libraries
   *
   * @return average grade of libraries
   */
  public String getAverageGrade() {
    return this.averageGrade;
  }

  @SerializedName("average_grade")
  private String averageGrade;

  /**
   * The average score of libraries
   *
   * @return the average score of libraries
   */
  public int getAverageScore() {
    return this.averageScore;
  }

  @SerializedName("average_score")
  private int averageScore = 0;

  /**
   * Return the library stats
   *
   * @return the library stats
   */
  public List<Score> getScores() {
    return scores;
  }

  @SerializedName("breakdown")
  private List<Score> scores;
}
