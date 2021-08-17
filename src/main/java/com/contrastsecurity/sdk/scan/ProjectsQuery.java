package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.sdk.internal.Nullable;
import com.google.auto.value.AutoValue;

/** Value type that describes a projects resource collection query predicate. */
@AutoValue
abstract class ProjectsQuery {

  /** @return new {@link Builder} */
  static Builder builder() {
    return new AutoValue_ProjectsQuery.Builder();
  }

  /** @return project name, or {@code null} to accept API default behavior. */
  @Nullable
  abstract String name();

  /**
   * @return true if the query will include archived projects, or {@code null} to accept API default
   *     behavior.
   */
  @Nullable
  abstract Boolean archived();

  /**
   * @return true if the projects resources should use exact string matching on the project name
   *     instead of a "contains" comparison, or {@code null} to accept the API default behavior.
   */
  @Nullable
  abstract Boolean unique();

  /** Builder for {@link ProjectsQuery}. */
  @AutoValue.Builder
  abstract static class Builder {

    abstract Builder name(String value);

    abstract Builder archived(Boolean value);

    abstract Builder unique(Boolean value);

    /** @return new {@code ProjectsQuery} */
    abstract ProjectsQuery build();
  }
}
