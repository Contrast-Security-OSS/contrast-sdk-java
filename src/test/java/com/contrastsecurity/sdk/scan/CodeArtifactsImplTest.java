package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 Contrast Security, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
  void upload_with_metadata(@TempDir final Path tmp) throws IOException {
    // GIVEN stubbed code artifacts client
    final CodeArtifactClient client = mock(CodeArtifactClient.class);
    final CodeArtifactInner inner = builder().metadata("prescan.json").build();
    final Path file = tmp.resolve(inner.filename());
    final Path meta = tmp.resolve(inner.metadata());
    when(client.upload(inner.projectId(), file, meta)).thenReturn(inner);

    // WHEN upload file,meta
    final CodeArtifacts codeArtifacts = new CodeArtifactsImpl(client, inner.projectId());
    final CodeArtifact codeArtifact = codeArtifacts.upload(file, meta);

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
  void upload_custom_metaname(@TempDir final Path tmp) throws IOException {
    // GIVEN stubbed code artifacts client
    final CodeArtifactClient client = mock(CodeArtifactClient.class);
    final CodeArtifactInner inner = builder().metadata("prescan.json").build();
    final Path file = tmp.resolve(inner.filename());
    final Path meta = tmp.resolve("other-prescan.json");
    when(client.upload(inner.projectId(), file, meta)).thenReturn(inner);

    // WHEN upload file,meta
    final CodeArtifacts codeArtifacts = new CodeArtifactsImpl(client, inner.projectId());
    final CodeArtifact codeArtifact =
        codeArtifacts.upload(file, inner.filename(), meta, inner.metadata());

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
