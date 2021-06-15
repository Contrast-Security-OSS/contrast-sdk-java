/*
 * Copyright (c) 2014, Contrast Security, LLC.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * Neither the name of the Contrast Security, LLC. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** A server with the Contrast agent installed. */
public class Server {

  /**
   * Return the id of this server
   *
   * @return the server id
   */
  public long getServerId() {
    return serverId;
  }

  @SerializedName("server_id")
  private long serverId;

  /**
   * Return the name of this server.
   *
   * @return the name of this server
   */
  public String getName() {
    return name;
  }

  private String name;

  /**
   * Return the hostname of this server.
   *
   * @return the hostname of this server
   */
  public String getHostname() {
    return hostname;
  }

  private String hostname;

  /**
   * Return the last time this server was restarted.
   *
   * @return the epoch time of the last server restart
   */
  public long getLastStartup() {
    return lastStartup;
  }

  @SerializedName("last_startup")
  private long lastStartup;

  /**
   * Return the list of applications in this server
   *
   * @return list of applications
   */
  public List<Application> getApplications() {
    return applications;
  }

  private List<Application> applications;

  /**
   * Return the last time a trace was received from this server.
   *
   * @return the epoch time of the last time the server received a trace
   */
  public long getLastTraceReceived() {
    return lastTraceReceived;
  }

  @SerializedName("last_trace_received")
  private long lastTraceReceived;

  /**
   * Return the last time any activity was received from this server.
   *
   * @return the epoch time of the last time any activity was received from this server
   */
  public long getLastActivity() {
    return lastActivity;
  }

  @SerializedName("last_activity")
  private long lastActivity;

  /**
   * Return the number of applications for this server
   *
   * @return number of applications
   */
  public long getNumberApps() {
    return numApps;
  }

  @SerializedName("num_apps")
  private long numApps;

  /**
   * Return the path on disk of this server, e.g., /opt/tomcat6/
   *
   * @return the path on disk of this server, e.g., /opt/tomcat6
   */
  public String getPath() {
    return path;
  }

  private String path;

  /**
   * Return the status of this server Values: ONLINE,OFFLINE
   *
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  private String status;

  /**
   * Return the Contrast "server code" for the server, e.g., "jboss5".
   *
   * @return the code for the server, e.g., "websphere85"
   */
  public String getType() {
    return type;
  }

  private String type;

  /**
   * Return the version of the Contrast agent that's monitoring this server.
   *
   * @return the version of the Contrast agent installed on this server
   */
  public String getAgentVersion() {
    return agentVersion;
  }

  @SerializedName("agent_version")
  private String agentVersion;

  /**
   * Return the server assess value
   *
   * @return assess value
   */
  public boolean getAssess() {
    return assess;
  }

  private boolean assess;

  /**
   * Return is the server changing Assess on restart
   *
   * @return assess pending value
   */
  public boolean getAssessPending() {
    return assessPending;
  }

  // @SerializedName("assess_pending")
  private boolean assessPending;

  /**
   * Return the server defend value
   *
   * @return defend value
   */
  public boolean getDefend() {
    return defend;
  }

  private boolean defend;

  /**
   * Return is the server changing Defend on restart
   *
   * @return defend pending value
   */
  public boolean getDefendPending() {
    return defendPending;
  }

  // @SerializedName("defend_pending")
  private boolean defendPending;

  /**
   * Return the server's container
   *
   * @return server's container
   */
  public String getContainer() {
    return container;
  }

  private String container;

  /**
   * Return the server's environment type TODO values
   *
   * @return the int representation of the serverenvironment type
   */
  public String getEnvironment() {
    return environment;
  }

  private String environment;

  /**
   * Return Is server changing Log Enhancers on restart
   *
   * @return log enhancer pending status
   */
  public boolean getLogEnhancerPending() {
    return logEnhancerPending;
  }

  // @SerializedName("defend_pending")
  private boolean logEnhancerPending;

  /**
   * Return the server log level
   *
   * @return log level
   */
  public String getLogLevel() {
    return logLevel;
  }

  private String logLevel;

  /**
   * Return the path to the servers log
   *
   * @return path to the log
   */
  public String getLogPath() {
    return logPath;
  }

  private String logPath;

  /**
   * Return Is server changing any settings on restart
   *
   * @return no pending changes status
   */
  public boolean getNoPending() {
    return noPending;
  }

  private boolean noPending;

  /**
   * Return if the server has assess sensors
   *
   * @return assess sensors
   */
  public boolean getAssessSensors() {
    return assessSensors;
  }

  private boolean assessSensors;

  /**
   * Return the last assess update for this server
   *
   * @return last assess update
   */
  public long getAssessLastUpdate() {
    return assessLastUpdate;
  }

  @SerializedName("assess_last_update")
  private long assessLastUpdate;
}
