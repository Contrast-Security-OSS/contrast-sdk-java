package com.contrastsecurity.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link ContrastSDKUtils} */
final class ContrastSDKUtilsTest {

  @Test
  public void ensure_api_conditionally_transforms_input_to_match_api_uri() {
    final String expectedApi = "http://localhost:19080/Contrast/api";
    final String ensureUrlOne = ContrastSDKUtils.ensureApi("http://localhost:19080/Contrast/");
    final String ensureUrlTwo = ContrastSDKUtils.ensureApi("http://localhost:19080/Contrast");

    assertThat(ensureUrlOne).isEqualTo(expectedApi);
    assertThat(ensureUrlTwo).isEqualTo(expectedApi);

    final String unchangedApi = "http://localhost:19080/";
    final String ensureUnchanged = ContrastSDKUtils.ensureApi(unchangedApi);

    assertThat(ensureUnchanged).isEqualTo(unchangedApi);
  }

  @Test
  public void ensure_api_handles_urls_with_incorrect_schemes() {
    final String expectedUrl = "htp:/localhost:19080/Contrast/api";
    final String badUrl = "htp:/localhost:19080/Contrast/";
    final String actualUrl = ContrastSDKUtils.ensureApi(badUrl);
    assertThat(expectedUrl).isEqualTo(actualUrl);
  }

  @Test
  public void ensure_api_ignores_null() {
    final String nullUrl = ContrastSDKUtils.ensureApi(null);
    assertThat(nullUrl).isNull();
  }

  @Test
  public void ensure_api_handles_blank_string() {
    final String blankUrl = ContrastSDKUtils.ensureApi("");
    final String ensureBlank = "";
    assertThat(blankUrl).isEqualTo(ensureBlank);
  }
}
