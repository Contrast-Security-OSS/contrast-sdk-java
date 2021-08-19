package com.contrastsecurity.sdk;

import static org.assertj.core.api.Assertions.assertThat;

import com.contrastsecurity.exceptions.InvalidConversionException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.http.HttpMethod;
import com.contrastsecurity.http.JobOutcomePolicyListResponse;
import com.contrastsecurity.http.RuleSeverity;
import com.contrastsecurity.http.SecurityCheckResponse;
import com.contrastsecurity.models.Applications;
import com.contrastsecurity.models.Chapter;
import com.contrastsecurity.models.EventResource;
import com.contrastsecurity.models.EventSummaryResponse;
import com.contrastsecurity.models.HttpRequestResponse;
import com.contrastsecurity.models.JobOutcomePolicy;
import com.contrastsecurity.models.MetadataEntity;
import com.contrastsecurity.models.RecommendationResponse;
import com.contrastsecurity.models.Rules;
import com.contrastsecurity.models.SecurityCheck;
import com.contrastsecurity.models.Servers;
import com.contrastsecurity.models.Story;
import com.contrastsecurity.models.StoryResponse;
import com.contrastsecurity.models.Tags;
import com.contrastsecurity.models.TagsResponse;
import com.contrastsecurity.models.Traces;
import com.contrastsecurity.models.VulnerabilityTrend;
import com.contrastsecurity.utils.ContrastSDKUtils;
import com.contrastsecurity.utils.MetadataDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link ContrastSDK} */
final class ContrastSDKTest {

  private static ContrastSDK contrastSDK;
  private static Gson gson;

  @BeforeAll
  public static void setUp() {
    contrastSDK =
        new ContrastSDK.Builder("test_user", "testApiKey", "testServiceKey")
            .withApiUrl("http://localhost:19080/Contrast/api")
            .withUserAgentProduct(UserAgentProduct.of("INTELLIJ_INTEGRATION", "1.0.0"))
            .build();
    gson =
        new GsonBuilder()
            .registerTypeAdapter(MetadataEntity.class, new MetadataDeserializer())
            .create();
  }

  @Test
  void builds_default_user_agent() {
    // WHEN build user-agent string with null major product
    final String ua = ContrastSDK.buildUserAgent(null);

    // THEN user-agent string contains only the contrast-sdk-java and Java products
    // does not verify version, because that would lead to a brittle test
    assertThat(ua).matches("contrast-sdk-java/\\d\\.\\d(\\d)?(-SNAPSHOT)? Java/\\d+.*");
  }

  @Test
  void builds_user_agent_with_custom_product() {
    // WHEN build a user-agent string with a custom product provided by the user
    final UserAgentProduct product =
        UserAgentProduct.of("contrast-maven-plugin", "3.2.0", "Apache Maven 3.8.1");
    final String ua = ContrastSDK.buildUserAgent(product);

    // THEN user-agent string contains 3 products contrast-sdk-java
    assertThat(ua)
        .matches(
            "contrast-maven-plugin/3.2.0 \\(Apache Maven 3.8.1\\) contrast-sdk-java/\\d\\.\\d(\\d)?(-SNAPSHOT)? Java/\\d+.*");
  }

  @Test
  void builds_user_agent_with_custom_product_no_version() {
    // WHEN build a user-agent string with a custom product provided by the user
    final UserAgentProduct product = UserAgentProduct.of("contrast-maven-plugin");
    final String ua = ContrastSDK.buildUserAgent(product);

    // THEN user-agent string contains 3 products contrast-sdk-java
    assertThat(ua)
        .matches("contrast-maven-plugin contrast-sdk-java/\\d\\.\\d(\\d)?(-SNAPSHOT)? Java/\\d+.*");
  }

  /** Builds a new {@code HttpURLConnection}, but does not send an outgoing request */
  @Test
  void sets_user_agent_header() throws IOException {
    final HttpURLConnection connection =
        contrastSDK.makeConnection("https://does-not-exist.contrastsecurity.com", "GET");

    final String ua = connection.getRequestProperty("User-Agent");
    assertThat(ua)
        .matches(
            "INTELLIJ_INTEGRATION/1.0.0 contrast-sdk-java/\\d\\.\\d(\\d)?(-SNAPSHOT)? Java/\\d+.*");
  }

