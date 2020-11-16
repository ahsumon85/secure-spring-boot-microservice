## Spring Boot, Spring Cloud Oauth2, Spring Cloud Netflix Eureka, Spring CLoud Zuul, Spring Data JPA, MySQL.



# Overview
The architecture is composed by five services:

   * `micro-eureka-server`: Service **Discovery Server** created with Eureka
   * `micro-api-getway`: API Gateway created with Zuul that uses the discovery-service to send the requests to the services. It uses Ribbon as a Load Balancer
   * `micro-auth-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **authorization service**
   * `micro-product-service`: Simple REST service created with `Spring Boot, Spring Data JPA, MySQL` to use as a **resource service**
   * `micro-sales-service`: Simple REST service created with `Spring Boot, Spring Data JPA, MySQL` to use as a **resource service**

### tools you will need
* Maven 3.0+ is your build tool
* Your favorite IDE but we will recommend `STS-4-4.4.1 version`. We use STS.
* MySQL server
* JDK 1.8+

##
# Eureka Service

Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server.

## How to run eureka service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

`mvn clean install`

or

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

Run on terminal `java –jar <JARFILE> `

 Run on sts IDE
 
 `click right button on the project >Run As >Spring Boot App`
 
Eureka Discovery-Service URL: `http://localhost:8761`


##
# Item Service

Now we will see `micro-item-service` as a resource service. The `micro-item-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of items when the application loads using an `ItemApplicationRunner` bean.

Add the following dependencies:

* **Web:** Spring MVC and embedded Tomcat
* **Actuator:** features to help you monitor and manage your application
* **EurekaClient:** for service registration
* **JPA:** to save/retrieve data
* **MySQL:** to use store data on database
* **RestRepositories:** to expose JPA repositories as REST endpoints
* **Hibernate validator:** to use runtime exception handling and return error messages
* **oauth2:** to use api endpoint security and user access auth permission

***Configure Application info and Oauth2 Configuration to check token validaty for auth service***

* `security.oauth2.resource.token-info-uri=http://localhost:9191/auth-api/oauth/check_token` That is used to check user given token validaty from authorization service.
* `security.oauth2.client.client-id=mobile` Here `moblie` client-id that was we are already input in auth database of `micro-auth-service`
* `security.oauth2.client.client-secret=pin` Here `pin` client-password that was we are already input in auth database of `micro-auth-service`

below we was used for checking user given token the following link `[http://localhost:9191/auth-api/oauth/check_token]` on the `http` means protocol, `localhost` for hostaddress `9191` are port of `micro-auth-service` we know auth service up on `9191` port `auth-api` are application context path of 'micro-auth-service' and `/oauth/check_token` is used to check token from auth service by spring security oauth2.

```
#Application Configuration
server.port=8380
spring.application.name=sales-server
server.servlet.context-path=/sales-api

#oauth2 configuration
security.oauth2.resource.token-info-uri=http://localhost:9191/auth-api/oauth/check_token
security.oauth2.client.client-id=mobile
security.oauth2.client.client-secret=pin
```

* ***Enable oauth2 on sales service as a resource service***
Now add the `@SpringBootApplication` and `@EnableResourceServer` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a resource service.

### Test HTTP GET Request on resource service (sales-service)
```
curl --location --request GET 'localhost:8180/sales-api/sales/find' --header 'Authorization: Bearer f11b7ced-3ebb-45ad-b47d-917c213c73de'
```
here `[http://localhost:8180/sales-api/sales/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the gateway service, `sales-api` are application context path of sales service and `/sales/find` is method URL.

### For getting All API Information
On this repository we will see `simple-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.

## How to run item service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

 `java –jar <JARFILE> `
 Run on sts IDE
 `click right button on the project >Run As >Spring Boot App`
 
Eureka Discovery-Service URL: `http://localhost:8761`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `item-server` instance gate will be run on `http://localhost:8280` port

