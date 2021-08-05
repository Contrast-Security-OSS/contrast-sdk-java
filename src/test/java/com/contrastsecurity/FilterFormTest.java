package com.contrastsecurity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    assertFalse(qs.isEmpty());

    assertTrue(qs.contains("limit"));
    assertTrue(qs.contains("offset"));
    assertTrue(qs.contains("status"));
    assertFalse(qs.contains("sort"));
    assertFalse(qs.contains("startDate"));
  }

  @org.junit.jupiter.api.Test
  public void testExpand() {
    EnumSet<FilterForm.ApplicationExpandValues> expand =
        EnumSet.of(
            FilterForm.ApplicationExpandValues.SCORES, FilterForm.ApplicationExpandValues.LICENSE);

    filterForm.setExpand(expand);

    String qs = filterForm.toString();

    assertFalse(qs.isEmpty());

    assertTrue(qs.contains("expand"));

    String expandValues = qs.split("=")[1];
    ArrayList<String> values = new ArrayList<>(Arrays.asList(expandValues.split(",")));

    assertTrue(values.contains("scores"));
    assertTrue(values.contains("license"));

    assertFalse(values.contains("apps"));
    assertFalse(values.contains("test"));
    assertFalse(values.contains("libs"));
  }
}
