# Contrast TeamServer Java SDK
This SDK gives you a quick start for programmatically accessing the [Contrast REST API](https://docs.contrastsecurity.com/dev_api1.html) using Java.

### Requirements
* JDK 7 / 8
* Maven
* TeamServer account
    
### How to use this SDK
1. Clone the project and run 'clean install' Maven goals to build the artifact with the latest code
2. Add contrast-sdk-java to your project dependency:

    ```
    <dependency>
        <groupId>com.contrastsecurity</groupId>
        <artifactId>contrast-sdk-java</artifactId>
        <version>1.2-SNAPSHOT</version>
    </dependency>
    ```
3. Look up the following information from TeamServer
    * API Key
    * User Service Key
    * REST API URL: the base Contrast API URL, refer to[Contrast API Endpoints]( https://support.contrastsecurity.com/entries/24184040-API-Endpoints)
        * e.g., http://localhost:19080/Contrast/api
4. Do what you want!

Code example:
```
ContrastConnection conn = new ContrastConnection("contrast_admin", "demo", "demo", "http://localhost:19080/Contrast/api");
Applications apps = conn.getApplications("a64b1f3e-a0b5-4e8b-900f-6ae263711d6d");

Gson gson = new Gson();
System.out.println(gson.toJson(apps));
```

Sample output:
```
"applications": [{
	"app_id": "72358543-bbdb-490c-8e3f-1b5f5e9a0bf7",
	"archived": false,
	"created": 1461631080000,
	"status": "offline",
	"path": "/Curl",
	"name": "Contrast-Curl",
	"language": "Java",
	"last_seen": 1461631080000,
	"total_modules": 1,
	"master": false
}, {
	"app_id": "b9acb026-36a6-4f4e-b568-33168b7a5ae6",
	"archived": false,
	"created": 1460582653000,
	"status": "offline",
	"path": "/portal",
	"name": "portal",
	"language": "Java",
	"last_seen": 1460925180000,
	"total_modules": 1,
	"master": false
}, {
	"app_id": "53775e84-90a6-4a64-bafe-2b153c3a40f0",
	"archived": false,
	"created": 1460582640000,
	"status": "offline",
	"path": "/",
	"name": "ROOT",
	"language": "Java",
	"last_seen": 1460925180000,
	"total_modules": 1,
	"master": false
}, {
	"app_id": "9e88815f-bb0d-44b4-ac5a-f02f661e8947",
	"archived": false,
	"created": 1460582659000,
	"status": "offline",
	"path": "/library",
	"name": "sakai-library",
	"language": "Java",
	"last_seen": 1460925180000,
	"total_modules": 1,
	"master": false
}, {
	"app_id": "e9a14797-42e7-4ba4-8282-364fdf37026c",
	"archived": false,
	"created": 1460582916000,
	"status": "offline",
	"path": "/sakai-user-tool",
	"name": "sakai-user-tool",
	"language": "Java",
	"last_seen": 1460925180000,
	"total_modules": 2,
	"master": true
}, {
	"app_id": "469b9147-5736-4b83-9158-657427d4c960",
	"archived": false,
	"created": 1461600682000,
	"status": "offline",
	"path": "/examples",
	"name": "Servlet and JSP Examples",
	"language": "Java",
	"last_seen": 1461737160000,
	"total_modules": 1,
	"master": false
}, {
	"app_id": "3da856f4-c508-48b8-95a9-514eddefcbf3",
	"archived": false,
	"created": 1461599820000,
	"status": "offline",
	"path": "/WebGoat",
	"name": "WebGoat",
	"language": "Java",
	"last_seen": 1461737160000,
	"total_modules": 1,
	"master": false
}]
}
```