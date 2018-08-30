package com.contrastsecurity;

import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.sdk.ContrastSDK;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;


public class ddooleyTester {

    private String username;
    private String url;
    private String apiKey;
    private String serviceKey;

    public static void main(String[] args) {
        System.out.println("Testing SDK calls...");

        ddooleyTester tester = new ddooleyTester();
        tester.readProperties();

        // Testing Group Creation
        String name = "UHGWM110-000007_Secure_Developer";
        String role = "edit";
        ArrayList<String> users = new ArrayList<>();
        tester.createGroup(name, role, users);

        // Testing User Creation
        String id = "david.api@contrast.com";
        ArrayList<Long> groupIds = new ArrayList<Long>(Arrays.asList(805L, 806L));
        long orgRole = 3; //edit

        //tester.createUser(id, orgRole, groupIds);
        System.out.println("Testing Complete ...");
    }

    private void createGroup(String name, String appRole, ArrayList<String> users) {
        ContrastSDK contrastSDK = new ContrastSDK(username, serviceKey, apiKey, url);
        try {
            System.out.println("Testing Create Group");
            int returnCode;
            returnCode = contrastSDK.createGroup("ac0ec4e6-f84a-46b5-8534-7a3a14632bf8", name, appRole, users);
            System.out.println("Return code is : " + returnCode);
        } catch (UnauthorizedException e) {
            System.out.println("Unable to connect to TeamServer. Printing stacktrace ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to create the user. Printing stacktrace ");
            e.printStackTrace();
        }
    }
    private void createUser(String id, long orgRole, ArrayList<Long> groupIds) {
        ContrastSDK contrastSDK = new ContrastSDK(username, serviceKey, apiKey, url);
        try {
            System.out.println("Testing Create User");
            int returnCode;
            returnCode = contrastSDK.createUser("ac0ec4e6-f84a-46b5-8534-7a3a14632bf8", id, "jimmy", "jackson", orgRole, groupIds);
            System.out.println("Return code is : " + returnCode);
        } catch (UnauthorizedException e) {
            System.out.println("Unable to connect to TeamServer. Printing stacktrace ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to create the user. Printing stacktrace ");
            e.printStackTrace();
        }
    }

    public void readProperties() {
        System.out.println("Reading in the feeds.properties file...");
        InputStream input = null;
        Properties prop = new Properties();
        try {
            input = new FileInputStream("csi.properties");
            // load a properties file
            prop.load(input);
            // get the property values
            this.setUsername(prop.getProperty("username"));
            this.setUrl(prop.getProperty("url"));
            this.setApiKey(prop.getProperty("apiKey"));
            this.setServiceKey(prop.getProperty("serviceKey"));

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    public String getServiceKey() {
        return serviceKey;
    }
    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }
}
