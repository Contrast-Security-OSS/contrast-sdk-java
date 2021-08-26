package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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

import com.contrastsecurity.exceptions.ServerResponseException;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.MediaType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

/** Implementation of {@link ScanClient}. */
final class ScanClientImpl implements ScanClient {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String organizationId;

  ScanClientImpl(final ContrastSDK contrast, final Gson gson, final String organizationId) {
    this.contrast = Objects.requireNonNull(contrast);
    this.gson = Objects.requireNonNull(gson);
    this.organizationId = Objects.requireNonNull(organizationId);
  }

  @Override
  public ScanInner get(final String projectId, final String scanId) throws IOException {
    final String uri =
        new URIBuilder()
            .appendPathSegments(
                "sast", "organizations", organizationId, "projects", projectId, "scans", scanId)
            .toURIString();
    try (InputStream is = contrast.makeRequest(HttpMethod.GET, uri);
        Reader reader = new InputStreamReader(is)) {
      return gson.fromJson(reader, AutoValue_ScanInner.class);
    } catch (JsonParseException e) {
      throw new ServerResponseException("Failed to parse Contrast API response", e);
    }
  }

  @Override
  public ScanInner create(final String projectId, final ScanCreate create) throws IOException {
    final String uri =
        new URIBuilder()
            .appendPathSegments(
                "sast", "organizations", organizationId, "projects", projectId, "scans")
            .toURIString();
    // TODO add a makeRequestWithBody method that gives callers access to the OutputStream
    // directly
    final String json = gson.toJson(create);
    try (Reader reader =
        new InputStreamReader(
            contrast.makeRequestWithBody(HttpMethod.POST, uri, json, MediaType.JSON))) {
      return gson.fromJson(reader, AutoValue_ScanInner.class);
    } catch (JsonParseException e) {
      throw new ServerResponseException("Failed to parse Contrast API response", e);
    }
  }

  @Override
  public InputStream getSarif(final String projectId, final String scanId) throws IOException {
    final String uri =
        new URIBuilder()
            .appendPathSegments(
                "sast",
                "organizations",
                organizationId,
                "projects",
                projectId,
                "scans",
                scanId,
                "raw-output")
            .toURIString();
    return contrast.makeRequest(HttpMethod.GET, uri);
  }

  @Override
  public ScanSummaryInner getSummary(final String projectId, final String scanId)
      throws IOException {
    final String uri =
        new URIBuilder()
            .appendPathSegments(
                "sast",
                "organizations",
                organizationId,
                "projects",
                projectId,
                "scans",
                scanId,
                "summary")
            .toURIString();
    try (Reader reader = new InputStreamReader(contrast.makeRequest(HttpMethod.GET, uri))) {
      return gson.fromJson(reader, AutoValue_ScanSummaryInner.class);
    } catch (JsonParseException e) {
      throw new ServerResponseException("Failed to parse Contrast API response", e);
    }
  }
}
