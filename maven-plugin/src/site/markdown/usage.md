## Usage

The Contrast Maven Plugin helps users include one or more Contrast Security analysis features in
their Maven projects. All such analysis must connect to Contrast, and therefore each plugin goal
requires Contrast connection parameters. These connection parameters may be externalized to
environment variables:

```xml
<pluginManagement>
  <plugins>
    <plugin>
      <groupId>com.contrastsecurity</groupId>
      <artifactId>contrast-maven-plugin</artifactId>
      <configuration>
        <username>${env.CONTRAST__API__USER_NAME}</username>
        <apiKey>${env.CONTRAST__API__API_KEY}</apiKey>
        <serviceKey>${env.CONTRAST__API__SERVICE_KEY}</serviceKey>
        <orgUuid>${env.CONTRAST__API__ORGANIZATION_ID}</orgUuid>
      </configuration>
    </plugin>
  </plugins>
</pluginManagement>
```

### Contrast Assess

[Contrast Assess](https://docs.contrastsecurity.com/en/assess.html) is an application security
testing tool that combines Static (SAST), Dynamic (DAST), and Interactive Application Security
Testing (IAST) approaches, to provide highly accurate and continuous information on security
vulnerabilities in your applications.

To add Contrast Assess analysis in your Java web application testing, you must attach Contrast Java
agent to your application. The `install` goal retrieves the Contrast Java agent and stores it
in `${project.build.directory}/contrast.jar`.

The `install` goal automatically detects and configures
the [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/test-mojo.html)
, [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/integration-test-mojo.html)
,
and [Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/)
to include the Contrast Java agent in their `test`, `integration-test`, and `run` goals
respectively.

Users may include the `verify` goal to fail their Maven build if Contrast Assess detects
vulnerabilities during the project's integration testing phase that violate the project's security
policies.

The following example includes the Contrast Java agent in the project's integration testing and
fails the build if any "Medium" vulnerabilities are detected during testing

```xml
<profile>
  <id>assess</id>
  <build>
    <plugins>
      <plugin>
        <groupId>com.contrastsecurity</groupId>
        <artifactId>contrast-maven-plugin</artifactId>
        <executions>
          <execution>
            <goals>
              <goal>install</goal>
            </goals>
          </execution>
          <execution>
            <goals>
              <goal>verify</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</profile>
```

To run the integration tests with security analysis and verification, execute the `verify` lifecycle
phase with this profile activated

```shell
mvn verify -Passess
```


### Contrast Scan

Contrast Scan is a static application security testing (SAST) tool that makes it easy for you to
find and remediate vulnerabilities in your Java web applications.

Use the `scan` goal to find vulnerabilities in your Java web application Maven project using
Contrast Scan. The goal uploads your application package to Contrast Scan for analysis.

The following example includes the `scan` goal in a `scan` profile

```xml
<profile>
  <id>scan</id>
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
</profile>
```

To run the scan, execute Maven with the scan profile activated

```shell
mvn verify -Pscan
```