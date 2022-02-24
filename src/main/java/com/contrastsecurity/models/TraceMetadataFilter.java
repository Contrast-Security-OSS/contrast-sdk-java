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

import java.util.Arrays;
import java.util.List;

public class TraceMetadataFilter {
  private String fieldID;
  private List<String> values;

  public TraceMetadataFilter(String fieldID, String... values) {
    this.fieldID = fieldID;
    this.values = Arrays.asList(values);
  }

  public TraceMetadataFilter(String fieldID, List<String> values) {
    this.fieldID = fieldID;
    this.values = values;
  }

  public String getFieldID() {
    return this.fieldID;
  }

  public void setFieldID(String fieldID) {
    this.fieldID = fieldID;
  }

  public List<String> getValues() {
    return this.values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }
}
