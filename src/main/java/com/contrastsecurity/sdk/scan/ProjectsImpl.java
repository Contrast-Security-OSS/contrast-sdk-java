package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.scan.Project.Definition;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Optional;

/** Implementation of the {@link Projects} resource collection. */
final class ProjectsImpl implements Projects {

  private final ProjectClient client;

  ProjectsImpl(final String organizationId, final ContrastSDK contrast, final Gson gson) {
    this.client = new ProjectClientImpl(contrast, gson, organizationId);
  }

  @Override
  public Definition define() {
    return new ProjectImpl.Definition(client);
  }

  @Override
  public Optional<Project> findByName(final String name) throws IOException {
    return client.findByName(name).map(ProjectImpl::new);
  }
}
