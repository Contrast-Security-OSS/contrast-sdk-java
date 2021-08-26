package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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
import lombok.Getter;

@Getter
public class SecurityCheck {

  /**
   * The ID of the security check
   *
   * @return the ID of the security check.
   */
  @SerializedName("id")
  private Long id;

  /**
   * The name of the application verified.
   *
   * @return the name of the application
   */
  @SerializedName("application_name")
  private String applicationName;

  /**
   * The ID of the application verified.
   *
   * @return the ID of the application.
   */
  @SerializedName("application_id")
  private String applicationId;

  /**
   * The origin of where the security check was made from.
   *
   * @return the origin of the security check.
   */
  @SerializedName("origin")
  private String origin;

  /**
   * The result of the security check true = the application passed all job outcome policies. false
   * = the application failed a job outcome policy. null = no applicable job outcome policy for
   * application.
   *
   * @return the result of the security check.
   */
  @SerializedName("result")
  private Boolean result;

  /**
   * The job outcome policy that the application failed. null if the application passed all job
   * outcome policies.
   *
   * @reutnr The job outcome policy that the application failed.
   */
  @SerializedName("job_outcome_policy")
  private JobOutcomePolicy jobOutcomePolicy;
}
