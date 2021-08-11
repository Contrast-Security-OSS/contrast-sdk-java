package com.contrastsecurity;

import static org.assertj.core.api.Assertions.assertThat;

import com.contrastsecurity.exceptions.QueryException;
import com.contrastsecurity.http.ApplicationFilterForm;
import com.contrastsecurity.http.ApplicationFilterForm.ApplicationExpandValues;
import com.contrastsecurity.http.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QueryTest {
  Query<?> query;

  @BeforeEach
  public void before() {
    query = new Query<>();
  }

  @Test
  @DisplayName("Test toQuery only returns url parameters with set parameters")
  public void testBasicQuery() throws QueryException {
    query.setLimit(10).setOffset(10);

    String queryString = query.toQuery();

    assertThat(queryString).contains("limit");
    assertThat(queryString).contains("offset");
    assertThat(queryString).doesNotContain("sort");
    assertThat(queryString).doesNotContain("startDate");
  }

  @Test
  @DisplayName("Test toQuery with expand set returns the expanded list")
  public void testQueryWithExpand() throws QueryException {
    EnumSet<ApplicationFilterForm.ApplicationExpandValues> expand =
        EnumSet.of(ApplicationExpandValues.SCORES, ApplicationExpandValues.LICENSE);

    query.setExpand(expand);

    String queryString = query.toQuery();

    String expandValues = queryString.split("=")[1];
    ArrayList<String> values = new ArrayList<>(Arrays.asList(expandValues.split(",")));

    assertThat(queryString).isNotEmpty();
    assertThat(queryString).contains("expand");

    assertThat(values).contains("scores");
    assertThat(values).contains("license");

    assertThat(values).doesNotContain("apps");
    assertThat(values).doesNotContain("test");
    assertThat(values).doesNotContain("libs");
  }
}
