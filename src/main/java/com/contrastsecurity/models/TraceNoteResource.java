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

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TraceNoteResource {

  @SerializedName("note")
  protected String note;

  public String getNote() {
    return note;
  }
  ;

  @SerializedName("creator")
  protected String creator;

  public String getCreator() {
    return creator;
  }
  ;

  @SerializedName("creation")
  protected String creation;

  public String getCreation() {
    return creation;
  }
  ;

  public List<NgTraceNoteReadOnlyPropertyResource> getProperties() {
    return properties;
  }

  @SerializedName("properties")
  private List<NgTraceNoteReadOnlyPropertyResource> properties;

  public class NgTraceNoteReadOnlyPropertyResource {

    @SerializedName("name")
    protected String name;

    public String getName() {
      return name;
    }
    ;

    @SerializedName("value")
    protected String value;

    public String getValue() {
      return value;
    }
    ;
  }
}
