package com.contrastsecurity.sdk.scan;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

/** Custom assertions for {@link CodeArtifact} */
final class CodeArtifactAssert extends AbstractAssert<CodeArtifactAssert, CodeArtifact> {

  /**
   * @param codeArtifact object to make assertions on
   * @return new {@link CodeArtifactAssert}
   */
  static CodeArtifactAssert assertThat(final CodeArtifact codeArtifact) {
    return new CodeArtifactAssert(codeArtifact);
  }

  private CodeArtifactAssert(final CodeArtifact CodeArtifact) {
    super(CodeArtifact, CodeArtifactAssert.class);
  }

  /**
   * Verifies that this code artifact has the same values as its internal representation.
   *
   * @param inner internal representation of a CodeArtifact
   * @return this
   */
  public CodeArtifactAssert hasSameValuesAsInner(final CodeArtifactInner inner) {
    Assertions.assertThat(actual.id()).isEqualTo(inner.id());
    Assertions.assertThat(actual.projectId()).isEqualTo(inner.projectId());
    Assertions.assertThat(actual.organizationId()).isEqualTo(inner.organizationId());
    Assertions.assertThat(actual.filename()).isEqualTo(inner.filename());
    Assertions.assertThat(actual.createdTime()).isEqualTo(inner.createdTime());
    return this;
  }
}
