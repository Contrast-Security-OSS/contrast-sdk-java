package com.contrastsecurity;

import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.models.Group;
import com.contrastsecurity.models.Groups;
import com.contrastsecurity.models.User;
import com.contrastsecurity.sdk.ContrastSDK;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public class ddooleyTester {

    private String username;
    private String url;
    private String apiKey;
    private String serviceKey;

    /* ddooley, reuse MAP code in CSI */
    private HashMap<String, Long> groupMap = new HashMap<>();
    public void logMap(){
        System.out.println("Printing group Mappings ...");
        for (String name: groupMap.keySet()) {
            System.out.println("Printing key " + name);
            System.out.println("Printing value " + groupMap.get(name).toString());
        }
    }
    public HashMap<String, Long> getGroupMap() {
        return groupMap;
    }
    public void setGroupMap(HashMap<String, Long> groupMap) {
        this.groupMap = groupMap;
    }

    public static void main(String[] args) {
        System.out.println("Testing SDK calls...");

        ddooleyTester tester = new ddooleyTester();
        tester.readProperties();

        // Testing Group Creation
        //test
        //String name = "UHGWM110-000008_Secure_Developer";
        //String role = "edit";
        //ArrayList<String> users = new ArrayList<>();
        //String id = "no_user@contrast.com";
        //users.add(id);
        //String name = "No Access Group";
        //String role = "no_access";
        //tester.createGroup(name, role, users);

        // Testing User Creation
        //String id = "david.api@contrast.com";
        //ArrayList<Long> groupIds = new ArrayList<Long>(Arrays.asList(805L, 806L));
        //ArrayList<Long> groupIds = new ArrayList<Long>();
        //long orgRole = 3; //edit
        //tester.createUser(id, orgRole, groupIds);

        // Testing Group Update i.e. add User to group
        //users.add(id);
        //tester.updateGroup(name, role, users, 892);
        //tester.buildGroupMap();
        //tester.logMap();
        //tester.reBuildGroup();

        // Test Update User
        //String id = "jb@optum.com";
        //ArrayList<Long> groupIds = new ArrayList<Long>();
        //groupIds.add(903L);
        //tester.updateUser(id, "Jack", "Black", 4, groupIds);
        //tester.lockUser(id, true);

        String id = "vlad_m_palchuck@optum.com";
        tester.getUser(id);
        System.out.println("Testing Complete ...");

    }
    public void buildGroupMap(){
        ContrastSDK contrastSDK = new ContrastSDK(username, serviceKey, apiKey, url);
        try {
            System.out.println("Build up initial Group Map ...");
            Groups organizationGroups = contrastSDK.getOrganizationGroups("ac0ec4e6-f84a-46b5-8534-7a3a14632bf8");
            List<Group> groups = organizationGroups.getGroups();
            for (Group group : groups)  {
                Long groupID = new Long(group.getID());
                String groupName = group.getName();
                groupMap.put(groupName, groupID);
            }
        } catch (UnauthorizedException e) {
            System.out.println("Unable to connect to TeamServer. Printing stacktrace ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to create the user. Printing stacktrace ");
            e.printStackTrace();
        }
    }
    public void reBuildGroup(){
        groupMap.clear();
        logMap();
        buildGroupMap();
        logMap();
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
    private void updateGroup(String name, String appRole, ArrayList<String> users, int group) {
        ContrastSDK contrastSDK = new ContrastSDK(username, serviceKey, apiKey, url);
        try {
            System.out.println("Testing Update Group");
            int returnCode;
            returnCode = contrastSDK.updateGroup("ac0ec4e6-f84a-46b5-8534-7a3a14632bf8", name, appRole, users, group);
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
    private void getUser(String id) {
        ContrastSDK contrastSDK = new ContrastSDK(username, serviceKey, apiKey, url);
        try {
            System.out.println("Testing Getting User");
            User user = contrastSDK.getOrganizationUser("ac0ec4e6-f84a-46b5-8534-7a3a14632bf8", id);
            List<Group> groups = user.getGroups();
            for (Group group:groups) {
                System.out.println("Group ID " + group.getID() + " Group name " + group.getName());
            }
        } catch (UnauthorizedException e) {
            System.out.println("Unable to connect to TeamServer. Printing stacktrace ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to create the user. Printing stacktrace ");
            e.printStackTrace();
        }
    }
    private void updateUser(String id, String fname, String lname, long orgRole, ArrayList<Long> groupIds) {
        ContrastSDK contrastSDK = new ContrastSDK(username, serviceKey, apiKey, url);
        try {
            System.out.println("Testing Update User");
            int returnCode;
            returnCode = contrastSDK.updateUser("ac0ec4e6-f84a-46b5-8534-7a3a14632bf8", id, fname, lname, orgRole, groupIds);
            System.out.println("Return code is : " + returnCode);
        } catch (UnauthorizedException e) {
            System.out.println("Unable to connect to TeamServer. Printing stacktrace ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to update the user. Printing stacktrace ");
            e.printStackTrace();
        }
    }
    private void lockUser(String id, boolean lock) {
        ContrastSDK contrastSDK = new ContrastSDK(username, serviceKey, apiKey, url);
        try {
            System.out.println("Testing Lock User");
            int returnCode;
            returnCode = contrastSDK.lockUser("ac0ec4e6-f84a-46b5-8534-7a3a14632bf8", id, lock);
            System.out.println("Return code is : " + returnCode);
        } catch (UnauthorizedException e) {
            System.out.println("Unable to connect to TeamServer. Printing stacktrace ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to update the user. Printing stacktrace ");
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
