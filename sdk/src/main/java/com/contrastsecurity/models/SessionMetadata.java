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
import java.util.List;

/**
 * Session metadata associated with a trace. Contains metadata items collected during the session
 * when the vulnerability was detected.
 */
public class SessionMetadata {

  /**
   * Return the session ID for this metadata.
   *
   * @return the session identifier
   */
  public String getSessionId() {
    return sessionId;
  }

  @SerializedName("session_id")
  private String sessionId;

  /**
   * Return the list of metadata items for this session.
   *
   * @return list of metadata items
   */
  public List<MetadataItem> getMetadata() {
    return metadata;
  }

  private List<MetadataItem> metadata;
}
