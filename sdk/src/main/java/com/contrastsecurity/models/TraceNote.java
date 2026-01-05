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

public class TraceNote {

  public long getCreation() {
    return creation;
  }

  private long creation;

  public String getCreator() {
    return creator;
  }

  private String creator;

  public long getLastModification() {
    return lastModification;
  }

  @SerializedName("last_modification")
  private long lastModification;

  public String getLastUpdater() {
    return lastUpdater;
  }

  @SerializedName("last_updater")
  private String lastUpdater;

  public String getNote() {
    return note;
  }

  private String note;

  public String getId() {
    return id;
  }

  private String id;
}
