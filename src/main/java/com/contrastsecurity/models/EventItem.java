package com.contrastsecurity.models;

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

public class EventItem extends EventModel {

  private String type;
  private String value;
  private boolean isStacktrace;

  public EventItem() {}

  public EventItem(EventResource parent, String type, String value, boolean isStacktrace) {
    super();
    this.type = type;
    this.value = value;
    this.isStacktrace = isStacktrace;
    this.parent = parent;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public boolean isStacktrace() {
    return isStacktrace;
  }

  public void setStacktrace(boolean isStacktrace) {
    this.isStacktrace = isStacktrace;
  }
}
