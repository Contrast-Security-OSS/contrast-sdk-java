package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.TestDataConstants;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.sdk.internal.GsonFactory;
import com.google.gson.Gson;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;

/** Unit tests for {@link ProjectImpl} */
final class ProjectImplTest implements EqualsContract<ProjectImpl> {

  private ContrastSDK contrast;
  private Gson gson;

  @BeforeEach
  void before() {
    contrast = new ContrastSDK.Builder("user", "service-key", "api-key").build();
    gson = GsonFactory.create();
  }

  @Override
  public ProjectImpl createValue() {
    final ProjectImpl.Value value = builder().build();
    return new ProjectImpl(contrast, gson, value);
  }

  @Override
  public ProjectImpl createNotEqualValue() {
    final ProjectImpl.Value value = builder().id("other-projet-id").build();
    return new ProjectImpl(contrast, gson, value);
  }

  private static ProjectImpl.Value.Builder builder() {
    return ProjectImpl.Value.builder()
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
