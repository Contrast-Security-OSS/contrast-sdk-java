package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
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

import com.contrastsecurity.sdk.ContrastSDK;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/** Code sample that shows how to use the Contrast SDK to integrate with Contrast Scan. */
@SuppressWarnings("unused")
final class Sample {

  static void sample() throws IOException {
    // BEGIN: scan-sample
    ContrastSDK contrast =
        new ContrastSDK.Builder("username", "my-service-key", "my-api-key").build();
    ScanManager scanManager = contrast.scan("organization-id");

    Projects projects = scanManager.projects();
    Project project =
        projects
            .findByName("spring-test-application")
            .orElseGet(
                () -> {
                  try {
                    return projects
                        .define()
                        .withName("spring-test-application")
                        .withLanguage("JAVA")
                        .create();
                  } catch (IOException e) {
                    throw new UncheckedIOException(e);
                  }
                });
    Path file = Paths.get("./target/spring-test-application.jar");
    CodeArtifact codeArtifact = project.codeArtifacts().upload(file);
    Scan scan = project.scans().define().withExistingCodeArtifact(codeArtifact).create();
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    try {
      scan.await(scheduler)
          .thenAccept(
              s -> {
                try {
                  s.saveSarif(Paths.get("./contrast-scan-results.sarif.json"));
                  ScanSummary summary = s.summary();
                  System.out.printf("Found %d total results%n", summary.totalResults());
                } catch (IOException e) {
                  throw new UncheckedIOException(e);
                }
              })
          .toCompletableFuture()
          .join();
    } finally {
      scheduler.shutdown();
    }
    // END: scan-sample
  }
}