  @Test
  public void testGetApplication()
      throws UnauthorizedException, IOException, ResourceNotFoundException {

    String applicationString =
        "{\"application\":{\"app_id\":\"3da856f4-c508-48b8-95a9-514eddefcbf3\",\"archived\":false,\"created\":1461599820000,\"status\":\"offline\",\"path\":\"/WebGoat\",\"name\":\"WebGoat\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false}}";

    Applications app = gson.fromJson(applicationString, Applications.class);
    assertThat(app).isNotNull();
    assertThat(app.getApplication()).isNotNull();

    assertThat(app.getApplications()).isNull();
  }

  @Test
  public void testGetApplications()
      throws UnauthorizedException, IOException, ResourceNotFoundException {

    String applicationsString =
        "{\"applications\":[{\"app_id\":\"72358543-bbdb-490c-8e3f-1b5f5e9a0bf7\",\"archived\":false,\"created\":1461631080000,\"status\":\"offline\",\"path\":\"/Curl\",\"name\":\"Contrast-Curl\",\"language\":\"Java\",\"last_seen\":1461631080000,\"total_modules\":1,\"master\":false},{\"app_id\":\"b9acb026-36a6-4f4e-b568-33168b7a5ae6\",\"archived\":false,\"created\":1460582653000,\"status\":\"offline\",\"path\":\"/portal\",\"name\":\"portal\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"53775e84-90a6-4a64-bafe-2b153c3a40f0\",\"archived\":false,\"created\":1460582640000,\"status\":\"offline\",\"path\":\"/\",\"name\":\"ROOT\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"9e88815f-bb0d-44b4-ac5a-f02f661e8947\",\"archived\":false,\"created\":1460582659000,\"status\":\"offline\",\"path\":\"/library\",\"name\":\"sakai-library\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"e9a14797-42e7-4ba4-8282-364fdf37026c\",\"archived\":false,\"created\":1460582916000,\"status\":\"offline\",\"path\":\"/sakai-user-tool\",\"name\":\"sakai-user-tool\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":2,\"master\":true},{\"app_id\":\"469b9147-5736-4b83-9158-657427d4c960\",\"archived\":false,\"created\":1461600682000,\"status\":\"offline\",\"path\":\"/examples\",\"name\":\"Servlet and JSP Examples\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false},{\"app_id\":\"3da856f4-c508-48b8-95a9-514eddefcbf3\",\"archived\":false,\"created\":1461599820000,\"status\":\"offline\",\"path\":\"/WebGoat\",\"name\":\"WebGoat\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false}]}";

    Applications apps = gson.fromJson(applicationsString, Applications.class);

    assertThat(apps).isNotNull();
    assertThat(apps.getApplications()).isNotNull();

    assertThat(apps.getApplication()).isNull();
    assertThat(!apps.getApplications().isEmpty()).isTrue();
  }

  @Test
  public void testGetFilteredApplications()
      throws UnauthorizedException, IOException, ResourceNotFoundException {

    String applicationsString =
        "{\"applications\":[{\"app_id\":\"72358543-bbdb-490c-8e3f-1b5f5e9a0bf7\",\"archived\":false,\"created\":1461631080000,\"status\":\"offline\",\"path\":\"/Curl\",\"name\":\"Contrast-Curl\",\"language\":\"Java\",\"last_seen\":1461631080000,\"total_modules\":1,\"master\":false},{\"app_id\":\"b9acb026-36a6-4f4e-b568-33168b7a5ae6\",\"archived\":false,\"created\":1460582653000,\"status\":\"offline\",\"path\":\"/portal\",\"name\":\"portal\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"53775e84-90a6-4a64-bafe-2b153c3a40f0\",\"archived\":false,\"created\":1460582640000,\"status\":\"offline\",\"path\":\"/\",\"name\":\"ROOT\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"9e88815f-bb0d-44b4-ac5a-f02f661e8947\",\"archived\":false,\"created\":1460582659000,\"status\":\"offline\",\"path\":\"/library\",\"name\":\"sakai-library\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false},{\"app_id\":\"e9a14797-42e7-4ba4-8282-364fdf37026c\",\"archived\":false,\"created\":1460582916000,\"status\":\"offline\",\"path\":\"/sakai-user-tool\",\"name\":\"sakai-user-tool\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":2,\"master\":true},{\"app_id\":\"469b9147-5736-4b83-9158-657427d4c960\",\"archived\":false,\"created\":1461600682000,\"status\":\"offline\",\"path\":\"/examples\",\"name\":\"Servlet and JSP Examples\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false},{\"app_id\":\"3da856f4-c508-48b8-95a9-514eddefcbf3\",\"archived\":false,\"created\":1461599820000,\"status\":\"offline\",\"path\":\"/WebGoat\",\"name\":\"WebGoat\",\"language\":\"Java\",\"last_seen\":1461737160000,\"total_modules\":1,\"master\":false}]}";

    Applications apps = gson.fromJson(applicationsString, Applications.class);

    assertThat(apps).isNotNull();
    assertThat(apps.getApplications()).isNotNull();

    assertThat(apps.getApplication()).isNull();
    assertThat(!apps.getApplications().isEmpty()).isTrue();
  }

