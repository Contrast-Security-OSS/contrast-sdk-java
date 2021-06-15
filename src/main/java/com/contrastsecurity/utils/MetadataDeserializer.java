package com.contrastsecurity.utils;

import com.contrastsecurity.models.FreeformMetadata;
import com.contrastsecurity.models.MetadataEntity;
import com.contrastsecurity.models.NumericMetadata;
import com.contrastsecurity.models.PointOfContactMetadata;
import com.google.gson.*;
import java.lang.reflect.Type;

public class MetadataDeserializer implements JsonDeserializer<MetadataEntity> {
  @Override
  public MetadataEntity deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    JsonElement type = jsonObject.get("type");
    if (type != null) {
      switch (type.getAsString()) {
        case "STRING":
          return context.deserialize(jsonObject, FreeformMetadata.class);
        case "NUMERIC":
          return context.deserialize(jsonObject, NumericMetadata.class);
        case "PERSON_OF_CONTACT":
          PointOfContactMetadata poiMetadata = new PointOfContactMetadata();
          poiMetadata.setFieldName(jsonObject.get("fieldName").getAsString());
          JsonArray subFields = jsonObject.getAsJsonArray("subfields");
          for (JsonElement subfieldElement : subFields) {
            JsonObject subField = subfieldElement.getAsJsonObject();
            JsonElement subfieldType = subField.get("type");
            switch (subfieldType.getAsString()) {
              case "CONTACT_NAME":
                poiMetadata.setContactName(subField.get("fieldValue").getAsString());
                break;
              case "EMAIL":
                poiMetadata.setEmail(subField.get("fieldValue").getAsString());
                break;
              case "PHONE":
                poiMetadata.setPhoneNo(subField.get("fieldValue").getAsString());
                break;
            }
          }
          return poiMetadata;
      }
    }
    return null;
  }
}
