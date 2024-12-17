# Contrast Gradle Plugin

Gradle plugin for including the Contrast Security analysis in Java web applications 

Requires gradle version 8.3+

## Building

Use `./gradlew build` to build the plugin


```shell
./gradlew publishToMavenLocal
```


## Tasks
The `installAgent` task takes in your configuration as defined by the `contrastConfiguration` block and attaches the java agent to all Test tasks for your project.
If no Agent is provided, the plugin will attempt to download the current Java Agent available on TeamServer, at the endpoint provided in the configuration.


## Configuration 
This plugin is configured via the `contrastConfiguration` block in your projects `gradle.build` script
```shell
contrastConfiguration{
  username = '<username>'
  apiKey = '<apiKey>'
  serviceKey = '<serviceKey>'
  apiUrl = '<apiUrl>'
  orgUuid = '<orgUuid>'
  appName = '<appName>'
  serverName = '<serverName>'
  appVersion = '<appVersion>'
  jarPath = "<path.to.local.agent.jar>"
}
```

### AppName
If no app name is configured the plugin will use the gradle project's name instead

### AppVersion
TODO: If no version is provided, the plugin will generate one based on the current Travis build number

Attaching the Java agent with this plugin relies on your API credentials being set in the following env variables:

### Running with your tests
The plugin will add jvm arguments for your run tests, but only if `installAgent` is run as a dependency for the test task.
To have your tests run with the agent add the following configuration to your project's `build.gradle` file
```shell
tasks.named("test").configure {
  dependsOn("installAgent")
}
```
TODO auto attach to tests

## Developement
### Publishing to MavenLocal
To publish this plugin to your mavenLocal apply the `maven-publish` plugin to this project's `build.gradle` file and run:
In order to run the plugin's end-to-end tests, you must configure these variables in your environment


### End to End testing
```shell
export CONTRAST__API__URL=https://app.contrastsecurity.com/Contrast ##Use your standard endpoint for the org, the plugin will apply `/api` for the restapi functionality
export CONTRAST__API__USER_NAME=<your-user-name>
export CONTRAST__API__API_KEY=<your-api-key>
export CONTRAST__API__SERVICE_KEY=<your-service-key>
export CONTRAST__API__ORGANIZATION_ID=<your-organization-id>
```
To enable end-to-end testing, these variables must be present and you must use the property `e2e`
```shell
./gradkew test -Pe2e
```