  @Test
  public void testGetApplicationsWithMetadata()
      throws UnauthorizedException, IOException, ResourceNotFoundException,
          InvalidConversionException {

    String applicationsString =
        "{\"applications\":[{\"app_id\":\"72358543-bbdb-490c-8e3f-1b5f5e9a0bf7\",\"archived\":false,\"created\":1461631080000,\"status\":\"offline\",\"path\":\"/Curl\",\"name\":\"Contrast-Curl\",\"language\":\"Java\",\"last_seen\":1461631080000,\"total_modules\":1,\"master\":false, \"metadataEntities\": [ { \"fieldName\": \"Contact\", \"fieldValue\": \"\", \"type\": \"PERSON_OF_CONTACT\", \"unique\": false, \"subfields\": [ { \"fieldName\": \"Contact Name\", \"fieldValue\": \"Contrast User\", \"type\": \"CONTACT_NAME\" }, { \"fieldName\": \"Contact Email\", \"fieldValue\": \"support@contrastsecurity.com\", \"type\": \"EMAIL\" }, { \"fieldName\": \"Contact Phone\", \"fieldValue\": \"1234567890\", \"type\": \"PHONE\" } ] }, { \"fieldName\": \"bU\", \"fieldValue\": \"PEDS\", \"type\": \"STRING\" }, {\"fieldName\": \"askId\", \"fieldValue\": \"123456789\", \"type\": \"NUMERIC\" }]}, ,{\"app_id\":\"9e88815f-bb0d-44b4-ac5a-f02f661e8947\",\"archived\":false,\"created\":1460582659000,\"status\":\"offline\",\"path\":\"/library\",\"name\":\"sakai-library\",\"language\":\"Java\",\"last_seen\":1460925180000,\"total_modules\":1,\"master\":false}]}";

    Applications apps = gson.fromJson(applicationsString, Applications.class);

    assertThat(apps).isNotNull();
    assertThat(apps.getApplications()).isNotNull();

    assertThat(apps.getApplications().get(0).getMetadataEntities()).isNotNull();
    MetadataEntity[] metadataEntities = apps.getApplications().get(0).getMetadataEntities();
    assertThat(apps.getApplications().get(0).getMetadataEntities().length).isEqualTo(3);

    assertThat(metadataEntities[0]).isNotNull();
    assertThat(MetadataEntity.MetadataType.POINT_OF_CONTACT)
        .isEqualTo(metadataEntities[0].getType());

    assertThat(metadataEntities[1]).isNotNull();
    assertThat(MetadataEntity.MetadataType.STRING).isEqualTo(metadataEntities[1].getType());

    assertThat(metadataEntities[2]).isNotNull();
    assertThat(MetadataEntity.MetadataType.NUMERIC).isEqualTo(metadataEntities[2].getType());
  }

  @Test
  public void testGetTraces() throws UnauthorizedException, IOException, ResourceNotFoundException {

    String tracesString =
        "{\"count\":2,\"traces\":[{\"title\":\"Application Disables \\u0027secure\\u0027 Flag on Cookies observed at DefaultSavedRequest.java\",\"language\":\"Java\",\"status\":\"Reported\",\"uuid\":\"KNBG-XTO9-ED1O-PG2X\",\"rule_name\":\"cookie-flags-missing\",\"severity\":\"Medium\",\"likelihood\":\"High\",\"impact\":\"Low\",\"confidence\":\"High\",\"first_time_seen\":1461600923859,\"last_time_seen\":1461601039100,\"category\":\"Secure Communications\",\"platform\":\"Oracle Corporation\",\"total_traces_received\":5,\"visible\":true},{\"title\":\"Insecure Authentication Protocol\",\"evidence\":\"Authorization: Basic Z3Vl...Q6Z3Vlc3Q\\u003d\",\"language\":\"Java\",\"status\":\"Reported\",\"uuid\":\"IJ92-WGDU-JCY4-F4EZ\",\"rule_name\":\"insecure-auth-protocol\",\"severity\":\"Medium\",\"likelihood\":\"Medium\",\"impact\":\"Medium\",\"confidence\":\"High\",\"first_time_seen\":1461600923853,\"last_time_seen\":1461601039100,\"category\":\"Access Control\",\"platform\":\"Oracle Corporation\",\"total_traces_received\":5,\"visible\":true}]}";

    Traces traces = gson.fromJson(tracesString, Traces.class);

    assertThat(traces).isNotNull();
    assertThat(traces.getTraces()).isNotNull();

    assertThat(traces.getCount() > 0).isTrue();
  }

