# Contrast Java SDK

[![javadoc](https://javadoc.io/badge2/com.contrastsecurity/contrast-sdk-java/javadoc.svg)](https://javadoc.io/doc/com.contrastsecurity/contrast-sdk-java)


This SDK gives you a quick start for programmatically accessing the [Contrast REST API](https://api.contrastsecurity.com/) using Java.


## Requirements

* JDK 1.8
* Contrast Account


## How to use this SDK

1. Add the
   [contrast-sdk-java](https://search.maven.org/artifact/com.contrastsecurity/contrast-sdk-java)
   dependency from Maven Central to your project. For example:
    ```xml
    <dependency>
        <groupId>com.contrastsecurity</groupId>
        <artifactId>contrast-sdk-java</artifactId>
        <verison>3.0.0</version>
    </dependency>
    ```
1. At a minimum, you will need to supply four basic connection parameters ([find them here](https://docs.contrastsecurity.com/en/personal-keys.html)):
   * Username
   * API Key
   * Service Key
   * Contrast REST API URL (e.g. https://app.contrastsecurity.com/Contrast/api)


## Example

```java
ContrastSDK contrastSDK = new ContrastSDK.Builder("contrast_admin", "demo", "demo")
        .withApiUrl("http://localhost:19080/Contrast/api")
        .build();

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


## Building

Requires JDK 11 to build

Use `./mvnw verify` to build and test changes to the project


### Formatting

To avoid distracting white space changes in pull requests and wasteful bickering
about format preferences, Contrast uses the google-java-format opinionated Java
code formatter to automatically format all code to a common specification.

Developers are expected to configure their editors to automatically apply this
format (plugins exist for both IDEA and Eclipse). Alternatively, developers can
apply the formatting before committing changes using the Maven plugin:

```shell
./mvnw spotless:apply
```
