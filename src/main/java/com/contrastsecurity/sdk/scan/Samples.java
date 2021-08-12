package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.ContrastSDK.Builder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

final class Samples {

  public static void main(final String[] args) throws IOException {
    final ContrastSDK contrast =
        new Builder("johnathan.gilday@contrastsecurity.com", "my-service-key", "my-api-key")
            .build();
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
    project
        .uploadCodeArtifact(file)
        .startScan()
        .await()
        .thenAccept(
            s -> {
              s.saveSarif(Paths.get("./contrast-scan-results.sarif.json"));
              final ScanSummary summary = s.summary();
              System.out.printf("Found %d total results%n", summary.totalResults());
            });
  }
}
