# SDK for accessing and using the Contrast TeamServer REST API in Java.

  This provides the entry point for using the [Contrast REST API](https://support.contrastsecurity.com/entries/23724324-Introduction-to-the-TeamServer-REST-API). Easily make an instance of com.contrastsecurity.rest.ContrastConnection and call the API methods.
### Environment Prerequisite
    * Install JDK7
    * Install Maven
### How to use this SDK
1. Simply run maven task 'clean install' and add it as your project dependency
2. You should have a standing TeamServer Instance and below required data;
3. Required data:
    * Api Key - API Key
    * Service Key - User service key
    * User -- Username (e.g., joe@acme.com)
    * Rest Api URL: the base Contrast API URL, refer to [Contrast API Endpoints]( https://support.contrastsecurity.com/entries/24184040-API-Endpoints) ( e.g., http://111.222.333.444:19080/Contrast/api )
    
### Code example of using this SDK
```
ContrastConnection conn = new ContrastConnection("contrast_admin", "demo", "demo", "http://localhost:19080/Contrast/api");
List<Application> apps = conn.getApplications();
for(Application app: apps)
{
	System.out.println(app.getName() + " / "+ app.getLanguage());
}
Gson gson = new Gson();
System.out.println(gson.toJson(apps));
```
Then the output would be like:
```
WebGoatJava0 / Java
WebGoatJava1 / Java
[{"app-id":"8f6eaf69-1c6a-4609-b8b6-aa7f9707698b","license":"Enterprise","path":"/lucy/root","name":"WebGoatJava0",
"views":0,"language":"Java","last-seen":1431484944000,"platform-vulnerabilities":[]},
{"app-id":"87647679-aa75-4b1f-9b8e-3516660f5093","license":"Enterprise","path":"/WebGoat","name":"WebGoatJava1",
"views":64,"language":"Java","last-seen":1435015113000,"platform-vulnerabilities":[]}]
```