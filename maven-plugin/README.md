# Contrast Maven Plugin

Maven plugin for including Contrast security analysis in Java web applications

See [usage](https://contrastsecurity.dev/contrast-maven-plugin/usage.html) to get started

Available on [Maven Central](https://search.maven.org/search?q=a:contrast-maven-plugin)


## Building

Requires JDK 21 to build.

Tests require JDK 11 and 17 to be set-up in [Maven
Toolchains](https://maven.apache.org/guides/mini/guide-using-toolchains.html)
and requires that Maven be on the `PATH`.

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


### End-to-End Testing

By default, the integration tests simulate the Contrast API using a stub web server. Developers can
change this behavior to test with an actual Contrast instance instead of the stub.

First, configure your environment with standard Contrast connection environment variables.

```shell
export CONTRAST__API__URL=https://app.contrastsecurity.com/Contrast/api
export CONTRAST__API__USER_NAME=<your-user-name>
export CONTRAST__API__API_KEY=<your-api-key>
export CONTRAST__API__SERVICE_KEY=<your-service-key>
export CONTRAST__API__ORGANIZATION_ID=<your-organization-id>
```

You may find it useful to store the environment variable configuration in a file so that you can
easily include it in your environment

```shell
source ~/contrast.env
```

Having configured the environment, you can run the integration tests with the `end-to-end-test`
profile active:

```shell
./mvnw -Pend-to-end-test verify
```

When you are finished testing, you may want to remove the variables from the
environment. In a POSIX shell, the script `unset-contrast.env` can take care of
this:

```shell
source unset-contrast.env
```
