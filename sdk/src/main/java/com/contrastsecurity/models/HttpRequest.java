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

/** An HTTP request. */
public class HttpRequest {

  public int getPort() {
    return port;
  }

  private int port;

  public String getProtocol() {
    return protocol;
  }

  private String protocol;

  public String getMethod() {
    return method;
  }

  private String method;

  public String getUri() {
    return uri;
  }

  private String uri;

  public String getUrl() {
    return url;
  }

  private String url;

  public String getVersion() {
    return version;
  }

  private String version;

  public String getQueryString() {
    return queryString;
  }

  @SerializedName("query_string")
  private String queryString;

  public List<NameValuePair> getHeaders() {
    return headers;
  }

  private List<NameValuePair> headers;

  public List<NameValuePair> getParameters() {
    return parameters;
  }

  private List<NameValuePair> parameters;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  private String text;
}
