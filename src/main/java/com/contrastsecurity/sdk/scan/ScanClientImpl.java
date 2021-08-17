package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.MediaType;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

final class ScanClientImpl implements ScanClient {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String organizationId;

  ScanClientImpl(final ContrastSDK contrast, final Gson gson, final String organizationId) {
    this.contrast = contrast;
    this.gson = gson;
    this.organizationId = organizationId;
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
  public ScanSummaryInner getSummary(final String projectId, final String scanId) throws IOException {
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
    }
  }
}