  @Test
  public void getGetRules() throws UnauthorizedException, IOException, ResourceNotFoundException {

    String rulesString =
        "{\"rules\":[{\"description\":\"Verifies that caching controls are used to protect application content.\",\"title\":\"Anti-Caching Controls Missing\",\"category\":\"Caching\",\"impact\":\"Low\",\"likelihood\":\"Low\",\"enabled\":\"true\",\"cwe\":\"http://cwe.mitre.org/data/definitions/525.html\",\"owasp\":\"https://www.owasp.org/index.php/Testing_for_Logout_and_Browser_Cache_Management_(OWASP-AT-007)\",\"confidence\":\"Low\",\"severity\":\"Note\",\"serviceLevel\":\"Enterprise\",\"references\":[],\"free\":false,\"name\":\"cache-controls-missing\"},{\"description\":\"Verifies that cookies have the \\u0027secure\\u0027 flag.\",\"title\":\"Application Disables \\u0027secure\\u0027 Flag on Cookies\",\"category\":\"Secure Communications\",\"impact\":\"Low\",\"likelihood\":\"High\",\"enabled\":\"true\",\"cwe\":\"http://cwe.mitre.org/data/definitions/614.html\",\"owasp\":\"http://www.owasp.org/index.php/SecureFlag\",\"confidence\":\"High\",\"severity\":\"Medium\",\"serviceLevel\":\"Enterprise\",\"references\":[\"https://www.owasp.org/index.php/Top_10_2013-A2-Broken_Authentication_and_Session_Management\"],\"free\":false,\"name\":\"cookie-flags-missing\"},{\"description\":\"Verifies that no untrusted data is used to build a path used in forwards.\",\"title\":\"Arbitrary Server Side Forwards\",\"category\":\"Access Control\",\"impact\":\"High\",\"likelihood\":\"Medium\",\"enabled\":\"true\",\"cwe\":\"http://cwe.mitre.org/data/definitions/22.html\",\"owasp\":\"https://www.owasp.org/index.php/Top_10_2013-A10-Unvalidated_Redirects_and_Forwards\",\"confidence\":\"High\",\"severity\":\"High\",\"serviceLevel\":\"Enterprise\",\"references\":[\"https://www.owasp.org/index.php/Top_10_2013-A10-Unvalidated_Redirects_and_Forwards\",\"https://blog.gdssecurity.com/labs/2011/9/9/net-servertransfer-vs-responseredirect-reiterating-a-securit.html\"],\"free\":false,\"name\":\"unvalidated-forward\"},{\"description\":\"Verifies that the application\\u0027s authorization rules do not include an allow all user rule before a deny rule.\",\"title\":\"Authorization Rules Misordered\",\"category\":\"Access Control\",\"impact\":\"Medium\",\"likelihood\":\"Medium\",\"enabled\":\"true\",\"cwe\":\"https://cwe.mitre.org/data/definitions/284.html\",\"owasp\":\"https://www.owasp.org/index.php/Top_10_2013-A5-Security_Misconfiguration\",\"confidence\":\"Medium\",\"severity\":\"Medium\",\"serviceLevel\":\"Enterprise\",\"references\":[\"http://msdn.microsoft.com/en-us/library/system.web.configuration.authorizationsection.aspx\"],\"free\":false,\"name\":\"authorization-rules-misordered\"},{\"description\":\"Verifies that the application\\u0027s authorization rules include a deny rule.\",\"title\":\"Authorization Rules Missing Deny Rule\",\"category\":\"Access Control\",\"impact\":\"Medium\",\"likelihood\":\"Medium\",\"enabled\":\"true\",\"cwe\":\"http://cwe.mitre.org/data/definitions/294.html\",\"owasp\":\"https://www.owasp.org/index.php/Top_10_2013-A5-Security_Misconfiguration\",\"confidence\":\"Medium\",\"severity\":\"Medium\",\"serviceLevel\":\"Enterprise\",\"references\":[\"http://msdn.microsoft.com/en-us/library/system.web.configuration.authorizationsection.aspx\"],\"free\":false,\"name\":\"authorization-missing-deny\"}]}";

    Rules rules = gson.fromJson(rulesString, Rules.class);

    assertThat(rules).isNotNull();
    assertThat(rules.getRules()).isNotNull();
  }

