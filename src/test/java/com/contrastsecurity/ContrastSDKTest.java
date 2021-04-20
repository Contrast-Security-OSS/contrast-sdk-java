package com.contrastsecurity;

import com.contrastsecurity.exceptions.InvalidConversionException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.JobOutcomePolicyListResponse;
import com.contrastsecurity.http.RuleSeverity;
import com.contrastsecurity.http.SecurityCheckResponse;
import com.contrastsecurity.models.*;
import com.contrastsecurity.sdk.ContrastSDK;
import com.contrastsecurity.utils.ContrastSDKUtils;
import com.contrastsecurity.utils.MetadataDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.core.IsInstanceOf;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URLConnection;

import static org.junit.Assert.*;

public class ContrastSDKTest extends ContrastSDK {

    private static ContrastSDK contrastSDK;
    private static Gson gson;

    @BeforeClass
    public static void setUp() {
        contrastSDK = new ContrastSDK.Builder("test_user", "testApiKey", "testServiceKey").withApiUrl("http://localhost:19080/Contrast/api").build();
        gson = new GsonBuilder()
                .registerTypeAdapter(MetadataEntity.class, new MetadataDeserializer()).create();;
    }

    @Test
    public void testGetApplication() throws UnauthorizedException, IOException, ResourceNotFoundException {

        String applicationString = "{\"application\":{\"app_id\":\"3da856f4-c508-48b8-95a9-514eddefcbf3\",\"archived\":false,\"created\":1461599820000,\"status\":\"offline\",\"path\":\"/WebGoat\",\"name\":\"WebGoat\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false}}";

        Applications app = gson.fromJson(applicationString, Applications.class);
        assertNotNull(app);
        assertNotNull(app.getApplication());

        assertNull(app.getApplications());
    }

    @Test
    public void testGetApplications() throws UnauthorizedException, IOException, ResourceNotFoundException {

        String applicationsString = "{\"applications\":[{\"app_id\":\"72358543-bbdb-490c-8e3f-1b5f5e9a0bf7\",\"archived\":false,\"created\":1461631080000,\"status\":\"offline\",\"path\":\"/Curl\",\"name\":\"Contrast-Curl\",\"language\":\"Java\",\"last_seen\":1461631080000,\"total_modules\":1,\"master\":false},{\"app_id\":\"b9acb026-36a6-4f4e-b568-33168b7a5ae6\",\"archived\":false,\"created\":1460582653000,\"status\":\"offline\",\"path\":\"/portal\",\"name\":\"portal\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"53775e84-90a6-4a64-bafe-2b153c3a40f0\",\"archived\":false,\"created\":1460582640000,\"status\":\"offline\",\"path\":\"/\",\"name\":\"ROOT\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"9e88815f-bb0d-44b4-ac5a-f02f661e8947\",\"archived\":false,\"created\":1460582659000,\"status\":\"offline\",\"path\":\"/library\",\"name\":\"sakai-library\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"e9a14797-42e7-4ba4-8282-364fdf37026c\",\"archived\":false,\"created\":1460582916000,\"status\":\"offline\",\"path\":\"/sakai-user-tool\",\"name\":\"sakai-user-tool\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":2,\"master\":true},{\"app_id\":\"469b9147-5736-4b83-9158-657427d4c960\",\"archived\":false,\"created\":1461600682000,\"status\":\"offline\",\"path\":\"/examples\",\"name\":\"Servlet and JSP Examples\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false},{\"app_id\":\"3da856f4-c508-48b8-95a9-514eddefcbf3\",\"archived\":false,\"created\":1461599820000,\"status\":\"offline\",\"path\":\"/WebGoat\",\"name\":\"WebGoat\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false}]}";

        Applications apps = gson.fromJson(applicationsString, Applications.class);

        assertNotNull(apps);
        assertNotNull(apps.getApplications());

        assertNull(apps.getApplication());
        assertTrue(!apps.getApplications().isEmpty());
    }

