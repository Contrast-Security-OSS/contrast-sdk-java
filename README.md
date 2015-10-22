# Contrast TeamServer Java SDK
This SDK gives you a quick start for programmatically accessing the [Contrast REST API](https://docs.contrastsecurity.com/dev_api1.html) using Java.

### Requirements
* JDK 7
* Maven
* TeamServer account
    
### How to use this SDK
1. Clone the project and run 'clean install' Maven goals to build the artifact with the latest code
2. Add contrast-sdk-java to your project dependency
```
<dependency>
    <groupId>com.contrastsecurity</groupId>
    <artifactId>contrast-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```
3. Look up the following information from TeamServer
    * API Key
    * User Service Key
    * REST API URL: the base Contrast API URL, refer to [Contrast API Endpoints]( https://support.contrastsecurity.com/entries/24184040-API-Endpoints) ( e.g., http://111.222.333.444:19080/Contrast/api )
4. Do what you want!

Code example:
```
ContrastConnection conn = new ContrastConnection("contrast_admin", "demo", "demo", "http://localhost:19080/Contrast/api");
		List<Application> apps = conn.getApplications("a64b1f3e-a0b5-4e8b-900f-6ae263711d6d");
		for(Application app: apps)
		{
			System.out.println(app.getName() + " / "+ app.getLanguage());
		}
		Gson gson = new Gson();
		System.out.println(gson.toJson(apps));
```

Sample output:
```
WebGoat / Java
[{"app-id":"aecfb08d-dbff-4665-b4ff-43930bffc81a","license":"Enterprise","path":"/WebGoat","name":"WebGoat","views":64,"language":"Java",
"technologies":["jQuery","Spring MVC","HTML5","ECS","J2EE","JSP","Bootstrap"],"last-seen":1445476419000,"platform-vulnerabilities":[]}]
```