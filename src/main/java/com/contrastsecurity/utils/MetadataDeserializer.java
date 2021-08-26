package com.contrastsecurity.utils;

/*-
 * #%L
 * Contrast Java SDK
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