    @Test
    public void testGetFilteredApplications() throws UnauthorizedException, IOException, ResourceNotFoundException {

        String applicationsString = "{\"applications\":[{\"app_id\":\"72358543-bbdb-490c-8e3f-1b5f5e9a0bf7\",\"archived\":false,\"created\":1461631080000,\"status\":\"offline\",\"path\":\"/Curl\",\"name\":\"Contrast-Curl\",\"language\":\"Java\",\"last_seen\":1461631080000,\"total_modules\":1,\"master\":false},{\"app_id\":\"b9acb026-36a6-4f4e-b568-33168b7a5ae6\",\"archived\":false,\"created\":1460582653000,\"status\":\"offline\",\"path\":\"/portal\",\"name\":\"portal\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"53775e84-90a6-4a64-bafe-2b153c3a40f0\",\"archived\":false,\"created\":1460582640000,\"status\":\"offline\",\"path\":\"/\",\"name\":\"ROOT\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"9e88815f-bb0d-44b4-ac5a-f02f661e8947\",\"archived\":false,\"created\":1460582659000,\"status\":\"offline\",\"path\":\"/library\",\"name\":\"sakai-library\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"e9a14797-42e7-4ba4-8282-364fdf37026c\",\"archived\":false,\"created\":1460582916000,\"status\":\"offline\",\"path\":\"/sakai-user-tool\",\"name\":\"sakai-user-tool\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":2,\"master\":true},{\"app_id\":\"469b9147-5736-4b83-9158-657427d4c960\",\"archived\":false,\"created\":1461600682000,\"status\":\"offline\",\"path\":\"/examples\",\"name\":\"Servlet and JSP Examples\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false},{\"app_id\":\"3da856f4-c508-48b8-95a9-514eddefcbf3\",\"archived\":false,\"created\":1461599820000,\"status\":\"offline\",\"path\":\"/WebGoat\",\"name\":\"WebGoat\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false}]}";

        Applications apps = gson.fromJson(applicationsString, Applications.class);

        assertNotNull(apps);
        assertNotNull(apps.getApplications());

        assertNull(apps.getApplication());
        assertTrue(!apps.getApplications().isEmpty());
    }

    @Test
    public void testGetApplicationsWithMetadata() throws UnauthorizedException, IOException, ResourceNotFoundException, InvalidConversionException {

        String applicationsString = "{\"applications\":[{\"app_id\":\"72358543-bbdb-490c-8e3f-1b5f5e9a0bf7\",\"archived\":false,\"created\":1461631080000,\"status\":\"offline\",\"path\":\"/Curl\",\"name\":\"Contrast-Curl\",\"language\":\"Java\",\"last_seen\":1461631080000,\"total_modules\":1,\"master\":false, \"metadataEntities\": [ { \"fieldName\": \"Contact\", \"fieldValue\": \"\", \"type\": \"PERSON_OF_CONTACT\", \"unique\": false, \"subfields\": [ { \"fieldName\": \"Contact Name\", \"fieldValue\": \"Contrast User\", \"type\": \"CONTACT_NAME\" }, { \"fieldName\": \"Contact Email\", \"fieldValue\": \"support@contrastsecurity.com\", \"type\": \"EMAIL\" }, { \"fieldName\": \"Contact Phone\", \"fieldValue\": \"1234567890\", \"type\": \"PHONE\" } ] }, { \"fieldName\": \"bU\", \"fieldValue\": \"PEDS\", \"type\": \"STRING\" }, {\"fieldName\": \"askId\", \"fieldValue\": \"123456789\", \"type\": \"NUMERIC\" }]}, ,{\"app_id\":\"9e88815f-bb0d-44b4-ac5a-f02f661e8947\",\"archived\":false,\"created\":1460582659000,\"status\":\"offline\",\"path\":\"/library\",\"name\":\"sakai-library\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false}]}";

        Applications apps = gson.fromJson(applicationsString, Applications.class);

        assertNotNull(apps);
        assertNotNull(apps.getApplications());

        assertNotNull(apps.getApplications().get(0).getMetadataEntities());
        MetadataEntity[] metadataEntities = apps.getApplications().get(0).getMetadataEntities();
        assertEquals(3, apps.getApplications().get(0).getMetadataEntities().length);

        assertNotNull(metadataEntities[0]);
        assertEquals(metadataEntities[0].getType(), MetadataEntity.MetadataType.POINT_OF_CONTACT);
        assertThat(metadataEntities[0].getAsPointOfContactMetadata(), new IsInstanceOf(PointOfContactMetadata.class));

        assertNotNull(metadataEntities[1]);
        assertEquals(metadataEntities[1].getType(), MetadataEntity.MetadataType.STRING);
        assertThat(metadataEntities[1].getAsFreeformMetadata(), new IsInstanceOf(FreeformMetadata.class));

        assertNotNull(metadataEntities[2]);
        assertEquals(metadataEntities[2].getType(), MetadataEntity.MetadataType.NUMERIC);
        assertThat(metadataEntities[2].getAsNumericMetadata(), new IsInstanceOf(NumericMetadata.class));
    }

