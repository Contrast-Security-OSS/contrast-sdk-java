package com.contrastsecurity.sdk.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link URIBuilder} */
final class URIBuilderTest {

  @Test
  void append_path_segments_encodes_segments() {
    final String uri = new URIBuilder().appendPathSegments("foo/bar", "🍔🍟").toURIString();
    assertThat(uri).isEqualTo("/foo%2Fbar/" + BURGER_AND_FRIES_ENCODED);
  }

  @Test
  void append_query_parameter_encodes_params() {
    final String uri = new URIBuilder().appendQueryParam("items", "🍔🍟").toURIString();
    assertThat(uri).isEqualTo("/?items=" + BURGER_AND_FRIES_ENCODED);
  }

  @Test
  void append_multiple_query_params() {
    final String uri =
        new URIBuilder()
            .appendPathSegments("nonsense")
            .appendQueryParam("foo", "bar")
            .appendQueryParam("wibble", "wubble")
            .toURIString();
    assertThat(uri).isEqualTo("/nonsense?foo=bar&wibble=wubble");
  }

  @Test
  void append_boolean_parameter() {
    final String uri = new URIBuilder().appendQueryParam("archived", false).toURIString();
    assertThat(uri).isEqualTo("/?archived=false");
  }

  @Test
  void throws_when_passed_empty_name() {
    final URIBuilder builder = new URIBuilder();
    assertThatThrownBy(() -> builder.appendQueryParam("", "foo"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  private static final String BURGER_AND_FRIES_ENCODED = "%F0%9F%8D%94%F0%9F%8D%9F";
}