  @Test
  public void getServers() throws UnauthorizedException, IOException, ResourceNotFoundException {

    String serversString =
        "{\"servers\":[{\"server_id\":2,\"name\":\"test_name\",\"hostname\":\"test_hostname\",\"last_startup\":1464286620000,\"last_trace_received\":0,\"last_activity\":1464286980000,\"num_apps\":0,\"path\":\"/Users/test/\",\"status\":\"ONLINE\",\"type\":\"Apache Tomcat 7.0.65\",\"agent_version\":\"3.2.8\",\"assess\":true,\"assessPending\":false,\"defend\":false,\"defendPending\":false,\"container\":\"tomcat\",\"environment\":\"DEVELOPMENT\",\"logEnhancerPending\":false,\"logLevel\":\"ERROR\",\"noPending\":true,\"assessSensors\":false,\"assess_last_update\":1464188870000},{\"server_id\":1,\"name\":\"test_name_2\",\"hostname\":\"test_host_name_2\",\"last_startup\":1464189060000,\"last_trace_received\":0,\"last_activity\":1464189120000,\"num_apps\":0,\"path\":\"/Users/justinleo/Jar-WebGoat/\",\"status\":\"OFFLINE\",\"type\":\"Apache Tomcat 7.0.59\",\"agent_version\":\"3.2.8\",\"assess\":true,\"assessPending\":false,\"defend\":false,\"defendPending\":false,\"container\":\"other\",\"environment\":1,\"logEnhancerPending\":false,\"logLevel\":\"ERROR\",\"noPending\":true,\"assessSensors\":false,\"assess_last_update\":1463779837000}]}";

    Servers servers = gson.fromJson(serversString, Servers.class);

    assertThat(servers).isNotNull();
    assertThat(servers.getServers()).isNotNull();
  }

  @Test
  public void testMakeSecurityCheck() {
    String securityCheckResponseString =
        "{'security_check':{'id':1,'application_name':'testName','application_id':'testId1','origin':'JENKINS','result':false, 'job_outcome_policy':{'name':'testPolicy','outcome':'UNSTABLE','severities':{'MEDIUM':1}}}}";

    SecurityCheckResponse response =
        gson.fromJson(securityCheckResponseString, SecurityCheckResponse.class);
    SecurityCheck securityCheck = response.getSecurityCheck();
    JobOutcomePolicy jobOutcomePolicy = securityCheck.getJobOutcomePolicy();
    assertThat(securityCheck.getId().longValue()).isEqualTo(1l);
    assertThat(securityCheck.getApplicationName()).isEqualTo("testName");
    assertThat(jobOutcomePolicy.getName()).isEqualTo("testPolicy");
    assertThat(jobOutcomePolicy.getOutcome()).isEqualTo(JobOutcomePolicy.Outcome.UNSTABLE);
    assertThat(jobOutcomePolicy.getSeverities().size()).isEqualTo(1);
    assertThat(jobOutcomePolicy.getSeverities().get(RuleSeverity.MEDIUM).longValue()).isEqualTo(1l);
  }

  @Test
  public void testGetEnabledJobOutcomePolicies() {
    String jobOutcomePolicyListResponseString =
        "{'policies':[{'name':'testJobOutcomePolicy','outcome':'SUCCESS'}]}";
    JobOutcomePolicyListResponse response =
        gson.fromJson(jobOutcomePolicyListResponseString, JobOutcomePolicyListResponse.class);
    assertThat(response.getPolicies()).isNotNull();
    assertThat("testJobOutcomePolicy").isEqualTo(response.getPolicies().get(0).getName());
  }

  @Test
  public void testGetEnabledJobOutcomePoliciesByApplication() {
    String jobOutcomePolicyListResponseString =
        "{'policies':[{'name':'testJobOutcomePolicy','outcome':'UNSTABLE'}]}";
    JobOutcomePolicyListResponse response =
        gson.fromJson(jobOutcomePolicyListResponseString, JobOutcomePolicyListResponse.class);
    assertThat(response.getPolicies()).isNotNull();
    assertThat("testJobOutcomePolicy").isEqualTo(response.getPolicies().get(0).getName());
  }

