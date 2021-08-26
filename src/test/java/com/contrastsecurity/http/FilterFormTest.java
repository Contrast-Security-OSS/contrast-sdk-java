package com.contrastsecurity.http;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class FilterFormTest {

  FilterForm filterForm;

  @BeforeEach
  public void setUp() {
    filterForm = new FilterForm();
  }

  @Test
  public void testBasicForm() {
    filterForm.setLimit(10);
    filterForm.setOffset(10);
    filterForm.setStatus("Reported");

    String qs = filterForm.toString();

    assertThat(qs.isEmpty()).isFalse();

    assertThat(qs.contains("limit")).isTrue();
    assertThat(qs.contains("offset")).isTrue();
    assertThat(qs.contains("status")).isTrue();
    assertThat(qs.contains("sort")).isFalse();
    assertThat(qs.contains("startDate")).isFalse();
  }

  @Test
  public void testExpand() {
    EnumSet<FilterForm.ApplicationExpandValues> expand =
        EnumSet.of(
            FilterForm.ApplicationExpandValues.SCORES, FilterForm.ApplicationExpandValues.LICENSE);

    filterForm.setExpand(expand);

    String qs = filterForm.toString();

    assertThat(qs.isEmpty()).isFalse();

    assertThat(qs.contains("expand")).isTrue();

    String expandValues = qs.split("=")[1];
    ArrayList<String> values = new ArrayList<>(Arrays.asList(expandValues.split(",")));

    assertThat(values.contains("scores")).isTrue();
    assertThat(values.contains("license")).isTrue();

    assertThat(values.contains("apps")).isFalse();
    assertThat(values.contains("test")).isFalse();
    assertThat(values.contains("libs")).isFalse();
  }
}
