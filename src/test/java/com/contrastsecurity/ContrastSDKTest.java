package com.contrastsecurity;

import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.models.Applications;
import com.contrastsecurity.sdk.ContrastSDK;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ContrastSDKTest extends ContrastSDK {

    private static ContrastSDK contrastSDK;

    @BeforeClass
    public static void setUp() {
        contrastSDK = new ContrastSDK("contrast_admin", "demo", "demo", "http://localhost:19080/Contrast/api");
    }

    // @Test
    public void testGetApplication() throws UnauthorizedException, IOException, ResourceNotFoundException {

        Applications app = contrastSDK.getApplication("test-org", "test-app");

        assertNotNull(app);
    }

}
