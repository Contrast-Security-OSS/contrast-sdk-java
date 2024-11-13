package com.contrastsecurity.maven.plugin.it.stub;

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

import java.util.Optional;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * JUnit 5 test extension that provides test authors with a stubbed instance of the Contrast API.
 */
public final class ContrastAPIStubExtension
    implements ParameterResolver, BeforeAllCallback, AfterAllCallback {

  /**
   * Starts the {@link ContrastAPI}
   *
   * @param context JUnit context
   */
  @Override
  public void beforeAll(final ExtensionContext context) {
    final ContrastAPI contrast = getContrastAPI(context);
    contrast.start();
    context.publishReportEntry("Contrast stub started " + contrast.connection().url());
  }

  /**
   * Stops the {@link ContrastAPI}
   *
   * @param context JUnit context
   */
  @Override
  public void afterAll(final ExtensionContext context) {
    final ContrastAPI contrast = getContrastAPI(context);
    contrast.stop();
  }

  /**
   * @return true if the parameter is of type {@link ContrastAPI}
   */
  @Override
  public boolean supportsParameter(
      final ParameterContext parameterContext, final ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType() == ContrastAPI.class;
  }

  /**
   * @return the {@link ContrastAPI} in the current test context
   */
  @Override
  public Object resolveParameter(
      final ParameterContext parameterContext, final ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return getContrastAPI(extensionContext);
  }

  /**
   * @return new or existing {@link ContrastAPI} in the current test context
   */
  private static ContrastAPI getContrastAPI(final ExtensionContext context) {
    return context
        .getStore(NAMESPACE)
        .getOrComputeIfAbsent(
            "server", ignored -> createFromConfiguration(context), ContrastAPI.class);
  }

  /**
   * @param context the current JUnit {@link ExtensionContext}
   * @return {@link ExternalContrastAPI} if Contrast connection properties are provided, otherwise
   *     fails. We leave open the possibility that we will once again provide a stubbed API e.g. one
   *     that uses Pactflow hosted stubs.
   */
  private static ContrastAPI createFromConfiguration(final ExtensionContext context) {
    // gather configuration parameters from the current context
    final Optional<String> url = context.getConfigurationParameter("contrast.api.url");
    final Optional<String> username = context.getConfigurationParameter("contrast.api.user_name");
    final Optional<String> apiKey = context.getConfigurationParameter("contrast.api.api_key");
    final Optional<String> serviceKey =
        context.getConfigurationParameter("contrast.api.service_key");
    final Optional<String> organization =
        context.getConfigurationParameter("contrast.api.organization_id");

    // if all connection parameters are present, then use end-to-end testing mode
    if (url.isPresent()
        && username.isPresent()
        && apiKey.isPresent()
        && serviceKey.isPresent()
        && organization.isPresent()) {
      context.publishReportEntry(
          "end-to-end testing enabled: using provided Contrast API connection instead of the stub");
      final ConnectionParameters connection =
          ConnectionParameters.builder()
              .url(url.get())
              .username(username.get())
              .apiKey(apiKey.get())
              .serviceKey(serviceKey.get())
              .organizationID(organization.get())
              .build();
      return new ExternalContrastAPI(connection);
    }
    throw new IllegalArgumentException(
        "Context lacks required configuration for connecting to an external Contrast instance");
  }

  private static final Namespace NAMESPACE = Namespace.create(ContrastAPIStubExtension.class);
}
