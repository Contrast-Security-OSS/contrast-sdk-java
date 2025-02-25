# Contrast Gradle Plugin

Gradle plugin for including the Contrast Security analysis in Java web applications 

Requires gradle version 8.3+

## Building

Use `./gradlew build` to build the plugin


```shell
./gradlew publishToMavenLocal
```


## Tasks
The plugin has configures two publicly available tasks.
1. `resolveAgent` -> Uses credentials defined in the `contrastConfiguration` block and downloads an agent via the ContrastSDK. If an agent jar was provided in the `jarPath` field, the task will verify the agent has been located at the given path.
2. `contrastCheck` -> Runs all configured `Test` tasks with the agent installed, and fails the build if any vulnerabilities are found for the application on TeamServer.


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


## Developement
### Publishing to MavenLocal
Run `./gradlew publishToMavenLocal` to install the plugin in your Maven Local repository for testing. You can then apply the plugin to your test application as you would any other plugin 
```code 
plugins {
    id 'com.contrastsecurity.java' version '<version>'`
}
```


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
./gradlew test -Pe2e
```
