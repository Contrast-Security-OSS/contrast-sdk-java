package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.TestDataConstants;

/** Unit tests for {@link CodeArtifactImpl}. */
final class CodeArtifactImplTest implements EqualsContract<CodeArtifactImpl> {

  @Override
  public CodeArtifactImpl createValue() {
    return builder().id("code-artifact-id").build();
  }

  @Override
  public CodeArtifactImpl createNotEqualValue() {
    return builder().id("other-id").build();
  }

  private static CodeArtifactImpl.Builder builder() {
    return CodeArtifactImpl.builder()
        .projectId("project-id")
        .organizationId("organization-id")
        .filename("spring-test-application.jar")
        .createdTime(TestDataConstants.TIMESTAMP_EXAMPLE);
  }
}
