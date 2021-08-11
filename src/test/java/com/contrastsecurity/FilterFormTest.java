package com.contrastsecurity;

import static org.assertj.core.api.Assertions.assertThat;

import com.contrastsecurity.http.FilterForm;
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
