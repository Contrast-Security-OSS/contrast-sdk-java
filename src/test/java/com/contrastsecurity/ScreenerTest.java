package com.contrastsecurity;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class ScreenerTest {

  private static ContrastSDK contrastSDK;
  private static final String TEST_PROPERTIES = "test.properties";
  private static Properties properties;

  @BeforeAll
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
      assertThat(true).isTrue();
    }

    assertThat(contrastJar.exists()).isTrue();
    assertThat(FileUtils.sizeOf(contrastJar) > 0).isTrue();
  }

  @Test
  public void testGetProfileOrganizations() throws IOException, UnauthorizedException {
    Organizations organizations = contrastSDK.getProfileOrganizations();

    assertThat(!organizations.getOrganizations().isEmpty()).isTrue();
  }

  @Test
  public void testGetProfileDefaultOrganization() throws IOException, UnauthorizedException {
    Organizations organizations = contrastSDK.getProfileDefaultOrganizations();

    assertThat(organizations.getOrganization().getName() != null).isTrue();
  }

  @Test
  public void testGetApplications() throws IOException, UnauthorizedException {
    String orgId = properties.getProperty("orgId");

    Applications applications = contrastSDK.getApplications(orgId);

    assertThat(!applications.getApplications().isEmpty()).isTrue();
  }

  @Test
  public void testGetServers() throws IOException, UnauthorizedException {
    String orgId = properties.getProperty("orgId");

    Servers servers = contrastSDK.getServers(orgId, null);

    assertThat(!servers.getServers().isEmpty()).isTrue();
  }

  @Test
  public void testGetApplicationTraces() throws IOException, UnauthorizedException {
    String orgId = properties.getProperty("orgId");

    Applications applications = contrastSDK.getApplications(orgId);

    assertThat(!applications.getApplications().isEmpty()).isTrue();

    Application application = applications.getApplications().get(0);

    assertThat(application.getId() != null).isTrue();

    TraceFilterForm form = new TraceFilterForm();

    form.setExpand(EnumSet.of(TraceFilterForm.TraceExpandValue.APPLICATION));

    Traces traces = contrastSDK.getTraces(orgId, application.getId(), form);

    assertThat(!traces.getTraces().isEmpty()).isTrue();
  }

  @Test
  public void testGetFilteredApplication() throws IOException, UnauthorizedException {
    String orgId = properties.getProperty("orgId");

    ApplicationFilterForm form = new ApplicationFilterForm();

    Applications applications = contrastSDK.getFilteredApplications(orgId, form);

    assertThat(applications.getApplications().isEmpty()).isFalse();

    Application application = applications.getApplications().get(0);

    assertThat(application.getId()).isNotNull();

    List<String> languages = new ArrayList<>();
    languages.add("Java");

    form.setFilterLanguages(languages);

    form.setIncludeOnlyLicensed(true);
    form.setIncludeMerged(true);
    form.setIncludeArchived(true);

    Applications licensedApplications = contrastSDK.getFilteredApplications(orgId, form);

    assertThat(licensedApplications.getApplications().isEmpty()).isFalse();
  }
}
