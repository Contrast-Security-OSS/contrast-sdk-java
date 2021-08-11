package com.contrastsecurity.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/** Factory for configuring an instance of GSON that is compatible with the Scan API */
final class GsonFactory {

  static Gson create() {
    return new GsonBuilder()
        .registerTypeAdapter(Instant.class, new InstantTypeAdapter().nullSafe())
        .create();
  }

  /** static members only */
  private GsonFactory() {}

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
