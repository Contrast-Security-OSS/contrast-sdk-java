/**
 * JUnit extension for stubbing the Contrast API for integration testing. Before a test, the
 * extension starts a new web server that simulates the subset of the Contrast API that the plugin
 * needs. At the conclusion of the test, the extension terminates the web server.
 *
 * <p>Some tests may be compatible with an external Contrast API system (that has already been
 * configured to be in the right state) instead of a stub. In this case, test authors can configure
 * this extension (using standard JUnit configuration) to provide connection parameters to the
 * external system instead of starting a stub system.
 *
 * <p>Set the following configuration parameters to configure the extension to use an external
 * Contrast API system instead of starting a stub:
 *
 * <ul>
 *   <li>{@code contrast.api.url}
 *   <li>{@code contrast.api.user_name}
 *   <li>{@code contrast.api.api_key}
 *   <li>{@code contrast.api.service_key}
 *   <li>{@code contrast.api.organization}
 * </ul>
 */
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
