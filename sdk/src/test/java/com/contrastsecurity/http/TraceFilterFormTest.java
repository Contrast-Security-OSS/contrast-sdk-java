package com.contrastsecurity.http;
/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2024 Contrast Security, Inc.
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

import java.io.UnsupportedEncodingException;
import java.util.EnumSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TraceFilterFormTest {
  TraceFilterForm form;

  @BeforeEach
  public void setUp() {
    form = new TraceFilterForm();
  }

  @Test
  public void
      toQuery_should_have_severities_and_environments_when_severities_and_environments_are_filled()
          throws UnsupportedEncodingException {
    final String expected =
        "?severities=HIGH,CRITICAL&environments=DEVELOPMENT,PRODUCTION&tracked=true&untracked=false";
    form.setSeverities(EnumSet.of(RuleSeverity.HIGH, RuleSeverity.CRITICAL));
    form.setEnvironments(EnumSet.of(ServerEnvironment.DEVELOPMENT, ServerEnvironment.PRODUCTION));
    final String actual = form.toQuery();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void toQuery_should_not_have_environments_when_environments_is_not_filled()
      throws UnsupportedEncodingException {
    final String expected = "?severities=HIGH,CRITICAL&tracked=true&untracked=false";
    form.setSeverities(EnumSet.of(RuleSeverity.HIGH, RuleSeverity.CRITICAL));
    final String actual = form.toQuery();

    assertThat(actual).isEqualTo(expected);
  }
}
