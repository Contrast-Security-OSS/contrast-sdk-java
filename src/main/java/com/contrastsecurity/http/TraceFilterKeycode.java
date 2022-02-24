package com.contrastsecurity.http;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
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

public enum TraceFilterKeycode {
  ALL_ISSUES("00001"),
  CRITICAL_HIGH_SEVERITIES("00002"),
  CURRENT_WEEK("00003"),
  HIGH_CONFIDENCE("00004"),
  OPEN_TRACES("00005"),
  APP_ID("appId"),
  SERVER_ID("serverId"),
  URL("url"),
  RULE_NAME("ruleName");

  @Getter private String label;

  TraceFilterKeycode(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label.toLowerCase();
  }
}
