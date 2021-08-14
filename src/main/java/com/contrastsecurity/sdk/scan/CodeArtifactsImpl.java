package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

final class CodeArtifactsImpl implements CodeArtifacts {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String organizationId;
  private final String projectId;

  CodeArtifactsImpl(
      final ContrastSDK contrast, final String organizationId, final String projectId) {
    this.contrast = Objects.requireNonNull(contrast);
    this.gson = GsonFactory.builder().create();
    this.organizationId = Objects.requireNonNull(organizationId);
    this.projectId = Objects.requireNonNull(projectId);
  }

  @Override
  public CodeArtifact upload(final Path file, final String name) throws IOException {
    final String uri =
        contrast.getRestApiURL()
            + new URIBuilder()
                .appendPathSegments(
                    "sast",
                    "organizations",
                    organizationId,
                    "projects",
                    projectId,
                    "code-artifacts")
                .toURIString();
    final String boundary = "ContrastFormBoundary" + ThreadLocalRandom.current().nextLong();
    final String header =
        "--"
            + boundary
            + CRLF
            + "Content-Disposition: form-data; name=\"filename\"; filename=\""
            + file.getFileName().toString()
            + '"'
            + CRLF
            + "Content-Type: "
            + determineMime(file)
            + CRLF
            + "Content-Transfer-Encoding: binary"
            + CRLF
            + CRLF;
    final String footer = CRLF + "--" + boundary + "--" + CRLF;
    final long contentLength = header.length() + Files.size(file) + footer.length();

    final HttpURLConnection connection = contrast.makeConnection(uri, "POST");
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    connection.setFixedLengthStreamingMode(contentLength);
    try (OutputStream os = connection.getOutputStream();
        PrintWriter writer =
            new PrintWriter(new OutputStreamWriter(os, StandardCharsets.US_ASCII), true)) {
      writer.append(header).flush();
      Files.copy(file, os);
      os.flush();
      writer.append(footer).flush();
    }
    final int code = connection.getResponseCode();
    if (code != 200 && code != 201) {
      throw HttpResponseException.fromConnection(
          connection, "Failed to upload code artifact to Contrast Scan");
    }
    try (Reader reader = new InputStreamReader(connection.getInputStream())) {
      return gson.fromJson(reader, AutoValue_CodeArtifactImpl.class);
    }
  }

  @Override
  public CodeArtifact upload(final Path file) throws IOException {
    return upload(file, file.getFileName().toString());
  }

  /**
   * Guesses the mime type from the file extension. Returns the arbitrary "application/octet-stream"
   * if no mime type can be inferred from the file extension.
   *
   * <p>Visible for testing
   */
  static String determineMime(final Path file) throws IOException {
    // trust the content type Java can determine
    final String contentType = Files.probeContentType(file);
    if (contentType != null) {
      return contentType;
    }
    // special checks for Java archive types, because not all of these types are identified by
    // Files.probeContentType(file) and we want to make sure we handle Java extensions correctly
    // since users of this code will most likely be uploading Java artifacts
    final String name = file.getFileName().toString();
    if (name.endsWith(".jar") || name.endsWith(".war") || name.endsWith(".ear")) {
      return "application/java-archive";
    }
    return "application/octet-stream";
  }

  private static final String CRLF = "\r\n";
}
