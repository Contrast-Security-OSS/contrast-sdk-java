package com.contrastsecurity.gradle.plugin;

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.UserAgentProduct;

public class ContrastSDKService {

  private static ContrastSDK INSTANCE = null;

  public static void initializeSdk(
      final String username, final String serviceKey, final String apiKey, final String apiUrl) {
    final UserAgentProduct gradle = UserAgentProduct.of("contrast-gradle-plugin");
      INSTANCE =
        new ContrastSDK.Builder(username, serviceKey, apiKey)
            .withApiUrl(apiUrl + "/api")
            // TODO JAVA-8883 figure out how to define this proxy
            // .withProxy(proxy) //with proxy?
            .withUserAgentProduct(gradle)
            .build();
  }

  public static ContrastSDK getSdk() {
    return INSTANCE;
  }

}
