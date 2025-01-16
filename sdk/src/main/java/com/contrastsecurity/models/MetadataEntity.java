package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2025 Contrast Security, Inc.
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

import com.contrastsecurity.exceptions.InvalidConversionException;
import com.google.gson.annotations.SerializedName;

public abstract class MetadataEntity {

  public enum MetadataType {
    STRING,
    NUMERIC,
    POINT_OF_CONTACT;

    @Override
    public String toString() {
      return name().toUpperCase();
    }
  };

  protected MetadataType type;

  public MetadataType getType() {
    return type;
  }

  public void setType(String type) {
    this.type = MetadataType.valueOf(type);
  }

  @SerializedName("fieldName")
  protected String fieldName;

  public String getFieldName() {
    return fieldName;
  }

  public FreeformMetadata getAsFreeformMetadata() throws InvalidConversionException {
    if (type == MetadataType.STRING) {
      return (FreeformMetadata) this;
    } else {
      throw new InvalidConversionException(type.toString(), MetadataType.STRING.toString());
    }
  }

  public NumericMetadata getAsNumericMetadata() throws InvalidConversionException {
    if (type == MetadataType.NUMERIC) {
      return (NumericMetadata) this;
    } else {
      throw new InvalidConversionException(type.toString(), MetadataType.NUMERIC.toString());
    }
  }

  public PointOfContactMetadata getAsPointOfContactMetadata() throws InvalidConversionException {
    if (type == MetadataType.POINT_OF_CONTACT) {
      return (PointOfContactMetadata) this;
    } else {
      throw new InvalidConversionException(
          type.toString(), MetadataType.POINT_OF_CONTACT.toString());
    }
  }
}
