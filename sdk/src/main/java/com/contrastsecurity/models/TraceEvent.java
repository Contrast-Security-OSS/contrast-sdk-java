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

/**
 * Several TraceEvents make up a vulnerability, or, "trace". They represent a method invocation that
 * Contrast monitored.
 */
public class TraceEvent {

  /**
   * Return the id for the event in the trace
   *
   * @return the id of the event
   */
  public long getEventId() {
    return eventId;
  }

  private long eventId;

  /**
   * Return the code context for the event
   *
   * @return code context for the event
   */
  private String codeContext;

  public String getCodeContext() {
    return codeContext;
  }

  /**
   * Return the type of event this is, e.g., Creation, P2O, Trigger, etc.
   *
   * @return the type of event this is
   */
  public String getType() {
    return type;
  }

  private String type;
}
