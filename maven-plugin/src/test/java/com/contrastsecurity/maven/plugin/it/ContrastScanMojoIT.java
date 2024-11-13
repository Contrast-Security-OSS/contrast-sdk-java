package com.contrastsecurity.maven.plugin.it;

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

import com.contrastsecurity.maven.plugin.it.stub.ContrastAPI;
import com.contrastsecurity.maven.plugin.it.stub.ContrastAPIStub;
import java.io.IOException;
import java.util.Arrays;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.junit.jupiter.api.Test;

/** Functional test for the "scan" goal */
@ContrastAPIStub
final class ContrastScanMojoIT {

  @Test
  void scan_submits_artifact_for_scanning(final ContrastAPI contrast)
      throws VerificationException, IOException {
    // GIVEN a spring-boot project that uses the plugin
    final Verifier verifier = Verifiers.springBoot(contrast.connection());

    // WHEN execute the "verify" goal
    verifier.setCliOptions(Arrays.asList("--activate-profiles", "scan"));
    verifier.executeGoal("verify");

    // THEN plugin submits the spring-boot application artifact for scanning
    verifier.verifyErrorFreeLog();
    verifier.verifyTextInLog(
        "Uploading spring-test-application-0.0.1-SNAPSHOT.jar to Contrast Scan");
    verifier.verifyTextInLog("Starting scan with label 0.0.1-SNAPSHOT");
    verifier.verifyTextInLog("Scan results will be available at http");
    verifier.verifyTextInLog("Waiting for scan results");
    verifier.verifyTextInLog("Scan completed");
    verifier.assertFilePresent("./target/contrast-scan-reports/contrast-scan-results.sarif.json");
  }

  @Test
  void fails_when_no_artifact_detected(final ContrastAPI contrast)
      throws VerificationException, IOException {
    // GIVEN a POM project that uses the plugin
    final Verifier verifier = Verifiers.parentPOM(contrast.connection());

    // WHEN execute the "verify" goal
    try {
      verifier.executeGoal("verify");
    } catch (VerificationException ignored) {
    }

    // THEN plugin fails because there is no artifact to scan
    verifier.verifyTextInLog(
        "Project's artifact file has not been set - see https://contrastsecurity.dev/contrast-maven-plugin/troubleshooting/artifact-not-set.html");
  }
}
