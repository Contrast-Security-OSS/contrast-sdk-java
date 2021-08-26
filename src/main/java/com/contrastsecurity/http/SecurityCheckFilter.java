package com.contrastsecurity.http;

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
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityCheckFilter {
  public enum QueryBy {
    APP_VERSION_TAG,
    START_DATE
  }

  /** The criteria to query vulnerabilities by */
  @SerializedName("query_by")
  private QueryBy queryBy;

  /** AppVersionTags to match */
  @SerializedName("app_version_tags")
  private List<String> appVersionTags;

  /** startDate to match */
  @SerializedName("start_date")
  private Long startDate;
}
