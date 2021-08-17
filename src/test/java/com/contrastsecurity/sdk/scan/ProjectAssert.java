package com.contrastsecurity.sdk.scan;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

/** Custom assertions for {@link Project} */
final class ProjectAssert extends AbstractAssert<ProjectAssert, Project> {

  /**
   * @param project object to make assertions on
   * @return new {@link ProjectAssert}
   */
  static ProjectAssert assertThat(final Project project) {
    return new ProjectAssert(project);
  }

  private ProjectAssert(final Project project) {
    super(project, ProjectAssert.class);
  }

  /**
   * Verifies that this project has the same values as its internal representation.
   *
   * @param inner internal representation of a scan
   * @return this
   */
  public ProjectAssert hasSameValuesAsInner(final ProjectInner inner) {
    Assertions.assertThat(actual.id()).isEqualTo(inner.id());
    Assertions.assertThat(actual.organizationId()).isEqualTo(inner.organizationId());
    Assertions.assertThat(actual.language()).isEqualTo(inner.language());
    Assertions.assertThat(actual.name()).isEqualTo(inner.name());
    Assertions.assertThat(actual.lastScanId()).isEqualTo(inner.lastScanId());
    Assertions.assertThat(actual.lastScanTime()).isEqualTo(inner.lastScanTime());
    Assertions.assertThat(actual.archived()).isEqualTo(inner.archived());
    Assertions.assertThat(actual.critical()).isEqualTo(inner.critical());
    Assertions.assertThat(actual.high()).isEqualTo(inner.high());
    Assertions.assertThat(actual.medium()).isEqualTo(inner.medium());
    Assertions.assertThat(actual.low()).isEqualTo(inner.low());
    Assertions.assertThat(actual.note()).isEqualTo(inner.note());
    Assertions.assertThat(actual.completedScans()).isEqualTo(inner.completedScans());
    Assertions.assertThat(actual.includeNamespaceFilters())
        .isEqualTo(inner.includeNamespaceFilters());
    Assertions.assertThat(actual.excludeNamespaceFilters())
        .isEqualTo(inner.excludeNamespaceFilters());
    return this;
  }
}
