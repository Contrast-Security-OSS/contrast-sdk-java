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

import com.contrastsecurity.maven.plugin.it.stub.ConnectionParameters;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

/**
 * Reusable static factory methods for creating new Maven Verifier instances from well-known sample
 * projects
 */
final class Verifiers {

  /**
   * @return new {@link Verifier} for the /it/spring-boot sample Maven project
   */
  static Verifier springBoot(final ConnectionParameters connection)
      throws IOException, VerificationException {
    final String path = "/it/spring-boot";
    return verifier(connection, path);
  }

  /**
   * @return new {@link Verifier} for the /it/parent-pom sample Maven project
   */
  static Verifier parentPOM(final ConnectionParameters connection)
      throws IOException, VerificationException {
    final String path = "/it/parent-pom";
    return verifier(connection, path);
  }

  private static Verifier verifier(final ConnectionParameters connection, final String path)
      throws IOException, VerificationException {
    final File projectDir = ResourceExtractor.simpleExtractResources(Verifiers.class, path);
    final Verifier verifier = new Verifier(projectDir.getAbsolutePath());
    final String testRepository =
        Objects.requireNonNull(
            System.getProperty("contrast.test-repository"),
            "required system property contrast.test-repository must be set to a directory containing a maven repository with the contrast-maven-plugin installed");
    verifier.setLocalRepo(testRepository);
    // AND the user provides common agent configuration connection parameters for connecting to
    // Contrast
    final Properties connectionProperties = connection.toProperties();
    verifier.setSystemProperties(connectionProperties);
    return verifier;
  }

  /** static members only */
  private Verifiers() {}
}
