package com.contrastsecurity.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/** Factory for configuring an instance of GSON that is compatible with the Scan API */
final class GsonFactory {

  static Gson create() {
    return new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter().nullSafe())
        .create();
  }

  /** static members only */
  private GsonFactory() {}

  private static final class OffsetDateTimeTypeAdapter extends TypeAdapter<OffsetDateTime> {

    @Override
    public void write(final JsonWriter writer, final OffsetDateTime value) throws IOException {
      final String formatted = value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
      writer.value(formatted);
    }

    @Override
    public OffsetDateTime read(final JsonReader reader) throws IOException {
      final String iso8601 = reader.nextString();
      return OffsetDateTime.parse(iso8601, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
  }
}
