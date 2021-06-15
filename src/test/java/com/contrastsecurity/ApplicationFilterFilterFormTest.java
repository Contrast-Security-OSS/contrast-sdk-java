package com.contrastsecurity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.contrastsecurity.http.ApplicationFilterForm;
import com.contrastsecurity.http.FilterForm;
import com.contrastsecurity.http.RuleSeverity;
import com.contrastsecurity.http.ServerEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import org.junit.Before;
import org.junit.Test;

public class ApplicationFilterFilterFormTest {

  ApplicationFilterForm filterForm;

  @Before
  public void setUp() {
    filterForm = new ApplicationFilterForm();
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

  @Test
  public void testValuesForm() {
    filterForm.setEnvironment(EnumSet.of(ServerEnvironment.DEVELOPMENT));

    filterForm.setFilterAppCode("test_app_code");
    filterForm.setFilterCompliance(Arrays.asList("owasp"));
    filterForm.setFilterLanguages(Arrays.asList("Java"));
    filterForm.setFilterServers(Arrays.asList("abcdefg"));
    filterForm.setFilterTags(Arrays.asList("tag1"));
    filterForm.setFilterTechs(Arrays.asList("MySQL"));
    filterForm.setFilterText("test");
    filterForm.setFilterVulnSeverities(EnumSet.of(RuleSeverity.CRITICAL));
    filterForm.setEnvironment(EnumSet.of(ServerEnvironment.DEVELOPMENT));
    filterForm.setIncludeArchived(false);
    filterForm.setIncludeMerged(false);
    filterForm.setIncludeOnlyLicensed(true);

    filterForm.setQuickFilter(ApplicationFilterForm.ApplicationQuickFilterType.ALL);

    String qs = filterForm.toString();

    assertFalse(qs.isEmpty());

    assertTrue(qs.contains("filterAppCode"));
    assertTrue(qs.contains("filterCompliance"));
    assertTrue(qs.contains("filterLanguages"));
    assertTrue(qs.contains("filterServers"));
    assertTrue(qs.contains("filterTags"));
    assertTrue(qs.contains("filterTechs"));
    assertTrue(qs.contains("filterText"));
    assertTrue(qs.contains("filterVulnSeverities"));
    assertTrue(qs.contains("environment"));
    assertTrue(qs.contains("quickFilter"));

    assertTrue(qs.contains("includeArchived"));
    assertTrue(qs.contains("includeMerged"));
    assertTrue(qs.contains("includeOnlyLicensed"));
  }

  @Test
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
    assertFalse(values.contains("apps"));
    assertFalse(values.contains("test"));
    assertFalse(values.contains("libs"));
  }
}
