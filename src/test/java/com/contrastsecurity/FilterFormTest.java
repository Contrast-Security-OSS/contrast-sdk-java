package com.contrastsecurity;


import com.contrastsecurity.http.FilterForm;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FilterFormTest {

    FilterForm filterForm;

    @Before
    public void setUp() {
        filterForm = new FilterForm();
    }

    @Test
    public void testBasicForm() {
        filterForm.setLimit(10);
        filterForm.setOffset(10);
        filterForm.setStatus("Reported");

        String qs = filterForm.toString();

        assertTrue(!qs.isEmpty());

        assertTrue(qs.contains("limit"));
        assertTrue(qs.contains("offset"));
        assertTrue(qs.contains("status"));
        assertFalse(qs.contains("sort"));
        assertFalse(qs.contains("startDate"));
    }

    @Test
    public void testExpand() {
        EnumSet<FilterForm.ExpandValues> expand = EnumSet.of(FilterForm.ExpandValues.CVE, FilterForm.ExpandValues.MANIFEST, FilterForm.ExpandValues.APPS);

        filterForm.setExpand(expand);

        String qs = filterForm.toString();

        assertTrue(!qs.isEmpty());

        assertTrue(qs.contains("expand"));

        String expandValues = qs.split("=")[1];
        ArrayList<String> values = new ArrayList<>(Arrays.asList(expandValues.split(",")));

        assertTrue(values.contains("cve"));
        assertTrue(values.contains("manifest"));
        assertTrue(values.contains("apps"));

        assertFalse(values.contains("test"));
        assertFalse(values.contains("libs"));
    }
}
