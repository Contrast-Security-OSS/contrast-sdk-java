package com.contrastsecurity.sdk.scan;

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

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ServerResponseException;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.URIBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
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

/** Implementation of {@link CodeArtifactClient}. */
final class CodeArtifactClientImpl implements CodeArtifactClient {

  private final ContrastSDK contrast;
  private final Gson gson;
  private final String organizationId;

  CodeArtifactClientImpl(final ContrastSDK contrast, final Gson gson, final String organizationId) {
    this.contrast = Objects.requireNonNull(contrast);
    this.gson = Objects.requireNonNull(gson);
    this.organizationId = Objects.requireNonNull(organizationId);
  }

  @Override
  public CodeArtifactInner upload(final String projectId, final Path file) throws IOException {
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
      return gson.fromJson(reader, AutoValue_CodeArtifactInner.class);
    } catch (JsonParseException e) {
      throw new ServerResponseException("Failed to parse Contrast API response", e);
    }
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
