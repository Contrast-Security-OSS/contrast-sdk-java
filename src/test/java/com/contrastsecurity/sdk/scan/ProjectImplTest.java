package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.ContrastSDK.Builder;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;

/** Unit tests for {@link ProjectImpl} */
final class ProjectImplTest implements EqualsContract<ProjectImpl> {

  private ContrastSDK contrast;

  @BeforeEach
  void before() {
    contrast = new Builder("user", "service-key", "api-key").build();
  }

  @Override
  public ProjectImpl createValue() {
    return new ProjectImpl(contrast)
        .setId("fake-project-id")
        .setOrganizationId("fake-organization-id")
        .setName("spring-test-application")
        .setArchived(false)
        .setLanguage("JAVA")
        .setCritical(1)
        .setHigh(2)
        .setMedium(3)
        .setLow(4)
        .setNote(5)
        .setCompletedScans(6)
        .setLastScanTime(LAST_SCAN_TIME_EXAMPLE)
        .setLastScanId("scan-id")
        .setIncludeNamespaceFilters(Collections.singletonList("com.example"))
        .setExcludeNamespaceFilters(Collections.singletonList("org.apache"));
  }

  @Override
  public ProjectImpl createNotEqualValue() {
    return new ProjectImpl(contrast);
  }

  private static final Instant LAST_SCAN_TIME_EXAMPLE =
      OffsetDateTime.of(1955, 11, 12, 22, 4, 0, 0, ZoneOffset.UTC).toInstant();
}
