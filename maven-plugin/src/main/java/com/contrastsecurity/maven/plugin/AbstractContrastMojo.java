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

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.UserAgentProduct;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Settings;

/**
 * Abstract mojo for mojos that need to connect to Contrast. Handles authentication, organization
 * selection (multi-tenancy), and proxy configuration.
 *
 * <p>Extensions of this class use the {@link #connectToContrast()} to obtain an instance of the
 * {@link ContrastSDK} with which they can make requests to Contrast.
 */
abstract class AbstractContrastMojo extends AbstractMojo {

  @Parameter(defaultValue = "${settings}", readonly = true)
  private Settings settings;

  @Parameter(defaultValue = "${maven.version}", readonly = true)
  private String mavenVersion;

  /**
   * User name for communicating with Contrast. Agent users lack permissions required by this
   * plugin. <a href="https://docs.contrastsecurity.com/en/personal-keys.html">Find your personal
   * keys</a>
   */
  @Parameter(alias = "username", required = true)
  private String userName;

  /**
   * API Key for communicating with Contrast. <a
   * href="https://docs.contrastsecurity.com/en/personal-keys.html">Find your personal keys</a>
   */
  @Parameter(property = "apiKey", required = true)
  private String apiKey;

  /**
   * Service Key for communicating with Contrast. <a
   * href="https://docs.contrastsecurity.com/en/personal-keys.html">Find your personal keys</a>
   */
  @Parameter(property = "serviceKey", required = true)
  private String serviceKey;

  /** Contrast API URL */
  @Parameter(alias = "apiUrl", defaultValue = "https://app.contrastsecurity.com/Contrast/api")
  private String url;

  /**
   * Unique ID for the Contrast Organization to which the plugin reports results. <a
   * href="https://docs.contrastsecurity.com/en/personal-keys.html">Find your Organization ID</a>
   */
  // TODO[JG] must this be required? If a user is only in one org, we can look it up using the
  // endpoint /ng/profile/organizations
  @Parameter(alias = "orgUuid", required = true)
  private String organizationId;

  /**
   * When true, will override Maven's proxy settings with Contrast Maven plugin specific proxy
   * configuration
   *
   * @deprecated in a future release, we will remove the proprietary proxy configuration in favor of
   *     standard Maven proxy configuration
   * @since 2.8
   */
  @Deprecated
  @Parameter(property = "useProxy", defaultValue = "false")
  private boolean useProxy;

  /**
   * Proxy host used to communicate to Contrast when {@code useProxy} is true
   *
   * @deprecated in a future release, we will remove the proprietary proxy configuration in favor of
   *     standard Maven proxy configuration
   * @since 2.8
   */
  @Deprecated
  @Parameter(property = "proxyHost")
  private String proxyHost;

  /**
   * Proxy port used to communicate to Contrast when {@code useProxy} is true
   *
   * @deprecated in a future release, we will remove the proprietary proxy configuration in favor of
   *     standard Maven proxy configuration
   * @since 2.8
   */
  @Deprecated
  @Parameter(property = "proxyPort")
  private int proxyPort;

  String getMavenVersion() {
    return mavenVersion;
  }

  void setMavenVersion(final String mavenVersion) {
    this.mavenVersion = mavenVersion;
  }

  String getUserName() {
    return userName;
  }

  void setUserName(final String userName) {
    this.userName = userName;
  }

  String getApiKey() {
    return apiKey;
  }

  void setApiKey(final String apiKey) {
    this.apiKey = apiKey;
  }

  String getServiceKey() {
    return serviceKey;
  }

  void setServiceKey(final String serviceKey) {
    this.serviceKey = serviceKey;
  }

  String getURL() {
    return url;
  }

  void setURL(final String url) {
    this.url = url;
  }

  String getOrganizationId() {
    return organizationId;
  }

  void setOrganizationId(final String organizationId) {
    this.organizationId = organizationId;
  }

  /**
   * @return new ContrastSDK configured to connect with the authentication and proxy parameters
   *     defined by this abstract mojo
   * @throws MojoFailureException when fails to connect to Contrast
   */
  ContrastSDK connectToContrast() throws MojoFailureException {
    final Proxy proxy = getProxy();
    final UserAgentProduct maven = getUserAgentProduct();
    try {
      return new ContrastSDK.Builder(userName, serviceKey, apiKey)
          .withApiUrl(url)
          .withProxy(proxy)
          .withUserAgentProduct(maven)
          .build();
    } catch (final IllegalArgumentException e) {
      throw new MojoFailureException(
          "\n\nWe couldn't connect to Contrast at this address [" + url + "]. The error is: ", e);
    }
  }

  /**
   * visible for testing
   *
   * @return {@link UserAgentProduct} for the contrast-maven-plugin
   */
  final UserAgentProduct getUserAgentProduct() {
    final String comment = "Apache Maven " + mavenVersion;
    return UserAgentProduct.of("contrast-maven-plugin", Version.VERSION, comment);
  }

  private Proxy getProxy() throws MojoFailureException {
    Proxy proxy = Proxy.NO_PROXY;
    final org.apache.maven.settings.Proxy proxySettings = settings.getActiveProxy();
    if (useProxy) {
      getLog().info(String.format("Using a proxy %s:%s", proxyHost, proxyPort));
      if (proxyHost != null && proxyPort != 0) {
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
      } else {
        throw new MojoFailureException(
            "When useProxy is true, proxyHost and proxyPort is required.");
      }
    } else if (proxySettings != null) {
      getLog()
          .info(
              String.format(
                  "Using a proxy %s:%s", proxySettings.getHost(), proxySettings.getPort()));
      proxy =
          new Proxy(
              Proxy.Type.HTTP,
              new InetSocketAddress(proxySettings.getHost(), proxySettings.getPort()));

      if (proxySettings.getUsername() != null || proxySettings.getPassword() != null) {
        Authenticator.setDefault(
            new Authenticator() {
              @Override
              protected PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType() == RequestorType.PROXY
                    && getRequestingHost().equalsIgnoreCase(proxySettings.getHost())
                    && proxySettings.getPort() == getRequestingPort()) {
                  return new PasswordAuthentication(
                      proxySettings.getUsername(),
                      proxySettings.getPassword() == null
                          ? null
                          : proxySettings.getPassword().toCharArray());
                } else {
                  return null;
                }
              }
            });
      }
    }

    return proxy;
  }
}
