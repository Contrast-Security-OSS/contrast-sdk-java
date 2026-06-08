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

    public ContrastGraphApiImpl(final ContrastSDK contrast, final Gson gson) {
        this.contrast = contrast;
        this.gson = gson;
    }

    @Override
    public ContrastGraphResponse searchGraph(
            final String organizationId, final ContrastGraphRequest request) throws IOException {
        final String uri = new URIBuilder()
                .appendPathSegments("v2", "organizations", organizationId, "contrast-graph")
                .toURIString();
        try (InputStream is = contrast.makeRequestWithBody(
                HttpMethod.POST, uri, gson.toJson(request), MediaType.JSON);
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, ContrastGraphResponse.class);
        } catch (JsonParseException e) {
            throw new ServerResponseException("Failed to parse Contrast Graph API response", e);
        }
    }

    @Override
    public ContrastGraphResponse getIncidentGraph(
            final String organizationId, final String incidentId) throws IOException {
        final String uri = new URIBuilder()
                .appendPathSegments(
                        "v2", "organizations", organizationId,
                        "contrast-graph", "incidents", incidentId)
                .toURIString();
        try (InputStream is = contrast.makeRequest(HttpMethod.GET, uri);
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, ContrastGraphResponse.class);
        } catch (JsonParseException e) {
            throw new ServerResponseException("Failed to parse Contrast Graph API response", e);
        }
    }

    @Override
    public FacetsResponse getFacets(
            final String organizationId,
            final String filterName,
            final RequestFilters filters) throws IOException {
        final String uri = new URIBuilder()
                .appendPathSegments(
                        "v2", "organizations", organizationId,
                        "contrast-graph", "facets", filterName)
                .toURIString();
        try (InputStream is = contrast.makeRequestWithBody(
                HttpMethod.POST, uri, gson.toJson(filters), MediaType.JSON);
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, FacetsResponse.class);
        } catch (JsonParseException e) {
            throw new ServerResponseException("Failed to parse Contrast Graph API response", e);
        }
    }

    @Override
    public ApplicationLibrariesResponse getApplicationLibraries(
            final String organizationId,
            final String applicationId,
            final String agentReportingInstanceId,
            final ApplicationLibrariesRequest request) throws IOException {
        final String uri = new URIBuilder()
                .appendPathSegments(
                        "v2", "organizations", organizationId,
                        "contrast-graph", "applications", applicationId, "libraries")
                .appendQueryParam("agentReportingInstanceId", agentReportingInstanceId)
                .toURIString();
        final String body = request != null ? gson.toJson(request) : null;
        final MediaType mediaType = request != null ? MediaType.JSON : null;
        try (InputStream is = contrast.makeRequestWithBody(HttpMethod.POST, uri, body, mediaType);
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, ApplicationLibrariesResponse.class);
        } catch (JsonParseException e) {
            throw new ServerResponseException("Failed to parse Contrast Graph API response", e);
        }
    }

    @Override
    public LibraryDetailsResponse getApplicationLibraryDetails(
            final String organizationId,
            final String applicationId,
            final String libraryHash) throws IOException {
        final String uri = new URIBuilder()
                .appendPathSegments(
                        "v2", "organizations", organizationId,
                        "contrast-graph", "applications", applicationId,
                        "libraries", libraryHash)
                .toURIString();
        try (InputStream is = contrast.makeRequest(HttpMethod.GET, uri);
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, LibraryDetailsResponse.class);
        } catch (JsonParseException e) {
            throw new ServerResponseException("Failed to parse Contrast Graph API response", e);
        }
    }
}