  @Test
  public void setCustomTiemouts() throws IOException {
    final int connectionTimeout = 1000;
    final int readTimeout = 4000;
    contrastSDK.setConnectionTimeout(connectionTimeout);
    contrastSDK.setReadTimeout(readTimeout);

    URLConnection conn =
        contrastSDK.makeConnection("https://www.google.com", HttpMethod.GET.toString());

    assertThat(conn.getConnectTimeout()).isEqualTo(connectionTimeout);
    assertThat(conn.getReadTimeout()).isEqualTo(readTimeout);
  }

  @Test
  public void negativeTimeoutsAreNotSetTest() throws IOException {
    final int connectionTimeout = -10;
    final int readTimeout = -50;

    contrastSDK.setConnectionTimeout(connectionTimeout);
    contrastSDK.setReadTimeout(readTimeout);

    URLConnection conn =
        contrastSDK.makeConnection("https://www.google.com", HttpMethod.GET.toString());

    assertThat(conn.getConnectTimeout()).isNotEqualTo(connectionTimeout);
    assertThat(conn.getReadTimeout()).isNotEqualTo(readTimeout);
  }

  @Test
  public void testEnsureApi() throws IOException {
    final String expectedApi = "http://localhost:19080/Contrast/api";
    final String ensureUrlOne = ContrastSDKUtils.ensureApi("http://localhost:19080/Contrast/");
    final String ensureUrlTwo = ContrastSDKUtils.ensureApi("http://localhost:19080/Contrast");

    assertThat(ensureUrlOne).isEqualTo(expectedApi);
    assertThat(ensureUrlTwo).isEqualTo(expectedApi);

    final String unchangedApi = "http://localhost:19080/";
    final String ensureUnchanged = ContrastSDKUtils.ensureApi(unchangedApi);

    assertThat(ensureUnchanged).isEqualTo(unchangedApi);
  }

  @Test
  public void testMalformedURL() throws IOException {
    final String expectedUrl = "htp:/localhost:19080/Contrast/api";
    final String badUrl = "htp:/localhost:19080/Contrast/";
    final String actualUrl = ContrastSDKUtils.ensureApi(badUrl);
    assertThat(expectedUrl).isEqualTo(actualUrl);
  }

  @Test
  public void testNullUrl() throws IOException {
    final String nullUrl = ContrastSDKUtils.ensureApi(null);
    assertThat(nullUrl).isNull();
  }

  @Test
  public void blankUrl() {
    final String blankUrl = ContrastSDKUtils.ensureApi("");
    final String ensureBlank = "";
    assertThat(blankUrl).isEqualTo(ensureBlank);
  }

  @Test
  public void testApplicationYearlyTrend() {
    final String exampleApplicationYearlyTrend =
        "{  \"success\": true,  \"messages\": [    \"Total Vulnerability trend loaded successfully\"  ],  \"open\": [    {      \"timestamp\": 1567310400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1569902400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1572580800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1575176400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1577854800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1580533200000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1583038800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1585713600000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1588305600000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 0        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1590984000000,      \"count\": 9,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 9        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1593576000000,      \"count\": 8,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 8        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1596254400000,      \"count\": 8,      \"statusBreakdown\": [        {          \"name\": \"Reported\",          \"value\": 8        },        {          \"name\": \"Suspicious\",          \"value\": 0        },        {          \"name\": \"Confirmed\",          \"value\": 0        }      ]    }  ],  \"closed\": [    {      \"timestamp\": 1567310400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1569902400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1572580800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1575176400000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1577854800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1580533200000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1583038800000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1585713600000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1588305600000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1590984000000,      \"count\": 0,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 0        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1593576000000,      \"count\": 1,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 1        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    },    {      \"timestamp\": 1596254400000,      \"count\": 1,      \"statusBreakdown\": [        {          \"name\": \"NotAProblem\",          \"value\": 1        },        {          \"name\": \"Remediated\",          \"value\": 0        },        {          \"name\": \"Fixed\",          \"value\": 0        },        {          \"name\": \"AutoRemediated\",          \"value\": 0        }      ]    }  ]}";
    VulnerabilityTrend vulnerabilityTrend =
        gson.fromJson(exampleApplicationYearlyTrend, VulnerabilityTrend.class);
    assertThat(vulnerabilityTrend).isNotNull();
    assertThat(vulnerabilityTrend.getOpenTrend()).isNotNull();
    assertThat(vulnerabilityTrend.getClosedTrend()).isNotNull();
  }

