package com.contrastsecurity.exceptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Unit tests for {@link UnauthorizedException}. */
final class UnauthorizedExceptionTest {

  @CsvSource({"401,Unauthorized", "403,Forbidden"})
  @ParameterizedTest
  void captures_authorization_response_codes(final int code, final String status) {
    final String message = "Ah ah ah";
    final UnauthorizedException exception =
        new UnauthorizedException(message, "GET", "/fails", code, status);
    assertThat(exception.getCode()).isEqualTo(code);
    assertThat(exception.getStatus()).isEqualTo(status);
    assertThat(exception.getMessage())
        .isEqualTo(message + "\nGET /fails\n\n" + code + " " + status);
  }

  @SuppressWarnings("ThrowableNotThrown")
  @Test
  void throws_on_non_authorizations_response_code() {
    assertThatThrownBy(() -> new UnauthorizedException("all good", "GET", "/successful", 200, "OK"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  /**
   * Verifies that the legacy behavior has a known bug. We need to keep this bug working as-is while
   * the legacy code exists. {@see UnauthorizedException(int)}
   */
  @Test
  void legacy_constructor_allows_other_status_codes() {
    final int code = 200;
    final UnauthorizedException exception = new UnauthorizedException(code);
    assertThat(exception.getCode()).isEqualTo(code);
  }
}
