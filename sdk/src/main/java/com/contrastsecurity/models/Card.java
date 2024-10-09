package com.contrastsecurity.models;

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

import com.google.gson.annotations.SerializedName;

public class Card {

  /**
   * Returns the Card body snippet as a LinkedTreeMap
   *
   * @return Snippet as LinkedTreeMap
   */
  public Object getBody() {
    return body;
  }

  private Object body;

  /**
   * Returns the Card header snippet as a LinkedTreeMap
   *
   * @return Snippet as LinkedTreeMap
   */
  public Object getHeader() {
    return header;
  }

  private Object header;

  /**
   * Hidden status of the Card
   *
   * @return hidden status
   */
  public boolean getIsHidden() {
    return isHidden;
  }

  @SerializedName("is_hidden")
  private boolean isHidden;

  /**
   * Severity level of the Card
   *
   * @return severity level
   */
  public String getSeverity() {
    return severity;
  }

  private String severity;

  /**
   * Card title
   *
   * @return title
   */
  public String getTitle() {
    return title;
  }

  private String title;

  /**
   * Trace id the Card belongs to
   *
   * @return Trace id
   */
  public String getTraceId() {
    return traceId;
  }
  // @SerializedName("trace_id")
  private String traceId;
}
