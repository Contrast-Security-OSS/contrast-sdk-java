package com.contrastsecurity.models;

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
