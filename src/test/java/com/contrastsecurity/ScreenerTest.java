package com.contrastsecurity;

import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.http.FilterForm;
import com.contrastsecurity.models.*;
import com.contrastsecurity.sdk.ContrastSDK;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public class ScreenerTest {

    private static ContrastSDK contrastSDK;
    private static final String TEST_PROPERTIES = "test.properties";
    private static Properties properties;
    private static Gson gson;


    @BeforeClass
    public static void setUp() throws IOException {
        InputStream propertiesFileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_PROPERTIES);
        properties = new Properties();
        properties.load(propertiesFileInputStream);

        contrastSDK = new ContrastSDK(properties.getProperty("username"),
                                      properties.getProperty("apiKey"),
                                      properties.getProperty("serviceKey"),
                                      properties.getProperty("localTeamServerUrl"));
        gson = new Gson();
    }

    @Test
    public void testDownloadAgent() throws IOException, UnauthorizedException {
        File contrastJar = new File("contrast.jar");

        FileUtils.writeByteArrayToFile(contrastJar, contrastSDK.getAgent(AgentType.JAVA, properties.getProperty("orgId")));

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
    public void testGetTraces() throws IOException, UnauthorizedException {
        String orgId = properties.getProperty("orgId");

        Servers servers = contrastSDK.getServers(orgId, null);

        assertTrue(!servers.getServers().isEmpty());
    }

    @Test
    public void testGetApplicationTraces() throws  IOException, UnauthorizedException {
        String orgId = properties.getProperty("orgId");

        Applications applications = contrastSDK.getApplications(orgId);

        assertTrue(!applications.getApplications().isEmpty());

        Application application = applications.getApplications().get(0);

        assertTrue(application.getId() != null);

        Traces traces = contrastSDK.getTraces(orgId, application.getId(), EnumSet.of(FilterForm.TraceExpandValue.CARD, FilterForm.TraceExpandValue.EVENTS));

        assertTrue(!traces.getTraces().isEmpty());
    }
}
