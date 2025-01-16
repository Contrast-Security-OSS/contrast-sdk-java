package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2025 Contrast Security, Inc.
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

public class MetadataFilterGroup {

  @SerializedName("label")
  protected String label;

  public String getLabel() {
    return label;
  }
  ;

  @SerializedName("id")
  protected String id;

  public String getId() {
    return id;
  }
  ;

  // fieldType STRING,NUMERIC,CONTACT_NAME,PHONE,EMAIL,PERSON_OF_CONTACT

  public List<MetadataFilterValue> getValues() {
    return values;
  }

  @SerializedName("values")
  private List<MetadataFilterValue> values;
}
