package com.contrastsecurity.sdk.scan;

import static org.assertj.core.api.Assertions.assertThat;

import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.ContrastSDK.Builder;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link ScanManagerImpl} */
final class ScanManagerImplTest {

  /**
   * {@code ScanManagerImpl} simply initializes resource collections and provides access to them.
   * This test verifies that this happens without errors.
   */
  @Test
  void initialization_smoke_test() {
    final ContrastSDK contrast = new Builder("username", "service-key", "api-key").build();
    final Gson gson = GsonFactory.create();
    final ScanManagerImpl manager = new ScanManagerImpl(contrast, gson, "organization-id");
    final Scans scans = manager.scans("project-id");
    assertThat(scans).isNotNull();
    final CodeArtifacts codeArtifacts = manager.codeArtifacts("project-id");
    assertThat(codeArtifacts).isNotNull();
  }
}
