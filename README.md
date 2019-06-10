# Contrast TeamServer Java SDK
This SDK gives you a quick start for programmatically accessing the [Contrast REST API](https://docs.contrastsecurity.com/tools-about.html#api-about) using Java.

### Requirements
* JDK 7/8
* Maven
* TeamServer account

### How to use this SDK
1. Clone the project
1. 'mvn clean install' Maven goals to build the artifact with the latest code
1. Add contrast-sdk-java to your project dependency:

    ``` 
    <dependency>
        <groupId>com.contrastsecurity</groupId>
        <artifactId>contrast-sdk-java</artifactId>
    </dependency>
    ```
1. Look up the following information from Contrast under "Your Account"
    * Username
    * Service Key
    * API Key
    * Contrast REST API URL
        * e.g., http://localhost:19080/Contrast/api

Code example:
```
ContrastSDK contrastSDK = new ContrastSDK("test@test.com", "testServiceKey", "testApiKey", "https://apptwo.contrastsecurity.com/Contrast/api");

String orgUuid = contrastSDK.getProfileDefaultOrganizations().getOrganization().getOrgUuid();

Applications apps = contrastSDK.getApplications(orgUuid);
for (Application app : apps.getApplications()) {
    System.out.println(app.getName() + " (" + app.getCodeShorthand() + " LOC)");
}
```

Sample output:
```
Aneritx (48K LOC)
Default Web Site (0k LOC)
EnterpriseTPS (48K LOC)
Feynmann (48K LOC)
jhipster-sample (0k LOC)
JSPWiki (48K LOC)
Liferay (48K LOC)
OpenMRS (65K LOC)
OracleFS (48K LOC)
Security Test (< 1K LOC)
Ticketbook (2K LOC)
WebGoat (48K LOC)
WebGoat7 (106K LOC)
```