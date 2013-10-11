package com.contrastsecurity.sdk;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestContrastConnector {

    @Mock
    private ContrastConnector contrast;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void mockGetConnection() {
    }

    @Test
    public void testGetAppList() throws Exception {
        when(contrast.getAppList()).thenCallRealMethod();
        contrast.getAppList();
    }
}
