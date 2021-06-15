package com.contrastsecurity.http;

import com.contrastsecurity.models.SecurityCheck;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/** A wrapper object for the response of a security check request */
@Getter
public class SecurityCheckResponse {
  /**
   * The resulting security check
   *
   * @return the security check
   */
  @SerializedName("security_check")
  private SecurityCheck securityCheck;
}
