package com.contrastsecurity.sdk.scan;

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
    final ContrastSDK contrast =
        new ContrastSDK.Builder("username", "my-service-key", "my-api-key").build();
    final ScanManager scanManager = contrast.scan("organization-id");

    final Projects projects = scanManager.projects();
    final Project project =
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
                  } catch (final IOException e) {
                    throw new UncheckedIOException(e);
                  }
                });
    final Path file = Paths.get("./target/spring-test-application.jar");
    final CodeArtifact codeArtifact = project.codeArtifacts().upload(file);
    final Scan scan = project.scans().define().withExistingCodeArtifact(codeArtifact).create();
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    try {
      scan.await(scheduler)
          .thenAccept(
              s -> {
                try {
                  s.saveSarif(Paths.get("./contrast-scan-results.sarif.json"));
                  final ScanSummary summary = s.summary();
                  System.out.printf("Found %d total results%n", summary.totalResults());
                } catch (final IOException e) {
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