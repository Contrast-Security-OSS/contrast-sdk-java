package com.contrastsecurity.maven.plugin;

/*-
 * #%L
 * Contrast Maven Plugin
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.contrastsecurity.http.RuleSeverity;
import com.contrastsecurity.http.TraceFilterForm;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ContrastVerifyMojoTest {

  private ContrastVerifyMojo verifyContrastMavenPluginMojo;

  @Before
  public void setUp() {
    verifyContrastMavenPluginMojo = new ContrastVerifyMojo();
    verifyContrastMavenPluginMojo.minSeverity = "Medium";
  }

  @Test
  public void testGetTraceFilterFormServerIdsNull() {
    TraceFilterForm traceFilterForm = verifyContrastMavenPluginMojo.getTraceFilterForm(null);
    assertNull(traceFilterForm.getServerIds());
  }

  @Test
  public void testGetTraceFilterForm() {
    List<Long> serverIds = new ArrayList<>();
    long server1 = 123L;
    long server2 = 456L;
    long server3 = 789L;

    serverIds.add(server1);
    serverIds.add(server2);
    serverIds.add(server3);

    TraceFilterForm traceFilterForm = verifyContrastMavenPluginMojo.getTraceFilterForm(serverIds);
    assertNotNull(traceFilterForm.getServerIds());
    assertEquals(3, traceFilterForm.getServerIds().size());
    assertEquals((Long) server1, traceFilterForm.getServerIds().get(0));
    assertEquals((Long) server2, traceFilterForm.getServerIds().get(1));
    assertEquals((Long) server3, traceFilterForm.getServerIds().get(2));
  }

  @Test
  public void testGetTraceFilterFormSeverities() {
    verifyContrastMavenPluginMojo.minSeverity = "Note";
    TraceFilterForm traceFilterForm = verifyContrastMavenPluginMojo.getTraceFilterForm(null);

    assertEquals(5, traceFilterForm.getSeverities().size());
    assertTrue(traceFilterForm.getSeverities().contains(RuleSeverity.NOTE));
    assertTrue(traceFilterForm.getSeverities().contains(RuleSeverity.LOW));
    assertTrue(traceFilterForm.getSeverities().contains(RuleSeverity.MEDIUM));
    assertTrue(traceFilterForm.getSeverities().contains(RuleSeverity.HIGH));
    assertTrue(traceFilterForm.getSeverities().contains(RuleSeverity.CRITICAL));
  }

  @Test
  public void testGetTraceFilterFormAppVersionTags() {
    String appVersion = "WebGoat-1";

    AbstractAssessMojo.computedAppVersion = appVersion;
    TraceFilterForm traceFilterForm = verifyContrastMavenPluginMojo.getTraceFilterForm(null);

    assertEquals(1, traceFilterForm.getAppVersionTags().size());
    assertEquals(appVersion, traceFilterForm.getAppVersionTags().get(0));
  }
}