    @Test
    public void testGetTraces() throws UnauthorizedException, IOException, ResourceNotFoundException {

        String tracesString = "{\"count\":2,\"traces\":[{\"title\":\"Application Disables \\u0027secure\\u0027 Flag on Cookies observed at DefaultSavedRequest.java\",\"language\":\"Java\",\"status\":\"Reported\",\"uuid\":\"KNBG-XTO9-ED1O-PG2X\",\"rule_name\":\"cookie-flags-missing\",\"severity\":\"Medium\",\"likelihood\":\"High\",\"impact\":\"Low\",\"confidence\":\"High\",\"first_time_seen\":1461600923859,\"last_time_seen\":1461601039100,\"category\":\"Secure Communications\",\"platform\":\"Oracle Corporation\",\"total_traces_received\":5,\"visible\":true},{\"title\":\"Insecure Authentication Protocol\",\"evidence\":\"Authorization: Basic Z3Vl...Q6Z3Vlc3Q\\u003d\",\"language\":\"Java\",\"status\":\"Reported\",\"uuid\":\"IJ92-WGDU-JCY4-F4EZ\",\"rule_name\":\"insecure-auth-protocol\",\"severity\":\"Medium\",\"likelihood\":\"Medium\",\"impact\":\"Medium\",\"confidence\":\"High\",\"first_time_seen\":1461600923853,\"last_time_seen\":1461601039100,\"category\":\"Access Control\",\"platform\":\"Oracle Corporation\",\"total_traces_received\":5,\"visible\":true}]}";

        Traces traces = gson.fromJson(tracesString, Traces.class);

        assertNotNull(traces);
        assertNotNull(traces.getTraces());

        assertTrue(traces.getCount() > 0);
    }


    @Test
    public void getGetRules() throws UnauthorizedException, IOException, ResourceNotFoundException {

        String rulesString = "{\"rules\":[{\"description\":\"Verifies that caching controls are used to protect application content.\",\"title\":\"Anti-Caching Controls Missing\",\"category\":\"Caching\",\"impact\":\"Low\",\"likelihood\":\"Low\",\"enabled\":\"true\",\"cwe\":\"http://cwe.mitre.org/data/definitions/525.html\",\"owasp\":\"https://www.owasp.org/index.php/Testing_for_Logout_and_Browser_Cache_Management_(OWASP-AT-007)\",\"confidence\":\"Low\",\"severity\":\"Note\",\"serviceLevel\":\"Enterprise\",\"references\":[],\"free\":false,\"name\":\"cache-controls-missing\"},{\"description\":\"Verifies that cookies have the \\u0027secure\\u0027 flag.\",\"title\":\"Application Disables \\u0027secure\\u0027 Flag on Cookies\",\"category\":\"Secure Communications\",\"impact\":\"Low\",\"likelihood\":\"High\",\"enabled\":\"true\",\"cwe\":\"http://cwe.mitre.org/data/definitions/614.html\",\"owasp\":\"http://www.owasp.org/index.php/SecureFlag\",\"confidence\":\"High\",\"severity\":\"Medium\",\"serviceLevel\":\"Enterprise\",\"references\":[\"https://www.owasp.org/index.php/Top_10_2013-A2-Broken_Authentication_and_Session_Management\"],\"free\":false,\"name\":\"cookie-flags-missing\"},{\"description\":\"Verifies that no untrusted data is used to build a path used in forwards.\",\"title\":\"Arbitrary Server Side Forwards\",\"category\":\"Access Control\",\"impact\":\"High\",\"likelihood\":\"Medium\",\"enabled\":\"true\",\"cwe\":\"http://cwe.mitre.org/data/definitions/22.html\",\"owasp\":\"https://www.owasp.org/index.php/Top_10_2013-A10-Unvalidated_Redirects_and_Forwards\",\"confidence\":\"High\",\"severity\":\"High\",\"serviceLevel\":\"Enterprise\",\"references\":[\"https://www.owasp.org/index.php/Top_10_2013-A10-Unvalidated_Redirects_and_Forwards\",\"https://blog.gdssecurity.com/labs/2011/9/9/net-servertransfer-vs-responseredirect-reiterating-a-securit.html\"],\"free\":false,\"name\":\"unvalidated-forward\"},{\"description\":\"Verifies that the application\\u0027s authorization rules do not include an allow all user rule before a deny rule.\",\"title\":\"Authorization Rules Misordered\",\"category\":\"Access Control\",\"impact\":\"Medium\",\"likelihood\":\"Medium\",\"enabled\":\"true\",\"cwe\":\"https://cwe.mitre.org/data/definitions/284.html\",\"owasp\":\"https://www.owasp.org/index.php/Top_10_2013-A5-Security_Misconfiguration\",\"confidence\":\"Medium\",\"severity\":\"Medium\",\"serviceLevel\":\"Enterprise\",\"references\":[\"http://msdn.microsoft.com/en-us/library/system.web.configuration.authorizationsection.aspx\"],\"free\":false,\"name\":\"authorization-rules-misordered\"},{\"description\":\"Verifies that the application\\u0027s authorization rules include a deny rule.\",\"title\":\"Authorization Rules Missing Deny Rule\",\"category\":\"Access Control\",\"impact\":\"Medium\",\"likelihood\":\"Medium\",\"enabled\":\"true\",\"cwe\":\"http://cwe.mitre.org/data/definitions/294.html\",\"owasp\":\"https://www.owasp.org/index.php/Top_10_2013-A5-Security_Misconfiguration\",\"confidence\":\"Medium\",\"severity\":\"Medium\",\"serviceLevel\":\"Enterprise\",\"references\":[\"http://msdn.microsoft.com/en-us/library/system.web.configuration.authorizationsection.aspx\"],\"free\":false,\"name\":\"authorization-missing-deny\"}]}";

        Rules rules = gson.fromJson(rulesString, Rules.class);

        assertNotNull(rules);
        assertNotNull(rules.getRules());
    }


