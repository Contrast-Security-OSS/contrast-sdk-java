package com.contrastsecurity.sdk;

import com.contrastsecurity.sdk.application.App;
import com.contrastsecurity.sdk.application.Apps;
import com.contrastsecurity.sdk.http.ContrastRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class TestContrastConnector {

    @Mock
    private ContrastConnector contrast;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAppList() throws Exception {
        Apps apps = new Apps();
        UUID appID = UUID.randomUUID();
        apps.setApps(Arrays.asList(new App(appID.toString(), "Test License", "/testapp", "Test Application")));
        when(contrast.sendRequest(any(ContrastRequest.class), anyString())).thenReturn(apps);

        contrast.getAppList();

        verify(contrast).getAppList();
    }
}
