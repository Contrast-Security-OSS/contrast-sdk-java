package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.EqualsContract;
import com.contrastsecurity.TestDataConstants;

/** Unit tests for {@link CodeArtifactImpl}. */
final class CodeArtifactImplTest implements EqualsContract<CodeArtifactImpl> {

  @Override
  public CodeArtifactImpl createValue() {
    final CodeArtifactInner inner = builder().id("code-artifact-id").build();
    return new CodeArtifactImpl(inner);
  }

  @Override
  public CodeArtifactImpl createNotEqualValue() {
    final CodeArtifactInner inner = builder().id("other-id").build();
    return new CodeArtifactImpl(inner);
  }

  private static CodeArtifactInner.Builder builder() {
    return CodeArtifactInner.builder()
        .projectId("project-id")
        .organizationId("organization-id")
        .filename("spring-test-application.jar")
        .createdTime(TestDataConstants.TIMESTAMP_EXAMPLE);
  }
}
