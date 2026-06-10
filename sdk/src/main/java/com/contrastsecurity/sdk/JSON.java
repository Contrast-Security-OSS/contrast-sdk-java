/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2026 Contrast Security, Inc.
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

package com.contrastsecurity.sdk;

import com.contrastsecurity.sdk.internal.GsonFactory;
import com.google.gson.Gson;

/**
 * Gson utility required by generated graph model classes (openapi-generator okhttp-gson output).
 *
 * <p>Delegates to {@link GsonFactory} so generated models share the same Gson configuration as the
 * rest of the SDK.
 */
public final class JSON {

  private JSON() {}

  public static Gson getGson() {
    return GsonFactory.create();
  }
}
