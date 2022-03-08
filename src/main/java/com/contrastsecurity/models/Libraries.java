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
