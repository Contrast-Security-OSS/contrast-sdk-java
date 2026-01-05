package com.contrastsecurity.models.dtm;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2026 Contrast Security, Inc.
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
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ApplicationCreateRequest {
  @SerializedName("name")
  @NonNull
  private String appName;

  @SerializedName("language")
  @NonNull
  private AgentType appLanguage;

  @SerializedName("path")
  private String appPath;

  @SerializedName("short_name")
  private String appShortName;

  public ApplicationCreateRequest(String appName, AgentType appLanguage) {
    this.appName = appName;
    this.appLanguage = appLanguage;
  }
}
