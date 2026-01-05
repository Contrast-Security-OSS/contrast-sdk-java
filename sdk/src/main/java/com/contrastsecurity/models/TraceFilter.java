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

public class TraceFilter {

  public String getKeycode() {
    return keycode;
  }

  public void setKeycode(String keycode) {
    this.keycode = keycode;
  }

  private String keycode;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  private String label;

  public Object getDetails() {
    return details;
  }

  private Object details;

  public long getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  private long count;

  @SerializedName("new_group")
  private boolean newGroup;

  public boolean getNewGroup() {
    return newGroup;
  }

  public void setNewGroup(boolean newGroup) {
    this.newGroup = newGroup;
  }

  @Override
  public String toString() {
    return keycode;
  }
}
