## Multi-Module Projects

Best practices for a multi-module project would have users configure the contrast-maven-plugin with
Contrast connection configuration in the parent POM's `<pluginManagement>` section. Doing so allows
this configuration to be reused in each child module that uses the contrast-maven-plugin.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns="http://maven.apache.org/POM/4.0.0"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.contrastsecurity</groupId>
  <artifactId>multi-module-example-parent</artifactId>
  <version>0.0.1</version>
  <type>pom</type>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>com.contrastsecurity</groupId>
          <artifactId>contrast-maven-plugin</artifactId>
          <version><!-- insert version here --></version>
          <configuration>
            <apiUrl>${env.CONTRAST__API__URL}</apiUrl>
            <username>${env.CONTRAST__API__USER_NAME}</username>
            <apiKey>${env.CONTRAST__API__API_KEY}</apiKey>
            <serviceKey>${env.CONTRAST__API__SERVICE_KEY}</serviceKey>
            <orgUuid>${env.CONTRAST__API__ORGANIZATION_ID}</orgUuid>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>
```

In child modules that use contrast-maven-plugin's goals, users may simply include the goal in
their `<build><plugins>` section with any module specific configuration:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns="http://maven.apache.org/POM/4.0.0"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.contrastsecurity</groupId>
  <artifactId>multi-module-example-child</artifactId>
  <version>0.0.1</version>
  <type>war</type>

  <build>
    <plugins>
      <plugin>
        <groupId>com.contrastsecurity</groupId>
        <artifactId>contrast-maven-plugin</artifactId>
        <executions>
          <execution>
            <goals>
              <goal>scan</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```