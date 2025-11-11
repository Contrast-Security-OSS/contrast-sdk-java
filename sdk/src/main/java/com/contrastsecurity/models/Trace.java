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

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** A vulnerability identified by Contrast. */
public class Trace {

  /**
   * Return the title for this vulnerability, e.g.: "XSS on /foo.jsp in 'bar' parameter"
   *
   * @return the title of this vulnerability
   */
  public String getTitle() {
    return title;
  }

  private String title;

  /**
   * Return any arbitrarily-formatted 'evidence' for this trace. Many rules won't cause any evidence
   * to be created.
   *
   * @return an arbitrarily-formatted 'evidence' for this trace, or null if none present
   */
  public String getEvidence() {
    return evidence;
  }

  private String evidence;

  /**
   * Return the language of application this trace was discovered in.
   *
   * @return the language of application this trace was discovered in
   */
  public String getLanguage() {
    return language;
  }

  private String language;

  /**
   * Return the status of this trace, like "Reported", "Verified", "Suspicious", etc.
   *
   * @return the status of this trace, like "Reported", "Verified", "Suspicious", etc
   */
  public String getStatus() {
    return status;
  }

  private String status;

  public String getSubStatus() {
    return subStatus;
  }

  private String subStatus;

  /**
   * Return the simple, numeric hash of this trace
   *
   * @return numeric hash of this trace
   */
  public String getHash() {
    return hash;
  }

  private String hash;

  /**
   * Return the UUID for this trace.
   *
   * @return the UUID for this trace
   */
  public String getUuid() {
    return uuid;
  }

  private String uuid;

  /**
   * Return the name of the rule that caused this trace.
   *
   * @return the name of the rule that caused this trace
   */
  public String getRule() {
    return rule;
  }

  @SerializedName("rule_name")
  private String rule;

  /**
   * Return the HTTP request that caused this trace to occur.
   *
   * @return the causing HTTP request
   */
  public HttpRequest getRequest() {
    return request;
  }

  private HttpRequest request;

  /**
   * The events that make up the vulnerability. Some traces will only have an evidence field and no
   * events.
   *
   * @return the TraceEvents
   */
  public List<TraceEvent> getEvents() {
    return events;
  }

  private List<TraceEvent> events;

  /**
   * Return the overall severity of this trace.
   *
   * @return the severity
   */
  public String getSeverity() {
    return severity;
  }

  private String severity;

  /**
   * Return the likelihood of this trace
   *
   * @return the likelihood
   */
  public String getLikelihood() {
    return likelihood;
  }

  private String likelihood;

  /**
   * Return the impact of this trace
   *
   * @return the impact
   */
  public String getImpact() {
    return impact;
  }

  private String impact;

  /**
   * Return the confidence rating for this trace
   *
   * @return the confidence
   */
  public String getConfidence() {
    return confidence;
  }

  private String confidence;

  /**
   * Return the First time this Trace was seen
   *
   * @return Time this trace was first seen
   */
  public Long getFirstTimeSeen() {
    return firstTimeSeen;
  }

  @SerializedName("first_time_seen")
  private Long firstTimeSeen;

  /**
   * Return the Last time this Trace was seen
   *
   * @return the time this trace was last seen
   */
  public Long getLastTimeSeen() {
    return lastTimeSeen;
  }

  @SerializedName("last_time_seen")
  private Long lastTimeSeen;

  /**
   * Return the Application for this trace
   *
   * @return the applicaiton
   */
  public Application getApplication() {
    return application;
  }

  private Application application;

  /**
   * Return the category for this trace
   *
   * @return the category
   */
  public String getCategory() {
    return category;
  }

  private String category;

  /**
   * Return the closed time this Trace was seen
   *
   * @return the time this trace was closed
   */
  public Long getClosedTime() {
    return closedTime;
  }

  @SerializedName("closed_time")
  private Long closedTime;

  /**
   * Return the parent application id for this trace
   *
   * @return the parent application id
   */
  public String getParentApplicationId() {
    return parentApplicationId;
  }

  private String parentApplicationId;

  /**
   * Return the platform for this trace
   *
   * @return the platform
   */
  public String getPlatform() {
    return platform;
  }

  private String platform;

  /**
   * Return the list of servers this traces is in
   *
   * @return list of Servers
   */
  public List<Server> getServers() {
    return servers;
  }

  private List<Server> servers;

  /**
   * Return the total number of traces received for the trace
   *
   * @return the total number
   */
  public Long getTotalTracesReceived() {
    return totalTotalTracesReceived;
  }

  @SerializedName("total_traces_received")
  private Long totalTotalTracesReceived;

  /**
   * Return if the Trace is visible
   *
   * @return visibility status
   */
  public boolean getVisible() {
    return visible;
  }

  private boolean visible;

  /**
   * Return the notes for the trace
   *
   * @return list of TraceNote's
   */
  public List<TraceNote> getTraceNotes() {
    return notes;
  }

  private List<TraceNote> notes;

  /**
   * Return the card for the trace
   *
   * @return Card Object
   */
  public Card getCard() {
    return card;
  }

  private Card card;

  /**
   * Return the server environments for this trace. This provides lightweight access to server
   * environment data without requiring the full servers expand.
   *
   * <p>Note: This field requires the expand parameter to be set. Use {@link
   * com.contrastsecurity.http.TraceFilterForm.TraceExpandValue#SERVER_ENVIRONMENTS} when querying
   * traces to populate this field.
   *
   * @return list of server environment names (e.g., "DEVELOPMENT", "QA", "PRODUCTION"), or null if
   *     not expanded
   */
  @SerializedName("server_environments")
  private List<String> serverEnvironments;

  public List<String> getServerEnvironments() {
    return serverEnvironments;
  }
  /**
   * Return the tags for this trace
   *
   * @return list of tags
   */
  public List<String> getTags() {
    return tags;
  }

  @SerializedName("tags")
  private List<String> tags;

  /**
   * Return the session metadata for this trace. Session metadata contains custom key-value pairs
   * collected by the Contrast agent during the session when this vulnerability was detected.
   *
   * <p>Note: This field requires the expand parameter to be set. Use {@link
   * com.contrastsecurity.http.TraceFilterForm.TraceExpandValue#SESSION_METADATA} when querying
   * traces to populate this field.
   *
   * @return list of session metadata objects containing session IDs and metadata items, or null if
   *     not expanded
   */
  public List<SessionMetadata> getSessionMetadata() {
    return sessionMetadata;
  }

  @SerializedName("session_metadata")
  private List<SessionMetadata> sessionMetadata;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Trace trace = (Trace) o;

    return uuid != null ? uuid.equals(trace.uuid) : trace.uuid == null;
  }

  @Override
  public int hashCode() {
    return uuid != null ? uuid.hashCode() : 0;
  }
}
