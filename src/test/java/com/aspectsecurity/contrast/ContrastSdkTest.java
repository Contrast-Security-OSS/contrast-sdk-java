package com.aspectsecurity.contrast;

import com.contrastsecurity.models.Applications;
import com.contrastsecurity.sdk.ContrastSDK;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContrastSDKTest {

    private String response;
    private ContrastSDK contrastSDK;

    @BeforeClass
    public void setUp() {
        contrastSDK = new ContrastSDK("user", "apiKey", "serviceKey", "localhost");
        response = "";
    }

    @Test
    public void testGetApplication() throws IOException {

        Applications apps = new Applications();
        // set fields here

        final HttpURLConnection mockCon = mock(HttpURLConnection. class);
        InputStream inputStream = IOUtils.toInputStream(response);
        when(mockCon.getInputStream()).thenReturn( inputStream);

        /*
        try {
            when(contrastSDK.makeSimpleRequest(anyString(), anyString()));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        }*/

        URLStreamHandler stubURLStreamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u ) throws IOException {
                return mockCon ;
            }
        };

        URL url = new URL(null,"https://www.google.com", stubURLStreamHandler);

        Applications actual = new Applications();
        try {
            actual = contrastSDK.getApplication("org", "app");
        } catch (Exception e ) {
            e.printStackTrace();
        }

        assertEquals(response, actual);

    }
}
