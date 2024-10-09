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

import lombok.Getter;

public enum TraceFilterType {
  MODULES("modules"),
  WORKFLOW("workflow"),
  SERVERS("servers"),
  TIME("time"),
  URL("url"),
  VULNTYPE("vulntype"),
  SERVER_ENVIRONMENT("server-environment"),
  APP_VERSION_TAGS("appversiontags");

  @Getter private String label;

  TraceFilterType(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label.toLowerCase();
  }
}
