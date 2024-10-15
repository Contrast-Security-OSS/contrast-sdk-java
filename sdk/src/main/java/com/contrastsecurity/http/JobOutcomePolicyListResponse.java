package com.contrastsecurity.http;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2024 Contrast Security, Inc.
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

import com.contrastsecurity.models.JobOutcomePolicy;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;

/** Wrapper for the response object returned by Contrast */
@Getter
public class JobOutcomePolicyListResponse {

  /**
   * List of job outcome policies
   *
   * @return The list of job outcome policies
   */
  @SerializedName("policies")
  private List<JobOutcomePolicy> policies;
}