### Test HTTP GET Request on resource service
```
curl --request GET http://localhost:8180/item-api/item/find
```
here `[http://localhost:8180/item-api/item/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the gateway service, `item-api` are context path of item service  and `/item/find` is method URL.

### For getting All API Information
On this repository we will see `secure-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.


##
# Sales Service

Now we will see `micro-sales-service` as a resource service. The `micro-sales-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of products when the application loads using an `SalesApplicationRunner` bean.

Add the following dependencies:

* **Web:** Spring MVC and embedded Tomcat
* **Actuator:** features to help you monitor and manage your application
* **EurekaClient:** for service registration
* **JPA:** to save/retrieve data
* **MySQL:** to use store data on database
* **RestRepositories:** to expose JPA repositories as REST endpoints
* **hibernate validator:** to use runtime exception handling and return error messages
* **oauth2:** to use api endpoint security and access auth permission

***Configure Application info, Database info and a few other configuration in properties file***
```
#Application Configuration
server.port=8380
spring.application.name=sales-server
server.servlet.context-path=/sales-api

#MySQL Database Configuration
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/sales_service?useSSL=false&createDatabaseIfNotExist=true
spring.datasource.username=[username]
spring.datasource.password=[password]

#Hibernet JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL57Dialect
spring.jpa.generate-ddl=true
spring.jpa.show-sql=true

#eureka server url
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.preferIpAddress=true
eureka.instance.lease-expiration-duration-in-seconds=1
eureka.instance.lease-renewal-interval-in-seconds=2

#oauth2 configuration
security.oauth2.resource.token-info-uri=http://localhost:9191/auth-api/oauth/check_token
security.oauth2.client.client-id=mobile
security.oauth2.client.client-secret=pin
```

### Enable Eureka Registry Service on sales service
Now add the `@EnableEurekaClient` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a resource service.

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `sales-server` instance gate will be run on `http://localhost:8380` port

### Test HTTP GET Request on resource service
```
curl --request GET http://localhost:8180/sales-api/sales/find
```
here `[http://localhost:8180/sales-api/sales/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the gateway service, `sales-api` are application context path of sales service and `/sales/find` is method URL.

### For getting All API Information
On this repository we will see `simple-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.


##
# API Gateway Service

***Enable Zuul Service Proxy***
Now add the `@EnableZuulProxy` and `@EnableEurekaClient` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a Zuul service proxy and will enable all the features of a API gateway layer as described before. We will then add some filters and route configurations.
```
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulApiGetWayRunner {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApiGetWayRunner.class, args);
		System.out.println("Zuul server is running...");
	}

	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}

	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}

	@Bean
	public ErrorFilter errorFilter() {
		return new ErrorFilter();
	}

	@Bean
	public RouteFilter routeFilter() {
		return new RouteFilter();
	}
}
```
***Zuul routes configuration***
Open application.properties and add below entries-
```
#Will start the gateway server @8180
server.port=8180
spring.application.name=zuul-server


# Disable accessing services using service name (i.e. user-service).
# They should be only accessed through the path defined below.
zuul.ignored-services=*

zuul.routes.secound.id=sales-server
zuul.routes.first.id=product-server

# Map paths to employee service
zuul.routes.product-server.path=/product-api/**
zuul.routes.product-server.serviceId=product-server
zuul.routes.product-server.stripPrefix=false

# Map paths to sales service
zuul.routes.sales-server.path=/sales-api/**
zuul.routes.sales-server.serviceId=sales-server
zuul.routes.sales-server.stripPrefix=false

eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
eureka.instance.preferIpAddress=true
eureka.instance.lease-expiration-duration-in-seconds=1
eureka.instance.lease-renewal-interval-in-seconds=2

ribbon.eager-load.enabled= true
ribbon.ConnectTimeout= 30000
ribbon.ReadTimeout= 30000
```

## How to run API Gateway Service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

`mvn clean install`
or

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

 `java –jar <JARFILE> `

 Run on sts IDE
 
 `click right button on the project >Run As >Spring Boot App`
 
After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `zuul-server` instance gate will be run on `http://localhost:8180` port
