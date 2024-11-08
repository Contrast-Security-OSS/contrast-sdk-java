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

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class ContrastInstallAgentMojoTest {

  private ContrastInstallAgentMojo installMojo;
  private Date now;

  @Rule public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

  @Before
  public void setUp() {
    installMojo = new ContrastInstallAgentMojo();
    installMojo.setAppName("caddyshack");
    installMojo.setServerName("Bushwood");
    installMojo.contrastAgentLocation = "/usr/local/bin/contrast.jar";

    now = new Date();
    environmentVariables.clear(
        "TRAVIS_BUILD_NUMBER",
        "CIRCLE_BUILD_NUM",
        "GIT_BRANCH",
        "GIT_COMMITTER_NAME",
        "GIT_COMMIT",
        "GIT_URL",
        "BUILD_NUMBER");
  }

  @Test
  public void testGenerateAppVersion() {
    installMojo.appVersion = "mycustomversion";
    AbstractAssessMojo.computedAppVersion = null;
    assertEquals("mycustomversion", installMojo.computeAppVersion(now));
  }

  @Test
  public void testGenerateAppVersionNoAppVersion() {
    installMojo.appVersion = null;
    AbstractAssessMojo.computedAppVersion = null;
    String expectedVersion = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
    assertEquals("caddyshack-" + expectedVersion, installMojo.computeAppVersion(now));
    assertEquals("caddyshack-" + expectedVersion, installMojo.computeAppVersion(now));
  }

  @Test
  public void testGenerateAppVersionTravis() {
    installMojo.appVersion = null;
    AbstractAssessMojo.computedAppVersion = null;
    environmentVariables.set("TRAVIS_BUILD_NUMBER", "19");
    assertEquals("caddyshack-19", installMojo.computeAppVersion(now));
    assertEquals("caddyshack-19", installMojo.computeAppVersion(now));
  }

  @Test
  public void testGenerateAppVersionCircle() {
    installMojo.appVersion = null;
    AbstractAssessMojo.computedAppVersion = null;
    environmentVariables.set("TRAVIS_BUILD_NUMBER", "circle");
    assertEquals("caddyshack-circle", installMojo.computeAppVersion(now));
    assertEquals("caddyshack-circle", installMojo.computeAppVersion(now));
  }

  @Test
  public void testGenerateAppVersionAppId() {
    String appName = "WebGoat";
    String appId = "12345";
    String travisBuildNumber = "travis";

    installMojo.appVersion = null;
    AbstractAssessMojo.computedAppVersion = null;
    environmentVariables.set("TRAVIS_BUILD_NUMBER", travisBuildNumber);
    installMojo.setAppId(appId);
    installMojo.applicationName = appName;

    assertEquals(appName + "-" + travisBuildNumber, installMojo.computeAppVersion(now));
  }

  @Test
  public void testGenerateSessionMetadata() {
    environmentVariables.set("GIT_BRANCH", "develop");
    assertEquals("branchName=develop", installMojo.computeSessionMetadata());

    environmentVariables.set("GIT_COMMITTER_NAME", "boh");
    assertEquals("branchName=develop,committer=boh", installMojo.computeSessionMetadata());

    environmentVariables.set("GIT_COMMIT", "deadbeef");
    assertEquals(
        "branchName=develop,commitHash=deadbeef,committer=boh",
        installMojo.computeSessionMetadata());

    environmentVariables.set(
        "GIT_URL", "https://github.com/Contrast-Security-OSS/contrast-maven-plugin.git");
    assertEquals(
        "branchName=develop,commitHash=deadbeef,committer=boh,repository=https://github.com/Contrast-Security-OSS/contrast-maven-plugin.git",
        installMojo.computeSessionMetadata());

    environmentVariables.set("BUILD_NUMBER", "123");
    assertEquals(
        "buildNumber=123,branchName=develop,commitHash=deadbeef,committer=boh,repository=https://github.com/Contrast-Security-OSS/contrast-maven-plugin.git",
        installMojo.computeSessionMetadata());

    environmentVariables.clear("BUILD_NUMBER");
    environmentVariables.set("CIRCLE_BUILD_NUM", "12345");
    assertEquals(
        "buildNumber=12345,branchName=develop,commitHash=deadbeef,committer=boh,repository=https://github.com/Contrast-Security-OSS/contrast-maven-plugin.git",
        installMojo.computeSessionMetadata());

    environmentVariables.clear("CIRCLE_BUILD_NUM");
    environmentVariables.set("TRAVIS_BUILD_NUMBER", "54321");
    assertEquals(
        "branchName=develop,commitHash=deadbeef,committer=boh,repository=https://github.com/Contrast-Security-OSS/contrast-maven-plugin.git,buildNumber=54321",
        installMojo.computeSessionMetadata());
  }

  @Test
  public void testBuildArgLine() {
    AbstractAssessMojo.computedAppVersion = "caddyshack-2";
    String currentArgLine = "";
    String expectedArgLine =
        "-javaagent:/usr/local/bin/contrast.jar -Dcontrast.server=Bushwood -Dcontrast.env=qa -Dcontrast.override.appversion=caddyshack-2 -Dcontrast.reporting.period=200 -Dcontrast.override.appname=caddyshack";
    assertEquals(expectedArgLine, installMojo.buildArgLine(currentArgLine));

    installMojo.setServerName(null); // no server name
    currentArgLine = "";
    expectedArgLine =
        "-javaagent:/usr/local/bin/contrast.jar -Dcontrast.env=qa -Dcontrast.override.appversion=caddyshack-2 -Dcontrast.reporting.period=200 -Dcontrast.override.appname=caddyshack";
    assertEquals(expectedArgLine, installMojo.buildArgLine(currentArgLine));

    installMojo.setServerName("Bushwood");
    installMojo.serverPath = "/home/tomcat/app/";
    currentArgLine = "";
    expectedArgLine =
        "-javaagent:/usr/local/bin/contrast.jar -Dcontrast.server=Bushwood -Dcontrast.env=qa -Dcontrast.override.appversion=caddyshack-2 -Dcontrast.reporting.period=200 -Dcontrast.override.appname=caddyshack -Dcontrast.path=/home/tomcat/app/";
    assertEquals(expectedArgLine, installMojo.buildArgLine(currentArgLine));

    installMojo.standalone = true;
    expectedArgLine =
        "-javaagent:/usr/local/bin/contrast.jar -Dcontrast.server=Bushwood -Dcontrast.env=qa -Dcontrast.override.appversion=caddyshack-2 -Dcontrast.reporting.period=200 -Dcontrast.standalone.appname=caddyshack -Dcontrast.path=/home/tomcat/app/";
    assertEquals(expectedArgLine, installMojo.buildArgLine(currentArgLine));

    environmentVariables.set("BUILD_NUMBER", "123");
    environmentVariables.set("GIT_COMMITTER_NAME", "boh");
    expectedArgLine =
        "-javaagent:/usr/local/bin/contrast.jar -Dcontrast.server=Bushwood -Dcontrast.env=qa -Dcontrast.override.appversion=caddyshack-2 -Dcontrast.reporting.period=200 -Dcontrast.application.session_metadata='buildNumber=123,committer=boh' -Dcontrast.standalone.appname=caddyshack -Dcontrast.path=/home/tomcat/app/";
    assertEquals(expectedArgLine, installMojo.buildArgLine(currentArgLine));
  }

  @Test
  public void testBuildArgNull() {
    AbstractAssessMojo.computedAppVersion = "caddyshack-2";
    String currentArgLine = null;
    String expectedArgLine =
        "-javaagent:/usr/local/bin/contrast.jar -Dcontrast.server=Bushwood -Dcontrast.env=qa -Dcontrast.override.appversion=caddyshack-2 -Dcontrast.reporting.period=200 -Dcontrast.override.appname=caddyshack";
    assertEquals(expectedArgLine, installMojo.buildArgLine(currentArgLine));
  }

  @Test
  public void testBuildArgLineAppend() {
    AbstractAssessMojo.computedAppVersion = "caddyshack-2";
    String currentArgLine = "-Xmx1024m";
    String expectedArgLine =
        "-Xmx1024m -javaagent:/usr/local/bin/contrast.jar -Dcontrast.server=Bushwood -Dcontrast.env=qa -Dcontrast.override.appversion=caddyshack-2 -Dcontrast.reporting.period=200 -Dcontrast.override.appname=caddyshack";
    assertEquals(expectedArgLine, installMojo.buildArgLine(currentArgLine));
  }

  @Test
  public void testBuildArgLineSkip() {
    installMojo.skipArgLine = true;
    String currentArgLine = "-Xmx1024m";
    String expectedArgLine = "-Xmx1024m";
    assertEquals(expectedArgLine, installMojo.buildArgLine(currentArgLine));
  }
}
