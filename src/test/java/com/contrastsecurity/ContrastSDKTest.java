package com.contrastsecurity;

import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.models.Applications;
import com.contrastsecurity.models.Traces;
import com.contrastsecurity.sdk.ContrastSDK;
import com.google.gson.Gson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ContrastSDKTest extends ContrastSDK {

    private static ContrastSDK contrastSDK;
    private static Gson gson;

    @BeforeClass
    public static void setUp() {
        contrastSDK = new ContrastSDK("contrast_admin", "demo", "demo", "http://localhost:19080/Contrast/api");
        gson = new Gson();
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
    public void testGetTraces() throws UnauthorizedException, IOException, ResourceNotFoundException {

        String tracesString = "{\"count\":2,\"traces\":[{\"title\":\"Application Disables \\u0027secure\\u0027 Flag on Cookies observed at DefaultSavedRequest.java\",\"language\":\"Java\",\"status\":\"Reported\",\"uuid\":\"KNBG-XTO9-ED1O-PG2X\",\"rule_name\":\"cookie-flags-missing\",\"severity\":\"Medium\",\"likelihood\":\"High\",\"impact\":\"Low\",\"confidence\":\"High\",\"first_time_seen\":1461600923859,\"last_time_seen\":1461601039100,\"category\":\"Secure Communications\",\"platform\":\"Oracle Corporation\",\"total_traces_received\":5,\"visible\":true},{\"title\":\"Insecure Authentication Protocol\",\"evidence\":\"Authorization: Basic Z3Vl...Q6Z3Vlc3Q\\u003d\",\"language\":\"Java\",\"status\":\"Reported\",\"uuid\":\"IJ92-WGDU-JCY4-F4EZ\",\"rule_name\":\"insecure-auth-protocol\",\"severity\":\"Medium\",\"likelihood\":\"Medium\",\"impact\":\"Medium\",\"confidence\":\"High\",\"first_time_seen\":1461600923853,\"last_time_seen\":1461601039100,\"category\":\"Access Control\",\"platform\":\"Oracle Corporation\",\"total_traces_received\":5,\"visible\":true}]}";

        Traces traces = gson.fromJson(tracesString, Traces.class);

        assertNotNull(traces);
        assertNotNull(traces.getTraces());

        assertTrue(traces.getCount() > 0);
    }

}
