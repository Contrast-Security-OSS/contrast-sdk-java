package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2025 Contrast Security, Inc.
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

public class TraceBreakdown {

  public int getSafes() {
    return safes;
  }

  private int safes = 0;

  public int getCriticals() {
    return criticals;
  }

  private int criticals = 0;

  public int getHighs() {
    return highs;
  }

  private int highs = 0;

  public int getMeds() {
    return meds;
  }

  private int meds = 0;

  public int getLows() {
    return lows;
  }

  private int lows = 0;

  public int getNotes() {
    return notes;
  }

  private int notes = 0;

  public int getTriaged() {
    return triaged;
  }

  private int triaged = 0;

  public int getTraceCount() {
    return traceCount;
  }

  private int traceCount = 0;

  public int getConfirmed() {
    return confirmed;
  }

  private int confirmed = 0;

  public int getSuspicious() {
    return suspicious;
  }

  private int suspicious = 0;

  public int getNotProblem() {
    return notProblem;
  }

  private int notProblem = 0;

  public int getRemediated() {
    return remediated;
  }

  private int remediated = 0;

  public int getReported() {
    return reported;
  }

  private int reported = 0;
}
