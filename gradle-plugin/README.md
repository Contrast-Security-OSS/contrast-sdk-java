# Contrast Gradle Plugin

Gradle plugin for including the Contrast Security analysis in Java web applications 

## Building
Requires Java 21 to build

Use `./gradlew build` to build the plugin

## Publishing to MavenLocal
To publish this plugin to your mavenLocal apply the `maven-publish` plugin to this project's `build.gradle` file and run:


```shell
./gradlew publishToMavenLocal
```

## Coniguration
Attaching the Java agent with this plugin relies on your API credentials being set in the following env variables:
```shell
export CONTRAST__API__URL=https://app.contrastsecurity.com/Contrast/api
export CONTRAST__API__USER_NAME=<your-user-name>
export CONTRAST__API__API_KEY=<your-api-key>
export CONTRAST__API__SERVICE_KEY=<your-service-key>
export CONTRAST__API__ORGANIZATION_ID=<your-organization-id>
```
