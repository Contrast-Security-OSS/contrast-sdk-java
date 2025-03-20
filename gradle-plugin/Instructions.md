# Contrast Gradle Plugin

Gradle plugin for Contrast Security, enables the installation of the Contrast Java Agent, connection to TeamServer, and verifying applications for new vulnerabilities

Requires gradle 8.3+


## Tasks
The plugin has configures two publicly available tasks.
- `resolveAgent`
  - Pulls down the agent provided by the ContrastSDK. If an agent is provided with the `jarPath` configuration field, the plugin verifies the file exists.
- `contrastCheck`
  - Runs all configured `Test` tasks with the agent installed, and fails the build if any vulnerabilities are found for the application on TeamServer.

    
## Configuration 
Import the plugin from the Gradle Plugin Portal as shown
```shell 
plugins{
    id 'com.contrastsecurity.contrastplugin' version '3.0.0'
}
```

This plugin is configured via the `contrastConfiguration` block in your projects `gradle.build` script. The `username`, `apiKey`, `serviceKey`, and `orgUuid` are the same credentials you'd use normally. These can be found in TeamServer under the **User Settings** and **Organization Settings**
 
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

### Configuration Properties

| Config Value                       | Description                                                                                                                                                                                                                                | Required? |
|------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|
| `username`, `apiKey`, `serviceKey` | Can be found in TeamServer under **User Settings**                                                                                                                                                                                         | Yes       |
| `orgUuid`                          | Can be found in TeamServer under **Organization Settings**                                                                                                                                                                                 | Yes       |
| `apiUrl`                           | The url from the TeamServer organization you use, and ends in `Contrast/api`. This is used by the ContrastSDK to retrieve the agentJar and to retrieve vulnerability data - Example: `https://teamserver-staging.contsec.com/Contrast/api` | Yes       |
| `appName`                          | If appName is not set, the name will be the gradle project name                                                                                                                                                                            | No        |
| `serverName`, `appVersion`         | Used to correlate data on TS, can be an existing app and server, but can also be new values for each                                                                                                                                       | Yes       |
| `jarPath`                          | Path to a provided Contrast Java Agent. If not provided, the plugin will download a path from the SDK                                                                                                                                      | No        |
| `minSeverity`                      | Configures the minimum severity of vulnerability required for the plugin to fail the build. Values in order of increasing severity = `Note`, `Low`, `Medium`, `High`, `Critical`. Defaults to `Medium`                                     | No        |



## Usage
Once your plugin has been added to your project and configured, simply run
```shell
./gradlew contrastCheck  
```
This will run all tasks of type `Test` in the project with the Contrast Java Agent attached. If any vulnerabilities which exceed the minimum severity level are found, the build will fail and the vulnerabilities will be logged to `<buildFolder>/contrast/traceResults_<testTaskName>.txt`
