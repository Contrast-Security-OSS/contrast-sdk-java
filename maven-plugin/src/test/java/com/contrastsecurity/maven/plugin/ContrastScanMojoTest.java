package com.contrastsecurity.maven.plugin;

/*-
 * #%L
 * Contrast Maven Plugin
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.contrastsecurity.sdk.scan.ScanSummary;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

/** Unit tests for {@link ContrastScanMojo} */
final class ContrastScanMojoTest {

  private ContrastScanMojo mojo;

  @BeforeEach
  void before() {
    mojo = new ContrastScanMojo();
    mojo.setOrganizationId("organization-id");
    mojo.setProjectName("project-id");
  }

  /**
   * Contrast MOJOs tolerate a variety of HTTP paths in the URL configuration. Regardless of the
   * path that the user has configured, the {@link ContrastScanMojo#createClickableScanURL} method
   * should generate the same URL
   */
  @ValueSource(
      strings = {
        "https://app.contrastsecurity.com/",
        "https://app.contrastsecurity.com/Contrast",
        "https://app.contrastsecurity.com/Contrast/api",
        "https://app.contrastsecurity.com/Contrast/api/"
      })
  @ParameterizedTest
  void it_generates_clickable_url(final String url) throws MojoFailureException {
    // GIVEN a scan mojo with known URL, organization ID, and project ID
    mojo.setURL(url);
    mojo.setOrganizationId("organization-id");

    // WHEN generate URL for the user to click-through to display the scan in their browser
    final String clickableScanURL =
        mojo.createClickableScanURL("project-id", "scan-id").toExternalForm();

    // THEN outputs expected URL
    assertThat(clickableScanURL)
        .isEqualTo(
            "https://app.contrastsecurity.com/Contrast/static/ng/index.html#/organization-id/scans/project-id/scans/scan-id");
  }

  @Test
  void it_prints_summary_to_console() {
    // GIVEN the plugin is configured to output scan results to the console
    mojo.setConsoleOutput(true);

    // WHEN print summary to console
    final int totalResults = 10;
    final int totalNewResults = 8;
    final int totalFixedResults = 1;
    final ScanSummary summary =
        FakeScanSummary.builder()
            .id("summary-id")
            .organizationId("organization-id")
            .projectId("project-id")
            .scanId("scan-id")
            .createdDate(Instant.now())
            .totalResults(totalResults)
            .totalNewResults(totalNewResults)
            .totalFixedResults(totalFixedResults)
            .duration(Duration.ofMillis(0))
            .build();
    @SuppressWarnings("unchecked")
    final Consumer<String> console = mock(Consumer.class);
    mojo.writeSummaryToConsole(summary, console);

    // THEN prints expected lines
    final List<String> expected =
        Arrays.asList(
            "Scan completed",
            "New Results\t" + totalNewResults,
            "Fixed Results\t" + totalFixedResults,
            "Total Results\t" + totalResults);
    final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(console, times(4)).accept(captor.capture());
    assertThat(captor.getAllValues()).hasSameElementsAs(expected);
  }

  @Test
  void it_may_be_configured_to_omit_summary_from_console() {
    // GIVEN the plugin is configured to omit scan results from the console
    mojo.setConsoleOutput(false);

    // WHEN print summary to console
    final ScanSummary summary =
        FakeScanSummary.builder()
            .id("summary-id")
            .organizationId("organization-id")
            .projectId("project-id")
            .scanId("scan-id")
            .createdDate(Instant.now())
            .totalResults(10)
            .totalNewResults(8)
            .totalFixedResults(1)
            .duration(Duration.ofMillis(0))
            .build();
    @SuppressWarnings("unchecked")
    final Consumer<String> console = mock(Consumer.class);
    mojo.writeSummaryToConsole(summary, console);

    // THEN only prints "completed" line
    verify(console).accept("Scan completed");
  }
}
