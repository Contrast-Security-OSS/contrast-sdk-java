package com.contrastsecurity.sdk.scan;

import static com.contrastsecurity.sdk.scan.CodeArtifactAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.contrastsecurity.EqualsAndHashcodeContract;
import com.contrastsecurity.TestDataConstants;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link CodeArtifactsImpl} and {@link CodeArtifactImpl}. Tested together because
 * these classes are tightly coupled.
 */
final class CodeArtifactsImplTest implements EqualsAndHashcodeContract<CodeArtifactImpl> {

  @Test
  void upload(@TempDir final Path tmp) throws IOException {
    // GIVEN stubbed code artifacts client
    final CodeArtifactClient client = mock(CodeArtifactClient.class);
    final CodeArtifactInner inner = builder().build();
    final Path file = tmp.resolve(inner.filename());
    when(client.upload(inner.projectId(), file)).thenReturn(inner);

    // WHEN upload file
    final CodeArtifacts codeArtifacts = new CodeArtifactsImpl(client, inner.projectId());
    final CodeArtifact codeArtifact = codeArtifacts.upload(file);

    // THEN returns expected code artifact
    assertThat(codeArtifact).hasSameValuesAsInner(inner);
  }

  @Test
  void upload_custom_filename(@TempDir final Path tmp) throws IOException {
    // GIVEN stubbed code artifacts client
    final CodeArtifactClient client = mock(CodeArtifactClient.class);
    final CodeArtifactInner inner = builder().build();
    final Path file = tmp.resolve("other-file.jar");
    when(client.upload(inner.projectId(), file)).thenReturn(inner);

    // WHEN upload file
    final CodeArtifacts codeArtifacts = new CodeArtifactsImpl(client, inner.projectId());
    final CodeArtifact codeArtifact = codeArtifacts.upload(file, inner.filename());

    // THEN returns expected code artifact
    assertThat(codeArtifact).hasSameValuesAsInner(inner);
  }

  @Test
  void delegates_to_inner() {
    final CodeArtifactInner inner = builder().build();
    final CodeArtifact codeArtifact = new CodeArtifactImpl(inner);

    assertThat(codeArtifact).hasSameValuesAsInner(inner);
  }

  @Override
  public CodeArtifactImpl createValue() {
    final CodeArtifactInner inner = builder().build();
    return new CodeArtifactImpl(inner);
  }

  @Override
  public CodeArtifactImpl createNotEqualValue() {
    final CodeArtifactInner inner = builder().id("other-id").build();
    return new CodeArtifactImpl(inner);
  }

  private static CodeArtifactInner.Builder builder() {
    return CodeArtifactInner.builder()
        .id("code-artifact-id")
        .projectId("project-id")
        .organizationId("organization-id")
        .filename("spring-test-application.jar")
        .createdTime(TestDataConstants.TIMESTAMP_EXAMPLE);
  }
}
