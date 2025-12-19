package com.contrastsecurity.http;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2025 Contrast Security, Inc.
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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class LibraryFilterFormTest {

  LibraryFilterForm form;

  @BeforeEach
  public void setUp() {
    form = new LibraryFilterForm();
  }

  @Test
  public void toString_should_include_environments_when_set() {
    form.setEnvironments(EnumSet.of(ServerEnvironment.DEVELOPMENT, ServerEnvironment.PRODUCTION));
    String qs = form.toString();

    assertThat(qs).contains("environments=");
    assertThat(qs).contains("DEVELOPMENT");
    assertThat(qs).contains("PRODUCTION");
  }

  @Test
  public void toString_should_not_include_environments_when_empty() {
    String qs = form.toString();

    assertThat(qs).doesNotContain("environments=");
  }

  @Test
  public void toString_should_include_single_environment() {
    form.setEnvironments(EnumSet.of(ServerEnvironment.QA));
    String qs = form.toString();

    assertThat(qs).contains("environments=QA");
  }

  @Test
  public void getEnvironments_should_return_set_environments() {
    EnumSet<ServerEnvironment> expected =
        EnumSet.of(ServerEnvironment.DEVELOPMENT, ServerEnvironment.QA);
    form.setEnvironments(expected);

    assertThat(form.getEnvironments()).isEqualTo(expected);
  }

  @Test
  public void environments_should_be_empty_by_default() {
    assertThat(form.getEnvironments()).isEmpty();
  }

  @Test
  public void toString_should_include_statuses_when_set() {
    form.setStatuses(Arrays.asList("CURRENT", "FLAGGED"));
    String qs = form.toString();

    assertThat(qs).contains("statuses=");
    assertThat(qs).contains("CURRENT");
    assertThat(qs).contains("FLAGGED");
  }

  @Test
  public void toString_should_not_include_statuses_when_empty() {
    String qs = form.toString();

    assertThat(qs).doesNotContain("statuses=");
  }

  @Test
  public void toString_should_include_single_status() {
    form.setStatuses(Arrays.asList("STALE"));
    String qs = form.toString();

    assertThat(qs).contains("statuses=STALE");
  }

  @Test
  public void getStatuses_should_return_set_statuses() {
    List<String> expected = Arrays.asList("CURRENT", "UNKNOWN");
    form.setStatuses(expected);

    assertThat(form.getStatuses()).isEqualTo(expected);
  }

  @Test
  public void statuses_should_be_empty_by_default() {
    assertThat(form.getStatuses()).isEmpty();
  }

  @Test
  public void toString_should_include_severities_when_set() {
    form.setSeverities(Arrays.asList("CRITICAL", "HIGH"));
    String qs = form.toString();

    assertThat(qs).contains("severities=");
    assertThat(qs).contains("CRITICAL");
    assertThat(qs).contains("HIGH");
  }

  @Test
  public void toString_should_not_include_severities_when_empty() {
    String qs = form.toString();

    assertThat(qs).doesNotContain("severities=");
  }

  @Test
  public void toString_should_include_single_severity() {
    form.setSeverities(Arrays.asList("MEDIUM"));
    String qs = form.toString();

    assertThat(qs).contains("severities=MEDIUM");
  }

  @Test
  public void getSeverities_should_return_set_severities() {
    List<String> expected = Arrays.asList("LOW", "NOTE");
    form.setSeverities(expected);

    assertThat(form.getSeverities()).isEqualTo(expected);
  }

  @Test
  public void severities_should_be_empty_by_default() {
    assertThat(form.getSeverities()).isEmpty();
  }
}
