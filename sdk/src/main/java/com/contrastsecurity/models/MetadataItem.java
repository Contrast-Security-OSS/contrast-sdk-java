package com.contrastsecurity.models;

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

import com.google.gson.annotations.SerializedName;

/** A single metadata item within session metadata. */
public class MetadataItem {

  /**
   * Return the value of this metadata item.
   *
   * @return the metadata value
   */
  public String getValue() {
    return value;
  }

  private String value;

  /**
   * Return the display label for this metadata item. This is the human-readable label shown in the
   * Contrast UI.
   *
   * @return the display label
   */
  public String getDisplayLabel() {
    return displayLabel;
  }

  @SerializedName("display_label")
  private String displayLabel;

  /**
   * Return the agent label for this metadata item. This is the internal label used by the Contrast
   * agent.
   *
   * @return the agent label
   */
  public String getAgentLabel() {
    return agentLabel;
  }

  @SerializedName("agent_label")
  private String agentLabel;
}
