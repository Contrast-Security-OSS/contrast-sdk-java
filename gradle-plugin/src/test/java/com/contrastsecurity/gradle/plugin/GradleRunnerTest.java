package com.contrastsecurity.gradle.plugin;

import java.io.File;
import org.junit.jupiter.api.io.TempDir;

/** Boilerplate for tests using the {@link org.gradle.testkit.runner.GradleRunner} */
public abstract class GradleRunnerTest {

  @TempDir public File projectDir;

  public File getBuildFile() {
    return new File(projectDir, "build.gradle");
  }

  public File getSettingsFile() {
    return new File(projectDir, "settings.gradle");
  }
}