  @Test
  public void testRecommendation() {
    final String recommendation =
        "{ \"success\" : true, \"messages\" : [ \"Vulnerability recommendation loaded successfully\" ], \"recommendation\" : { \"text\" : \"test recommendation\", \"formattedTextVariables\" : { } }, \"owasp\" : \"OWASP link\", \"cwe\" : \"CWE link\", \"custom_recommendation\" : { \"text\" : \"custom recommendation\", \"formattedText\" : \"custom recommendation\", \"formattedTextVariables\" : { } }, \"rule_references\" : { \"text\" : \"rule reference\", \"formattedText\" : \"rule reference\", \"formattedTextVariables\" : { } }, \"custom_rule_references\" : { \"text\" : \"test custom\", \"formattedText\" : \"test custom\", \"formattedTextVariables\" : { } } }";
    RecommendationResponse vulnerabilityRecommendation =
        gson.fromJson(recommendation, RecommendationResponse.class);
    assertThat(vulnerabilityRecommendation).isNotNull();

    assertThat("test recommendation")
        .isEqualTo(vulnerabilityRecommendation.getRecommendation().getText());
    assertThat("custom recommendation")
        .isEqualTo(vulnerabilityRecommendation.getCustomRecommendation().getText());
    assertThat("test custom")
        .isEqualTo(vulnerabilityRecommendation.getCustomRuleReferences().getText());
    assertThat("CWE link").isEqualTo(vulnerabilityRecommendation.getCwe());
    assertThat("OWASP link").isEqualTo(vulnerabilityRecommendation.getOwasp());
    assertThat("rule reference")
        .isEqualTo(vulnerabilityRecommendation.getRuleReferences().getText());
  }

  @Test
  public void testStory() {
    final String storyString =
        "{ \"success\" : true, \"messages\" : [ \"Vulnerability story loaded successfully\" ], \"story\" : { \"traceId\" : \"testID\", \"chapters\" : [ { \"type\" : \"source\", \"introText\" : \"test intro\", \"introTextFormat\" : \"test intro formatted\", \"introTextVariables\" : { \"source0\" : \"\\Test variable 1\" }, \"body\" : \"test body 1\", \"bodyFormat\" : \"test body formatted\", \"bodyFormatVariables\" : { \"paramNameKey1\" : \"test param 1\", \"paramValueKey1\" : \"test param 2\", \"urlVariableKey\" : \"test param 3\" } }, { \"type\" : \"location\", \"introText\" : \"intro text 2\", \"introTextFormat\" : \"intro text 2 formatted\", \"introTextVariables\" : { }, \"body\" : \"test body 2\", \"bodyFormat\" : \"test body 2 formatted\", \"bodyFormatVariables\" : { \"line\" : \"test line number\", \"methodName\" : \"test method name\", \"className\" : \"test class name\" } }, { \"type\" : \"dataflow\", \"introText\" : \"test chapter intro text\", \"introTextFormat\" : \"test chapter intro text formatted\", \"introTextVariables\" : { }, \"body\" : \"test body 3\", \"bodyFormat\" : \"test body 3 formatted\", \"bodyFormatVariables\" : { \"untrustedTaintedKey0\" : \"variable 3\" }, \"vector\" : null } ], \"risk\" : { \"text\" : \"test risk\", \"formattedTextVariables\" : { } } }, \"custom_risk\" : { \"test custom risk\" : \"\", \"formattedText\" : \"\", \"formattedTextVariables\" : { } } }";
    StoryResponse storyResponse = gson.fromJson(storyString, StoryResponse.class);
    Story story = storyResponse.getStory();
    assertThat(story).isNotNull();

    List<Chapter> chapters = story.getChapters();

    assertThat(chapters.isEmpty()).isFalse();
    chapters.forEach(
        chapter -> {
          int index = Integer.parseInt(String.valueOf(chapters.indexOf(chapter))) + 1;
          assertThat("test body " + index).isEqualTo(chapter.getBody());
        });
  }

  @Test
  public void testTags() {
    final String tagsString =
        "{ \"success\" : true, \"messages\" : [ \"Tags for vulnerability loaded successfully\" ], \"tags\" : [ \"tag1\", \"tag2\", \"tag3\", \"tag4\" ] }";
    TagsResponse tags = gson.fromJson(tagsString, TagsResponse.class);

    assertThat(tags.getTags().isEmpty()).isFalse();

    Tags tagsObject = new Tags(tags.getTags());

    assertThat(tagsObject.getTags().isEmpty()).isFalse();
  }

