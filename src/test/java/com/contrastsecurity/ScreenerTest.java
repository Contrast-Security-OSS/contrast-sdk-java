package com.contrastsecurity;

import static org.junit.Assert.*;

import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.http.ApplicationFilterForm;
import com.contrastsecurity.http.TraceFilterForm;
import com.contrastsecurity.models.*;
import com.contrastsecurity.sdk.ContrastSDK;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScreenerTest {

  private static ContrastSDK contrastSDK;
  private static final String TEST_PROPERTIES = "test.properties";
  private static Properties properties;

  @BeforeClass
  public static void setUp() throws IOException {
    InputStream propertiesFileInputStream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_PROPERTIES);
    properties = new Properties();
    properties.load(propertiesFileInputStream);

    contrastSDK =
        new ContrastSDK.Builder(
                properties.getProperty("username"),
                properties.getProperty("serviceKey"),
                properties.getProperty("apiKey"))
            .withApiUrl(properties.getProperty("localTeamServerUrl"))
            .build();
  }

  @Test
  public void testDownloadAgent() throws IOException {
    File contrastJar = new File("contrast.jar");

    try {
      FileUtils.writeByteArrayToFile(
          contrastJar, contrastSDK.getAgent(AgentType.JAVA, properties.getProperty("orgId")));
    } catch (IOException | UnauthorizedException e) {
      assertTrue(true);
    }

    assertTrue(contrastJar.exists());
    assertTrue(FileUtils.sizeOf(contrastJar) > 0);
  }

  @Test
  public void testGetProfileOrganizations() throws IOException, UnauthorizedException {
    Organizations organizations = contrastSDK.getProfileOrganizations();

    assertTrue(!organizations.getOrganizations().isEmpty());
  }

  @Test
  public void testGetProfileDefaultOrganization() throws IOException, UnauthorizedException {
    Organizations organizations = contrastSDK.getProfileDefaultOrganizations();

    assertTrue(organizations.getOrganization().getName() != null);
  }

  @Test
  public void testGetApplications() throws IOException, UnauthorizedException {
    String orgId = properties.getProperty("orgId");

    Applications applications = contrastSDK.getApplications(orgId);

    assertTrue(!applications.getApplications().isEmpty());
  }

  @Test
  public void testGetServers() throws IOException, UnauthorizedException {
    String orgId = properties.getProperty("orgId");

    Servers servers = contrastSDK.getServers(orgId, null);

    assertTrue(!servers.getServers().isEmpty());
  }

  @Test
  public void testGetApplicationTraces() throws IOException, UnauthorizedException {
    String orgId = properties.getProperty("orgId");

    Applications applications = contrastSDK.getApplications(orgId);

    assertTrue(!applications.getApplications().isEmpty());

    Application application = applications.getApplications().get(0);

    assertTrue(application.getId() != null);

    TraceFilterForm form = new TraceFilterForm();

    form.setExpand(EnumSet.of(TraceFilterForm.TraceExpandValue.APPLICATION));

    Traces traces = contrastSDK.getTraces(orgId, application.getId(), form);

    assertTrue(!traces.getTraces().isEmpty());
  }

  @Test
  public void testGetFilteredApplication() throws IOException, UnauthorizedException {
    String orgId = properties.getProperty("orgId");

    ApplicationFilterForm form = new ApplicationFilterForm();

    Applications applications = contrastSDK.getFilteredApplications(orgId, form);

    assertFalse(applications.getApplications().isEmpty());

    Application application = applications.getApplications().get(0);

    assertNotNull(application.getId());

    List<String> languages = new ArrayList<>();
    languages.add("Java");

    form.setFilterLanguages(languages);

    form.setIncludeOnlyLicensed(true);
    form.setIncludeMerged(true);
    form.setIncludeArchived(true);

    Applications licensedApplications = contrastSDK.getFilteredApplications(orgId, form);

    assertFalse(licensedApplications.getApplications().isEmpty());
  }
}
