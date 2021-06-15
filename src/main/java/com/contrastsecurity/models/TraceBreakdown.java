package com.contrastsecurity.models;

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
