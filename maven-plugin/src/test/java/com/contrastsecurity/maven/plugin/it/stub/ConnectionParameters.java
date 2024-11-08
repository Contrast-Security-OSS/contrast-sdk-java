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

import com.google.auto.value.AutoValue;
import java.util.Properties;

/**
 * Value type that holds all of the connection configuration that a consumer needs to access
 * authenticated Contrast API endpoints.
 */
@AutoValue
public abstract class ConnectionParameters {

  /**
   * @return new {@link Builder}
   */
  public static Builder builder() {
    return new AutoValue_ConnectionParameters.Builder();
  }

  /**
   * @return Contrast API URL e.g. https://app.contrastsecurity.com/Contrast/api
   */
  public abstract String url();

  /**
   * @return Contrast API username
   */
  public abstract String username();

  /**
   * @return Contrast API Key
   */
  public abstract String apiKey();

  /**
   * @return Contrast API service key
   */
  public abstract String serviceKey();

  /**
   * @return Contrast organization ID
   */
  public abstract String organizationID();

  /**
   * @return new {@link Properties} object populated with the connection configuration with <a
   *     href="https://docs.contrastsecurity.com/en/java-configuration.html#java-system-properties">standard
   *     Contrast Java system property names</a>
   */
  public final Properties toProperties() {
    final Properties properties = new Properties();
    properties.setProperty("contrast.api.url", url());
    properties.setProperty("contrast.api.user_name", username());
    properties.setProperty("contrast.api.api_key", apiKey());
    properties.setProperty("contrast.api.service_key", serviceKey());
    properties.setProperty("contrast.api.organization_id", organizationID());
    return properties;
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder url(String value);

    public abstract Builder username(String value);

    public abstract Builder apiKey(String value);

    public abstract Builder serviceKey(String value);

    public abstract Builder organizationID(String value);

    public abstract ConnectionParameters build();
  }
}
