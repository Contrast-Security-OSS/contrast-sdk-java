package com.contrastsecurity;


import com.contrastsecurity.http.ApplicationFilterForm;
import com.contrastsecurity.http.FilterForm;
import com.contrastsecurity.http.RuleSeverity;
import com.contrastsecurity.http.ServerEnvironment;
import com.sun.tools.javac.util.List;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        filterForm.setFilterCompliance(List.of("owasp"));
        filterForm.setFilterLanguages(List.of("Java"));
        filterForm.setFilterServers(List.of("abcdefg"));
        filterForm.setFilterTags(List.of("tag1"));
        filterForm.setFilterTechs(List.of("MySQL"));
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
        EnumSet<FilterForm.ApplicationExpandValues> expand = EnumSet.of(FilterForm.ApplicationExpandValues.SCORES, FilterForm.ApplicationExpandValues.LICENSE);

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
