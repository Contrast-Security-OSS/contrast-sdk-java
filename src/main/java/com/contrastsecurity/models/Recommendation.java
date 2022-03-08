package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 Contrast Security, Inc.
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

import java.util.Map;

public class Recommendation {

  private String text;
  private String formattedText;
  private Map<String, String> formattedTextVariables;

  public Recommendation() {}

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getFormattedText() {
    return formattedText;
  }

  public void setFormattedText(String formattedText) {
    this.formattedText = formattedText;
  }

  public Map<String, String> getFormattedTextVariables() {
    return formattedTextVariables;
  }

  public void setFormattedTextVariables(Map<String, String> formattedTextVariables) {
    this.formattedTextVariables = formattedTextVariables;
  }
}
