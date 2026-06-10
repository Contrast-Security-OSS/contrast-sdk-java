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
package com.contrastsecurity.sdk.graph;

import com.contrastsecurity.exceptions.ServerResponseException;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.MediaType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.contrastsecurity.utils.ContrastSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public final class ContrastGraphApiImpl implements ContrastGraphApi {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String graphApiBase;

  public ContrastGraphApiImpl(final ContrastSDK contrast, final Gson gson) {
    this.contrast = contrast;
    this.gson = gson;
    this.graphApiBase = ContrastSDKUtils.getServerUrl(contrast.getRestApiURL()) + "/api";
  }

  @Override
  public ContrastGraphResponse searchGraph(
      final String organizationId, final ContrastGraphRequest request) throws IOException {
    final String url =
        graphApiBase
            + new URIBuilder()
                .appendPathSegments("v2", "organizations", organizationId, "contrast-graph")
                .toURIString();
    return post(url, gson.toJson(request), ContrastGraphResponse.class);
  }

  @Override
  public ContrastGraphResponse getIncidentGraph(
      final String organizationId, final String incidentId) throws IOException {
    final String url =
        graphApiBase
            + new URIBuilder()
                .appendPathSegments(
                    "v2",
                    "organizations",
                    organizationId,
                    "contrast-graph",
                    "incidents",
                    incidentId)
                .toURIString();
    return get(url, ContrastGraphResponse.class);
  }

  @Override
  public FacetsResponse getFacets(
      final String organizationId, final String filterName, final RequestFilters filters)
      throws IOException {
    final String url =
        graphApiBase
            + new URIBuilder()
                .appendPathSegments(
                    "v2",
                    "organizations",
                    organizationId,
                    "contrast-graph",
                    "facets",
                    filterName)
                .toURIString();
    return post(url, gson.toJson(filters), FacetsResponse.class);
  }

  @Override
  public ApplicationLibrariesResponse getApplicationLibraries(
      final String organizationId,
      final String applicationId,
      final String agentReportingInstanceId,
      final ApplicationLibrariesRequest request)
      throws IOException {
    final String url =
        graphApiBase
            + new URIBuilder()
                .appendPathSegments(
                    "v2",
                    "organizations",
                    organizationId,
                    "contrast-graph",
                    "applications",
                    applicationId,
                    "libraries")
                .appendQueryParam("agentReportingInstanceId", agentReportingInstanceId)
                .toURIString();
    return post(url, request != null ? gson.toJson(request) : null, ApplicationLibrariesResponse.class);
  }

  @Override
  public LibraryDetailsResponse getApplicationLibraryDetails(
      final String organizationId, final String applicationId, final String libraryHash)
      throws IOException {
    final String url =
        graphApiBase
            + new URIBuilder()
                .appendPathSegments(
                    "v2",
                    "organizations",
                    organizationId,
                    "contrast-graph",
                    "applications",
                    applicationId,
                    "libraries",
                    libraryHash)
                .toURIString();
    return get(url, LibraryDetailsResponse.class);
  }

  private <T> T get(final String url, final Class<T> type) throws IOException {
    try (InputStream is = contrast.makeRequestToUrl(HttpMethod.GET, url)) {
      return parse(is, type);
    }
  }

  private <T> T post(final String url, final String body, final Class<T> type) throws IOException {
    try (InputStream is = contrast.makeRequestWithBodyToUrl(HttpMethod.POST, url, body, MediaType.JSON)) {
      return parse(is, type);
    }
  }

  private <T> T parse(final InputStream is, final Class<T> type) throws IOException {
    try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
      return gson.fromJson(reader, type);
    } catch (JsonParseException e) {
      throw new ServerResponseException("Failed to parse Contrast Graph API response", e);
    }
  }
}
