package com.contrastsecurity.sdk.scan;

import static com.contrastsecurity.sdk.scan.ProjectAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.contrastsecurity.EqualsAndHashcodeContract;
import com.contrastsecurity.TestDataConstants;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ProjectsImpl} and {@link ProjectImpl}. Tested together because these
 * classes are tightly coupled.
 */
final class ProjectsImplTest implements EqualsAndHashcodeContract<ProjectImpl> {

  private CodeArtifacts.Factory codeArtifactsFactory;
  private Scans.Factory scansFactory;

  @BeforeEach
  void before() {
    codeArtifactsFactory = mock(CodeArtifacts.Factory.class);
    scansFactory = mock(Scans.Factory.class);
  }

  /** Test for {@link Projects#define()} */
  @Test
  void define_project() throws IOException {
    // GIVEN project client returns a new project when given expected project create request
    final ProjectClient client = mock(ProjectClient.class);
    final ProjectCreate create =
        ProjectCreate.builder()
            .name("spring-test-application")
            .language("JAVA")
            .includeNamespaceFilters(Collections.singletonList("com.acme"))
            .excludeNamespaceFilters(Collections.singletonList("org.apache"))
            .build();
    final ProjectInner inner = builder().build();
    when(client.create(create)).thenReturn(inner);

    // WHEN define new project
    final ProjectsImpl projects = new ProjectsImpl(codeArtifactsFactory, scansFactory, client);
    final Project project =
        projects
            .define()
            .withName("spring-test-application")
            .withLanguage("JAVA")
            .withIncludeNamespaceFilters(Collections.singletonList("com.acme"))
            .withExcludeNamespaceFilters(Collections.singletonList("org.apache"))
            .create();

    // THEN returns new project with the same values as the stubbed ProjectInner
    assertThat(project).hasSameValuesAsInner(inner);
  }

  @Test
  void find_by_name() throws IOException {
    // GIVEN stubbed project client returns a new project
    final ProjectClient client = mock(ProjectClient.class);
    final ProjectInner inner = builder().build();
    when(client.findByName(inner.name())).thenReturn(Optional.of(inner));

    // WHEN find project by name
    final Projects projects = new ProjectsImpl(codeArtifactsFactory, scansFactory, client);
    final Optional<Project> optional = projects.findByName(inner.name());

    // THEN returns expected project
    assertThat(optional)
        .hasValueSatisfying(project -> assertThat(project).hasSameValuesAsInner(inner));
  }

  /** Test for {@link Project#codeArtifacts()} */
  @Test
  void code_artifacts_traversal() {
    // GIVEN a code artifacts factory that creates a stubbed code artifacts for this project's ID
    final ProjectImpl project = createValue();
    final CodeArtifacts expected = mock(CodeArtifacts.class);
    when(codeArtifactsFactory.create(project.id())).thenReturn(expected);

    // WHEN traverse to code artifacts resource collection
    final CodeArtifacts codeArtifacts = project.codeArtifacts();

    // THEN creates new code artifacts resource collection with this project's ID
    assertThat(codeArtifacts).isSameAs(expected);
  }

  /** Test for {@link Project#scans()} */
  @Test
  void scans_traversal() {
    // GIVEN a scans factory that creates a stubbed scans for this project's ID
    final ProjectImpl project = createValue();
    final Scans expected = mock(Scans.class);
    when(scansFactory.create(project.id())).thenReturn(expected);

    // WHEN traverse to scans resource collection
    final Scans scans = project.scans();

    // THEN creates new scans resource collection with this project's ID
    assertThat(scans).isSameAs(expected);
  }

  @Override
  public ProjectImpl createValue() {
    final ProjectInner inner = builder().build();
    return new ProjectImpl(codeArtifactsFactory, scansFactory, inner);
  }

  @Override
  public ProjectImpl createNotEqualValue() {
    final ProjectInner inner = builder().id("other-project-id").build();
    return new ProjectImpl(codeArtifactsFactory, scansFactory, inner);
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
