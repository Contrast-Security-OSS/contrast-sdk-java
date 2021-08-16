package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.TestDataConstants;
import java.util.Collections;

/** Unit tests for {@link ProjectImpl} */
final class ProjectImplTest implements EqualsContract<ProjectImpl> {

  @Override
  public ProjectImpl createValue() {
    final ProjectInner inner = builder().build();
    return new ProjectImpl(inner);
  }

  @Override
  public ProjectImpl createNotEqualValue() {
    final ProjectInner inner = builder().id("other-project-id").build();
    return new ProjectImpl(inner);
  }

  private static ProjectInner.Builder builder() {
    return ProjectInner.builder()
        .id("fake-project-id")
        .organizationId("fake-organization-id")
        .name("spring-test-application")
        .archived(false)
        .language("JAVA")
        .critical(1)
        .high(2)
        .medium(3)
        .low(4)
        .note(5)
        .completedScans(6)
        .lastScanTime(TestDataConstants.TIMESTAMP_EXAMPLE)
        .lastScanId("scan-id")
        .includeNamespaceFilters(Collections.singletonList("com.example"))
        .excludeNamespaceFilters(Collections.singletonList("org.apache"));
  }
}
