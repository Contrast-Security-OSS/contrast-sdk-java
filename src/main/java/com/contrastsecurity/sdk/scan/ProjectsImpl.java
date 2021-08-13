package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.ContrastException;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.contrastsecurity.sdk.scan.Project.Definition;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Optional;

final class ProjectsImpl implements Projects {

  private final String organizationId;
  private final ContrastSDK contrast;
  private final Gson gson;

  ProjectsImpl(final String organizationId, final ContrastSDK contrast) {
    this.organizationId = organizationId;
    this.contrast = contrast;
    this.gson =
        GsonFactory.builder()
            .registerTypeAdapter(
                Project.class, (InstanceCreator<ProjectImpl>) type -> new ProjectImpl(contrast))
            .create();
  }

  @Override
  public Definition define() {
    return new ProjectImpl.Definition(contrast, gson, organizationId);
  }

  @Override
  public Optional<Project> findByName(final String name) throws IOException {
    // requests made with ContrastSDK.makeRequest must have their path prepended with "/"
    final ProjectsQuery query = ProjectsQuery.builder().name(name).unique(true).build();
    final String uri =
        new URIBuilder()
            .appendPathSegments("sast", "organizations", organizationId, "projects")
            .appendQueryParam("name", query.name())
            .appendQueryParam("unique", query.unique())
            .toURIString();
    final ScanPagedResult<Project> page;
    try (Reader reader = new InputStreamReader(contrast.makeRequest(HttpMethod.GET, uri))) {
      page = gson.fromJson(reader, new TypeToken<ScanPagedResult<ProjectImpl>>() {}.getType());
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