    @Test
    public void getServers() throws UnauthorizedException, IOException, ResourceNotFoundException {

        String serversString = "{\"servers\":[{\"server_id\":2,\"name\":\"test_name\",\"hostname\":\"test_hostname\",\"last_startup\":1464286620000,\"last_trace_received\":0,\"last_activity\":1464286980000,\"num_apps\":0,\"path\":\"/Users/test/\",\"status\":\"ONLINE\",\"type\":\"Apache Tomcat 7.0.65\",\"agent_version\":\"3.2.8\",\"assess\":true,\"assessPending\":false,\"defend\":false,\"defendPending\":false,\"container\":\"tomcat\",\"environment\":\"DEVELOPMENT\",\"logEnhancerPending\":false,\"logLevel\":\"ERROR\",\"noPending\":true,\"assessSensors\":false,\"assess_last_update\":1464188870000},{\"server_id\":1,\"name\":\"test_name_2\",\"hostname\":\"test_host_name_2\",\"last_startup\":1464189060000,\"last_trace_received\":0,\"last_activity\":1464189120000,\"num_apps\":0,\"path\":\"/Users/justinleo/Jar-WebGoat/\",\"status\":\"OFFLINE\",\"type\":\"Apache Tomcat 7.0.59\",\"agent_version\":\"3.2.8\",\"assess\":true,\"assessPending\":false,\"defend\":false,\"defendPending\":false,\"container\":\"other\",\"environment\":1,\"logEnhancerPending\":false,\"logLevel\":\"ERROR\",\"noPending\":true,\"assessSensors\":false,\"assess_last_update\":1463779837000}]}";

        Servers servers = gson.fromJson(serversString, Servers.class);

        assertNotNull(servers);
        assertNotNull(servers.getServers());
    }

    @Test
    public void testMakeSecurityCheck() {
        String securityCheckResponseString = "{'security_check':{'id':1,'application_name':'testName','application_id':'testId1','origin':'JENKINS','result':false, 'job_outcome_policy':{'name':'testPolicy','outcome':'UNSTABLE','severities':{'MEDIUM':1}}}}";

        SecurityCheckResponse response = gson.fromJson(securityCheckResponseString, SecurityCheckResponse.class);
        SecurityCheck securityCheck = response.getSecurityCheck();
        JobOutcomePolicy jobOutcomePolicy = securityCheck.getJobOutcomePolicy();
        assertEquals(1l, securityCheck.getId().longValue());
        assertEquals("testName", securityCheck.getApplicationName());
        assertEquals("testPolicy", jobOutcomePolicy.getName());
        assertEquals(JobOutcomePolicy.Outcome.UNSTABLE, jobOutcomePolicy.getOutcome());
        assertEquals(1, jobOutcomePolicy.getSeverities().size());
        assertEquals(1l, jobOutcomePolicy.getSeverities().get(RuleSeverity.MEDIUM).longValue());

    }

