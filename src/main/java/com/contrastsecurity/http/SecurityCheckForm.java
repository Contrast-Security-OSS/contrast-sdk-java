package com.contrastsecurity.http;

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

import com.contrastsecurity.models.AgentType;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Form that is used making security checks */
@Getter
@Setter
@AllArgsConstructor
public class SecurityCheckForm {

  /**
   * The ID of the application to be verified
   *
   * @param applicationId New value for application id.
   * @return The ID of the application
   */
  @SerializedName("application_id")
  private String applicationId;

  /**
   * The name of the application to be verified
   *
   * @param applicationName New value for application name.
   * @return The name of the application.
   */
  @SerializedName("application_name")
  private String applicationName;

  /**
   * The language of the agent used to instrument the application
   *
   * @param agentLanguage New value for agent language.
   * @return the agent language of the application
   */
  @SerializedName("agent_language")
  private AgentType agentLanguage;

  /**
   * Where the request is being made Default: OTHER
   *
   * @param origin New origin for the security check
   * @returnThe origin of the security check
   */
  @SerializedName("origin")
  private String origin = "OTHER";

  /**
   * The job start time of the jenkins job Some job outcome policy configurations may not work as
   * expected if not set
   *
   * @param jobStartTime New value for job start time.
   * @return the start time of the jenkins job
   */
  @SerializedName("job_start_time")
  private Long jobStartTime;

  /**
   * Filter to be applied to the vulnerabilities if the jop opt in
   *
   * @param securityCheckFilter the security check filter.
   * @return the security check filter
   */
  @SerializedName("security_check_filter")
  private SecurityCheckFilter securityCheckFilter;

  public SecurityCheckForm(String applicationId) {
    this.applicationId = applicationId;
  }

  public SecurityCheckForm(String applicationName, AgentType agentLanguage) {
    this.applicationName = applicationName;
    this.agentLanguage = agentLanguage;
  }
}
