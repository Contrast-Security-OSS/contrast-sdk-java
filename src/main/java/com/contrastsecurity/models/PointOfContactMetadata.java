package com.contrastsecurity.models;

public class PointOfContactMetadata extends MetadataEntity {

  public PointOfContactMetadata() {
    type = MetadataType.POINT_OF_CONTACT;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  private String contactName;

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  private String phoneNo;

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }
}