    @Test
    public void testGetEnabledJobOutcomePolicies() {
        String jobOutcomePolicyListResponseString = "{'policies':[{'name':'testJobOutcomePolicy','outcome':'SUCCESS'}]}";
        JobOutcomePolicyListResponse response = gson.fromJson(jobOutcomePolicyListResponseString, JobOutcomePolicyListResponse.class);
        assertNotNull(response.getPolicies());
        assertEquals(response.getPolicies().get(0).getName(), "testJobOutcomePolicy");
    }

    @Test
    public void testGetEnabledJobOutcomePoliciesByApplication() {
        String jobOutcomePolicyListResponseString = "{'policies':[{'name':'testJobOutcomePolicy','outcome':'UNSTABLE'}]}";
        JobOutcomePolicyListResponse response = gson.fromJson(jobOutcomePolicyListResponseString, JobOutcomePolicyListResponse.class);
        assertNotNull(response.getPolicies());
        assertEquals(response.getPolicies().get(0).getName(), "testJobOutcomePolicy");
    }

    @Test
    public void setCustomTiemouts() throws IOException {
        final int connectionTimeout = 1000;
        final int readTimeout = 4000;
        contrastSDK.setConnectionTimeout(connectionTimeout);
        contrastSDK.setReadTimeout(readTimeout);

        URLConnection conn = contrastSDK.makeConnection("https://www.google.com", HttpMethod.GET.toString());

        assertEquals(connectionTimeout, conn.getConnectTimeout());
        assertEquals(readTimeout, conn.getReadTimeout());
    }

    @Test
    public void negativeTimeoutsAreNotSetTest() throws IOException {
        final int connectionTimeout = -10;
        final int readTimeout = -50;

        contrastSDK.setConnectionTimeout(connectionTimeout);
        contrastSDK.setReadTimeout(readTimeout);

        URLConnection conn = contrastSDK.makeConnection("https://www.google.com", HttpMethod.GET.toString());

        assertNotEquals(connectionTimeout, conn.getConnectTimeout());
        assertNotEquals(readTimeout, conn.getReadTimeout());
    }

    @Test
    public void testEnsureApi() throws IOException {
        final String expectedApi = "http://localhost:19080/Contrast/api";
        final String ensureUrlOne = ContrastSDKUtils.ensureApi("http://localhost:19080/Contrast/");
        final String ensureUrlTwo = ContrastSDKUtils.ensureApi("http://localhost:19080/Contrast");

        assertEquals(expectedApi, ensureUrlOne);
        assertEquals(expectedApi, ensureUrlTwo);

        final String unchangedApi = "http://localhost:19080/";
        final String ensureUnchanged = ContrastSDKUtils.ensureApi(unchangedApi);

        assertEquals(unchangedApi, ensureUnchanged);
    }

    @Test
    public void testMalformedURL() throws IOException {
        final String expectedUrl = "htp:/localhost:19080/Contrast/api";
        final String badUrl = "htp:/localhost:19080/Contrast/";
        final String actualUrl = ContrastSDKUtils.ensureApi(badUrl);
        assertEquals(actualUrl, expectedUrl);
    }

    @Test
    public void testNullUrl() throws IOException {
        final String nullUrl = ContrastSDKUtils.ensureApi(null);
        assertNull(nullUrl);
    }

    @Test
    public void blankUrl() {
        final String blankUrl = ContrastSDKUtils.ensureApi("");
        final String ensureBlank = "";
        assertEquals(ensureBlank, blankUrl);
    }

    @Test
    public void testApplicationYearlyTrend() {
        final String exampleApplicationYearlyTrend = "{  \"success\": true,  \"messages\": [    \"Total Vulnerability trend loaded successfully\"  ],  \"open\": [    {      \"timestamp\": 1567310400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1569902400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1572580800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1575176400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1577854800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1580533200000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1583038800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1585713600000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1588305600000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1590984000000,      \"count\": 9,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 9        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1593576000000,      \"count\": 8,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 8        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1596254400000,      \"count\": 8,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 8        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    }  ],  \"closed\": [    {      \"timestamp\": 1567310400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1569902400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1572580800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1575176400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1577854800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1580533200000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1583038800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1585713600000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1588305600000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1590984000000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1593576000000,      \"count\": 1,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 1        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1596254400000,      \"count\": 1,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 1        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    }  ]}";
        VulnerabilityTrend vulnerabilityTrend = gson.fromJson(exampleApplicationYearlyTrend, VulnerabilityTrend.class);
        assertNotNull(vulnerabilityTrend);
        assertNotNull(vulnerabilityTrend.getOpenTrend());
        assertNotNull(vulnerabilityTrend.getClosedTrend());
    }

}