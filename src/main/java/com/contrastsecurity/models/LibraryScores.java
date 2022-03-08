package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 Contrast Security, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
