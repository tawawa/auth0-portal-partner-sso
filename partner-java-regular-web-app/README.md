## Partner Web App SSO using Java Servlets and JSP

### Prerequisites

In order to run this example you will need to have Java 8 and Maven installed. You can install Maven with [brew](http://brew.sh/):

```sh
brew install maven
```

Check that your maven version is 3.0.x or above:
```sh
mvn -v
```

### Update configuration information

Enter your:

`client_id`, `client_secret`, `domain`, and `connection` information into `src/main/webapp/WEB-INF/web.xml`


### Build and Run

In order to build and run the project you must execute:

```sh
mvn clean install tomcat7:run
```

Then, go to [http://app2.demonstration.site:3001/](http://app2.demonstration.site:3001/).
