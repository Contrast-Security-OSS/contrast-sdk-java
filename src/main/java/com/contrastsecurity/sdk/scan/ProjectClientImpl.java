package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.ContrastException;
import com.contrastsecurity.exceptions.ServerResponseException;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.MediaType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.Optional;

/** Implementation of {@link ProjectClient}. */
final class ProjectClientImpl implements ProjectClient {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String organizationId;

  ProjectClientImpl(final ContrastSDK contrast, final Gson gson, final String organizationId) {
    this.contrast = Objects.requireNonNull(contrast);
    this.gson = Objects.requireNonNull(gson);
    this.organizationId = Objects.requireNonNull(organizationId);
  }

  @Override
  public ProjectInner create(final ProjectCreate create) throws IOException {
    final String path =
        new URIBuilder()
            .appendPathSegments("sast", "organizations", organizationId, "projects")
            .toURIString();
    final String json = gson.toJson(create);
    try (Reader reader =
        new InputStreamReader(
            contrast.makeRequestWithBody(HttpMethod.POST, path, json, MediaType.JSON))) {
      return gson.fromJson(reader, AutoValue_ProjectInner.class);
    } catch (JsonParseException e) {
      throw new ServerResponseException("Failed to parse Contrast API response", e);
    }
  }

  @Override
  public Optional<ProjectInner> findByName(final String name) throws IOException {
    // requests made with ContrastSDK.makeRequest must have their path prepended with "/"
    final ProjectsQuery query = ProjectsQuery.builder().name(name).unique(true).build();
    final String uri =
        new URIBuilder()
            .appendPathSegments("sast", "organizations", organizationId, "projects")
            .appendQueryParam("name", query.name())
            .appendQueryParam("unique", query.unique())
            .toURIString();
    final ScanPagedResult<ProjectInner> page;
    try (Reader reader = new InputStreamReader(contrast.makeRequest(HttpMethod.GET, uri))) {
      page =
          gson.fromJson(
              reader, new TypeToken<ScanPagedResult<AutoValue_ProjectInner>>() {}.getType());
    } catch (JsonParseException e) {
      throw new ServerResponseException("Failed to parse Contrast API response", e);
    }

    // the Scan API reuses a paged response structure even when we specify the query parameter
    // "unique=true". When "unique=true", there should be at most one scan. If this is not true,
    // throw an exception, because something is wrong with the Scan API.
    if (page.getTotalElements() > 1) {
      throw new ContrastException(
          "Expected Contrast to return exactly one project with the given name, or no projects, but returned "
              + page.getTotalElements()
              + " projects");
    }

    // return the project, or null if no such project is found
    return page.getTotalElements() == 1 ? Optional.of(page.getContent().get(0)) : Optional.empty();
  }
}