  @Test
  public void testHttpRequest() {
    final String httpRequest =
        "{ \"success\" : true, \"messages\" : [ \"Vulnerability HTTP Request loaded successfully\" ], \"http_request\" : { \"text\" : \"test request method\", \"formattedText\" : \"test request method formatted\", \"formattedTextVariables\" : { \"headerNameKey6\" : \"Host\"  } } }";
    HttpRequestResponse http = gson.fromJson(httpRequest, HttpRequestResponse.class);

    assertThat(http.getHttpRequest()).isNotNull();

    assertThat("test request method").isEqualTo(http.getHttpRequest().getText());
  }

  @Test
  public void testEventSummary() {
    final String eventString =
        "{\"success\" : true,\"messages\" : [ \"Vulnerability events summary loaded successfully\" ], \"risk\" : \"test risk\", \"showEvidence\" : false,\"showEvents\" : true,\"events\" : [ { \"id\" : \"612\", \"important\" : true,\"type\" : \"Creation\",\"description\" : \"Test description 1\",\"extraDetails\" : null, \"codeView\" : { \"lines\" : [ {\"fragments\" : [ { \"type\" : \"NORMAL_CODE\", \"value\" : \"code\" }, {\"type\" : \"CODE_STRING\",\"value\" : \"code\"}, {\"type\" : \"NORMAL_CODE\",\"value\" : \"code\"} ],\"text\" : \"code\" } ],\"nested\" : false},\"probableStartLocationView\" : { \"lines\" : [ { \"fragments\" : [ {\"type\" : \"STACKTRACE_LINE\",\"value\" : \"code\"} ],\"text\" : \"code\"} ],\"nested\" : false},\"dataView\" : {\"lines\" : [ {\"fragments\" : [ { \"type\" : \"TEXT\", \"value\" : \"code\" }, {\"type\" : \"TAINT_VALUE\",\"value\" : \"code\"} ], \"text\" : \"code\"} ],\"nested\" : false},\"collapsedEvents\" : [ ], \"dupes\" : 0}, { \"id\" : \"613\",\"important\" : true,\"type\" : \"Trigger\",\"description\" : \"Test description 2\",\"extraDetails\" : null,\"codeView\" : {\"lines\" : [ {\"fragments\" : [ { \"type\" : \"NORMAL_CODE\",\"value\" : \"code\"}, {\"type\" : \"CODE_STRING\",\"value\" : \"code\"}, {\"type\" : \"NORMAL_CODE\",\"value\" : \")\"} ],\"text\" : \"code\"} ],\"nested\" : false},\"probableStartLocationView\" : {\"lines\" : [ { \"fragments\" : [ {\"type\" : \"STACKTRACE_LINE\",\"value\" : \"code\"} ],\"text\" : \"code\"} ],\"nested\" : false},\"dataView\" : { \"lines\" : [ {\"fragments\" : [ {\"type\" : \"TAINT_VALUE\",\"value\" : \"code\"} ],\"text\" : \"code\"} ], \"nested\" : false}, \"collapsedEvents\" : [ ], \"dupes\" : 0} ]}";
    EventSummaryResponse event = gson.fromJson(eventString, EventSummaryResponse.class);
    assertThat(event).isNotNull();

    List<EventResource> eventResources = event.getEvents();
    assertThat(eventResources.isEmpty()).isFalse();

    assertThat("test risk").isEqualTo(event.getRisk());

    assertThat(event.getShowEvents()).isTrue();
    assertThat(event.getShowEvidence()).isFalse();
  }

  @Test
  public void testGetTraceMetadataFilters() {

    String traceMetadataString =
        "{\"count\":1,\"traces\":[{\"title\":\"foo\",\"language\":\"Java\",\"status\":\"Reported\",\"uuid\":\"bar\",\"rule_name\":\"baz\",\"severity\":\"Medium\",\"likelihood\":\"High\",\"impact\":\"Low\",\"confidence\":\"High\",\"first_time_seen\":100000000,\"last_time_seen\":100000000,\"category\":\"Secure Communications\",\"platform\":\"foo\",\"total_traces_received\":5,\"visible\":true}]}";

    Traces traces = gson.fromJson(traceMetadataString, Traces.class);

    assertThat(traces).isNotNull();
    assertThat(traces.getTraces()).isNotNull();

    assertThat(traces.getCount() > 0).isTrue();
  }
}
