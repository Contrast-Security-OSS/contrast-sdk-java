package com.contrastsecurity.sdk.internal;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/** Factory for configuring an instance of GSON that is compatible with the Contrast API */
public final class GsonFactory {

  /** @return new {@code GsonBuilder} */
  public static GsonBuilder builder() {
    return new GsonBuilder()
        .registerTypeAdapter(Instant.class, new InstantTypeAdapter().nullSafe());
  }

  /** @return new {@code Gson} */
  public static Gson create() {
    return builder().create();
  }

  /** static members only */
  private GsonFactory() {}

  /** {@code TypeAdapter} for (de)serializing {@code Instant} in ISO8601 */
  private static final class InstantTypeAdapter extends TypeAdapter<Instant> {

    @Override
    public void write(final JsonWriter writer, final Instant value) throws IOException {
      final String formatted = DateTimeFormatter.ISO_INSTANT.format(value);
      writer.value(formatted);
    }

    @Override
    public Instant read(final JsonReader reader) throws IOException {
      final String rfc3339 = reader.nextString();
      final TemporalAccessor accessor = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(rfc3339);
      return Instant.from(accessor);
    }
  }
}
