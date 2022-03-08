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

public class EventSummaryResponse {
  private boolean success;
  private List<String> messages;
  private String risk;
  private boolean showEvidence;
  private boolean showEvents;
  private List<EventResource> events;

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public boolean getSuccess() {
    return this.success;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  public List<String> getMessages() {
    return this.messages;
  }

  public void setRisk(String risk) {
    this.risk = risk;
  }

  public String getRisk() {
    return this.risk;
  }

  public void setShowEvidence(boolean showEvidence) {
    this.showEvidence = showEvidence;
  }

  public boolean getShowEvidence() {
    return this.showEvidence;
  }

  public void setShowEvents(boolean showEvents) {
    this.showEvents = showEvents;
  }

  public boolean getShowEvents() {
    return this.showEvents;
  }

  public void setEvents(List<EventResource> events) {
    this.events = events;
  }

  public List<EventResource> getEvents() {
    return this.events;
  }
}
