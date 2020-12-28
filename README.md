# Spring Security Oauth2 With [simple-spring-boot-microservice](https://github.com/ahsumon85/simple-spring-boot-microservice)

![Screenshot from 2020-11-22 09-59-11](https://user-images.githubusercontent.com/31319842/99893389-be602800-2ca9-11eb-951f-639c42520d23.png)

## Overview
### The architecture is composed by five services:

   * [`micro-eureka-server`](https://github.com/habibsumoncse/secure-spring-boot-microservice#eureka-service): Service **Discovery Server** created with Eureka
   * [`micro-api-getway`](https://github.com/habibsumoncse/secure-spring-boot-microservice#api-gateway-service): API Gateway created with Zuul that uses the discovery-service to send the requests to the services. It uses Ribbon as a Load Balancer
   * [`micro-auth-service`](https://github.com/habibsumoncse/secure-spring-boot-microservice#authorization-service): Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **authorization service**
   * [`micro-item-service`](https://github.com/ahsumon85/secure-spring-boot-microservice#item-service--resource-service): Simple REST service created with `Spring Boot, Spring Data JPA, MySQL` to use as a **resource service**
   * [`micro-sales-service`](https://github.com/ahsumon85/secure-spring-boot-microservice#sales-service--resource-service): Simple REST service created with `Spring Boot, Spring Data JPA, MySQL` to use as a **resource service**

`Follow the link to see Hystrix Dashboard and swagger in microservice architecture` [`advance-spring-boot-microservice`](https://github.com/ahsumon85/advance-spring-boot-microservice) 

### Tools you will need

* Maven 3.0+ is your build tool
* Your favorite IDE but we will recommend `STS-4-4.4.1 version`. We use STS.
* MySQL server
* JDK 1.8+

### Microservice Running Process:

- First we need to run `eureka service`
- Second we need to run `auth-service`
- Third we need to run `item-servic` and `sales-service`
- Finally we need to run `gateway-service`, if we did run `gateway-service` before running `auth-service and iteam,sales-service` then we have to wait approximately 10 second 



## Eureka Service

Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server.

### How to run eureka service?

 **Run on sts IDE**

 `click right button on the project >Run As >Spring Boot App`

Eureka Discovery-Service URL: `http://localhost:8761`


## Authorization Service

![1](https://user-images.githubusercontent.com/31319842/99893591-9d003b80-2cab-11eb-9f06-1d5a785c775b.png)

Whenever we think of microservices and distributed applications, the first point that comes to mind is security. Obviously, in distributed architectures, it is really difficult to manage security as we do not have much control over the application. So in this situation, we always need to have a central entry point to this distributed architecture. This is the reason why, in microservices, we have a separate and dedicated layer for all these purposes. This layer is known as the API Gateway. It is an entry point for a microservice's architecture.

To maintain security, the first necessary condition is to restrict direct microservice calls for outside callers. All calls should only go through the API Gateway. The API Gateway is mainly responsible for authentication and authorization of the API requests made by external callers. Also, this layer performs the routing of API requests that come from external clients to respective microservices. This allows the API Gateway to act as an entry point for all its respective microservices. So, we can say the API Gateway is mainly responsible for the security of microservices.

### Oauth2
In this Spring security oauth2 tutorial, learn to build an authorization server to authenticate your identity to provide access_token, which you can use to request data from resource server.

**Introduction to OAuth 2**
OAuth 2 is an authorization method to provide access to protected resources over the HTTP protocol. Primarily, oauth2 enables a third-party application to obtain limited access to an HTTP service –

* either on behalf of a resource owner by orchestrating an approval interaction between the resource owner and the HTTP service
* or by allowing the third-party application to obtain access on its own behalf.

**OAuth2 Roles:** There are four roles that can be applied on OAuth2:

* `Resource Owner`: The owner of the resource — this is pretty self-explanatory.
* `Resource Server`: This serves resources that are protected by the OAuth2 token.
* `Client`: The application accessing the resource server.
* `Authorization Server`:  This is the server issuing access tokens to the client after successfully authenticating the resource owner and obtaining authorization.


**OAuth2 Tokens:** Tokens are implementation specific random strings, generated by the authorization server.

* `Access Token`: Sent with each request, usually valid for about an hour only.
* `Refresh Token`: It is used to get a 00new access token, not sent with each request, usually lives longer than access token.


### Quick Start a Cloud Security App

Let's start by configuring spring cloud oauth2 in a Spring Boot application for microservice security.

First, we need to add the `spring-cloud-starter-oauth2` dependency:
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version>2.2.2.RELEASE</version>
</dependency>
```
This will also bring in the `spring-cloud-starter-security`dependency.
```
<dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-security</artifactId>
</dependency>
```

### Create tables for users, groups, group authorities and group members

For Spring OAuth2 mechanism to work, we need to create tables to hold users, groups, group authorities and group members. We can create these tables as part of application start up by providing the table definations in `schema.sql` file as shown below. This setup is good enough for POC code.
`src/main/resources/schema.sql`
```
create table if not exists  oauth_client_details (
  client_id varchar(255) not null,
  client_secret varchar(255) not null,
  web_server_redirect_uri varchar(2048) default null,
  scope varchar(255) default null,
  access_token_validity int(11) default null,
  refresh_token_validity int(11) default null,
  resource_ids varchar(1024) default null,
  authorized_grant_types varchar(1024) default null,
  authorities varchar(1024) default null,
  additional_information varchar(4096) default null,
  autoapprove varchar(255) default null,
  primary key (client_id)
);

create table if not exists  permission (
  id int(11) not null auto_increment,
  name varchar(512) default null,
  primary key (id),
  unique key name (name)
) ;

create table if not exists role (
  id int(11) not null auto_increment,
  name varchar(255) default null,
  primary key (id),
  unique key name (name)
) ;

create table if not exists  user (
  id int(11) not null auto_increment,
  username varchar(100) not null,
  password varchar(1024) not null,
  email varchar(1024) not null,
  enabled tinyint(4) not null,
  accountNonExpired tinyint(4) not null,
  credentialsNonExpired tinyint(4) not null,
  accountNonLocked tinyint(4) not null,
  primary key (id),
  unique key username (username)
) ;

create table  if not exists permission_role (
  permission_id int(11) default null,
  role_id int(11) default null,
  key permission_id (permission_id),
  key role_id (role_id),
  constraint permission_role_ibfk_1 foreign key (permission_id) references permission (id),
  constraint permission_role_ibfk_2 foreign key (role_id) references role (id)
);

create table if not exists role_user (
  role_id int(11) default null,
  user_id int(11) default null,
  key role_id (role_id),
  key user_id (user_id),
  constraint role_user_ibfk_1 foreign key (role_id) references role (id),
  constraint role_user_ibfk_2 foreign key (user_id) references user (id)
);
```
* `oauth_client_details table` is used to store client details.
* `oauth_access_token` and `oauth_refresh_token` is used internally by OAuth2 server to store the user tokens.

### Create a client

Let’s insert a record in `oauth_client_details` table for a client named appclient with a password `appclient`.

Here, `appclient` is the ID has access to the `product-server` and `sales-server` resource.

I have used `CodeachesBCryptPasswordEncoder.java` available [here](https://github.com/habibsumoncse/spring-boot-microservice-auth-zuul-eureka-hystrix/blob/master/micro-auth-service/src/main/resources/schema.sql) to get the Bcrypt encrypted password.

`src/main/resources/data.sql`

```
INSERT INTO oauth_client_details (client_id, client_secret, web_server_redirect_uri, scope, access_token_validity, refresh_token_validity, resource_ids, authorized_grant_types, additional_information) 
VALUES ('mobile', '{bcrypt}$2a$10$gPhlXZfms0EpNHX0.HHptOhoFD1AoxSr/yUIdTqA8vtjeP4zi0DDu', 'http://localhost:8080/code', 'READ,WRITE', '3600', '10000', 'inventory,payment', 'authorization_code,password,refresh_token,implicit', '{}');

/*client_id - client_secret*/
/* mobile - pin* /


 INSERT INTO PERMISSION (NAME) VALUES
 ('create_profile'),
 ('read_profile'),
 ('update_profile'),
 ('delete_profile');

 INSERT INTO role (NAME) VALUES ('ROLE_admin'),('ROLE_editor'),('ROLE_operator');

 INSERT INTO PERMISSION_ROLE (PERMISSION_ID, ROLE_ID) VALUES
     (1,1), /*create-> admin */
     (2,1), /* read admin */
     (3,1), /* update admin */
     (4,1), /* delete admin */
     (2,2),  /* read Editor */
     (3,2),  /* update Editor */
     (2,3);  /* read operator */


 insert into user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('1', 'admin','{bcrypt}$2a$12$xVEzhL3RTFP1WCYhS4cv5ecNZIf89EnOW4XQczWHNB/Zi4zQAnkuS', 'habibsumoncse2@gmail.com', '1', '1', '1', '1');
 
 insert into  user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('2', 'ahasan', '{bcrypt}$2a$12$DGs/1IptlFg0szj.3PttmeC8swHZs/pZ6YEKng4Cl1l2woMtkNhvi','habibsumoncse2@gmail.com', '1', '1', '1', '1');
 
 insert into  user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('3', 'user', '{bcrypt}$2a$12$udISUXbLy9ng5wuFsrCMPeQIYzaKtAEXNJqzeprSuaty86N4m6emW','habibsumoncse2@gmail.com', '1', '1', '1', '1');
 /*
 username - passowrds:
 admin - admin
 ahasan - ahasan
 user - user
 */


INSERT INTO ROLE_USER (ROLE_ID, USER_ID)
    VALUES
    (1, 1), /* admin-admin */,
    (2, 2), /* ahasan-editor */ ,
    (3, 3); /* user-operatorr */ ;
```
### Configure Authorization Server

Annotate the `Oauth2AuthorizationServerApplication.java` with `@EnableAuthorizationServer`. This enables the Spring to consider this service as authorization Server.

Let’s create a class `AuthorizationServerConfiguration.java` with below details.

* **JdbcTokenStore** implements token services that stores tokens in a database.
* **BCryptPasswordEncoder** implements PasswordEncoder that uses the BCrypt strong hashing function. Clients can optionally supply a “strength” (a.k.a. log rounds in BCrypt) and a SecureRandom instance. The larger the strength parameter the more work will have to be done (exponentially) to hash the passwords. The value used in this example is 8 for client secret.
* **AuthorizationServerEndpointsConfigurer** configures the non-security features of the Authorization Server endpoints, like token store, token customizations, user approvals and grant types.
* **AuthorizationServerSecurityConfigurer** configures the security of the Authorization Server, which means in practical terms the /oauth/token endpoint.
* **ClientDetailsServiceConfigurer** configures the ClientDetailsService, e.g. declaring individual clients and their properties.

```
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfiguration implements AuthorizationServerConfigurer {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception 		{
        security.checkTokenAccess("isAuthenticated()").tokenKeyAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jdbcTokenStore());
        endpoints.authenticationManager(authenticationManager);
    }
}
```
### Configure User Security Authentication
Let’s create a class `UserSecurityConfig.java` to handle user authentication.

* **PasswordEncoder** implements PasswordEncoder that uses the BCrypt strong hashing function. Clients can optionally supply a “strength” (a.k.a. log rounds in BCrypt) and a SecureRandom instance. The larger the strength parameter the more work will have to be done (exponentially) to hash the passwords. The value used in this example is 4 for user’s password.
```
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    protected AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
```

### Test Authorization Service
**Get Access Token**

Let’s get the access token for `admin` by passing his credentials as part of header along with authorization details of appclient by sending `client_id` `client_pass` `username` `userpsssword`

Now hit the POST method URL via postman to get the oauth2 token.

**`http://localhost:8180/auth-api/oauth/token`**

Now, add the Request Headers as follows −

- `Authorization` − Basic Auth with your `Client Id` and `Client secret`

- `Content Type` − application/x-www-form-urlencoded
  ![Screenshot from 2020-12-09 10-22-05](https://user-images.githubusercontent.com/31319842/101584943-ed47ff00-3a08-11eb-9d01-e196e0e089a6.png)

Now, add the Request body as follows −

* `grant_type` = password
* `username` = your username
* ` password` = your password
![Screenshot from 2020-12-09 10-22-12](https://user-images.githubusercontent.com/31319842/101584942-ec16d200-3a08-11eb-9355-0e082a2493c7.png)


**HTTP POST Response**
```
{ 
  "access_token":"000ff762-414c-4605-858a-0ed7bee6f68e",
  "token_type":"bearer",
  "refresh_token":"79aabc70-f310-4c49-bf7e-516208b3bef4",
  "expires_in":999999,
  "scope":"read write"
}
```


## Item Service -resource service

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



### Configure Application info and Oauth2 Configuration to check token validaty from auth service

* `security.oauth2.resource.token-info-uri=http://localhost:9191/auth-api/oauth/check_token` That is used to check user given token validaty from authorization service.
* `security.oauth2.client.client-id=mobile` Here `moblie` client-id that was we are already input in auth database of `micro-auth-service`
* `security.oauth2.client.client-secret=pin` Here `pin` client-password that was we are already input in auth database of `micro-auth-service`

Below we was used for checking user given token the following link `[http://localhost:9191/auth-api/oauth/check_token]` on the `http` means protocol, `localhost` for hostaddress, `9191` are port of `micro-auth-service`, we know auth service up on `9191` port `auth-api` are application context path of 'micro-auth-service' and `/oauth/check_token` is used to check token from auth service by spring security oauth2.

```
#Application Configuration
server.port=8380
spring.application.name=item-server
server.servlet.context-path=/sales-api

#oauth2 configuration
security.oauth2.resource.token-info-uri=http://localhost:9191/auth-api/oauth/check_token
security.oauth2.client.client-id=mobile
security.oauth2.client.client-secret=pin
```

### Enable oauth2 on sales service
Now add the `@EnableResourceServer` and `@Configuration` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a resource service. With this `@EnableResourceServer` annotation, this artifact will act like a resource service.


```
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "microservice";
    private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('READ')";
    private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('WRITE')";
    private static final String SECURED_PATTERN = "/**";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().disable()
                .authorizeRequests()
//                 .antMatchers("/item/list").permitAll()
                .and()
                .requestMatchers()                
                .antMatchers(SECURED_PATTERN).and().authorizeRequests()
                .antMatchers(HttpMethod.POST, SECURED_PATTERN)
                .access(SECURED_WRITE_SCOPE)
                .anyRequest().access(SECURED_READ_SCOPE);
    }
}
```
### How to run item service?

 **Run on sts IDE**
 `click right button on the project >Run As >Spring Boot App`

Eureka Discovery-Service URL: `http://localhost:8761`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `item-server` instance gate will be run on `http://localhost:8280` port

### Test HTTP GET Request on item-service -resource service

```
curl --request GET 'localhost:8180/item-api/item/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```
* Here `[http://localhost:8180/item-api/item/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the   
  gateway service, `item-api` are application context path of item service and `/item/find` is method URL.

* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token


### For getting All API Information
On this repository we will see `secure-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.



# Sales Service -resource service
Now we will see `micro-sales-service` as a resource service. The `micro-sales-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of items when the application loads using an `SalesApplicationRunner` bean.

Add the following dependencies:

* **Web:** Spring MVC and embedded Tomcat
* **Actuator:** features to help you monitor and manage your application
* **EurekaClient:** for service registration
* **JPA:** to save/retrieve data
* **MySQL:** to use store data on database
* **RestRepositories:** to expose JPA repositories as REST endpoints
* **Hibernate validator:** to use runtime exception handling and return error messages
* **oauth2:** to use api endpoint security and user access auth permission

### Configure Application info and Oauth2 Configuration to check token validaty from auth service

* `security.oauth2.resource.token-info-uri=http://localhost:9191/auth-api/oauth/check_token` That is used to check user given token validaty from authorization service.
* `security.oauth2.client.client-id=mobile` Here `moblie` client-id that was we are already input in auth database of `micro-auth-service`
* `security.oauth2.client.client-secret=pin` Here `pin` client-password that was we are already input in auth database of `micro-auth-service`

Below we was used for checking user given token the following link `[http://localhost:9191/auth-api/oauth/check_token]` on the `http` means protocol, `localhost` for hostaddress, `9191` are port of `micro-auth-service`, we know auth service up on `9191` port `auth-api` are application context path of 'micro-auth-service' and `/oauth/check_token` is used to check token from auth service by spring security oauth2.
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

### Enable oauth2 on sales service as a resource service
Now add the `@EnableResourceServer` and `@Configuration` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a resource service. With this `@EnableResourceServer` annotation, this artifact will act like a resource service.


```
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "microservice";
    private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('READ')";
    private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('WRITE')";
    private static final String SECURED_PATTERN = "/**";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().disable()
                .authorizeRequests()
//                 .antMatchers("/sales/list").permitAll()
                .and()
                .requestMatchers()                
                .antMatchers(SECURED_PATTERN).and().authorizeRequests()
                .antMatchers(HttpMethod.POST, SECURED_PATTERN)
                .access(SECURED_WRITE_SCOPE)
                .anyRequest().access(SECURED_READ_SCOPE);
    }
}
```
### How to run sales service?

 **Run on sts IDE**
 `click right button on the project >Run As >Spring Boot App`

Eureka Discovery-Service URL: `http://localhost:8761`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `sales-server` instance gate will be run on `http://localhost:8280` port

### Test HTTP GET Request on resource service -resource service
```
curl --request GET 'localhost:8180/sales-api/sales/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```
* Here `[http://localhost:8180/sales-api/sales/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the   
  gateway service, `sales-api` are application context path of item service and `/sales/find` is method URL.

* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token


### For getting All API Information
On this repository we will see `secure-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.



# API Gateway Service

Gateway Server is an application that transmit all API to desire services. every resource services information such us: `service-name, context-path` will beconfigured into the gateway service and every request will transmit configured services by gateway


## How to run API Gateway Service?

 **Run on sts IDE**

 `click right button on the project >Run As >Spring Boot App`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `zuul-server` on eureka dashboard. the gateway instance will be run on `http://localhost:8180` port

![Screenshot from 2020-11-15 11-21-33](https://user-images.githubusercontent.com/31319842/99894579-6af0d880-2caf-11eb-84aa-d41b16cfbd12.png)

After we will seen start `auth, sales, item, zuul` instance, then we can try by using `secure-microservice-architecture.postman_collection.json` imported API from postman with token



# Hystrix, Swagger in [secure-microservice](https://github.com/ahsumon85/secure-spring-boot-microservice)

**Below we will see how to configure Hystrix, swagger and docker in microservice**

**To follow link**  [advance-spring-boot-microservice](https://github.com/ahsumon85/advance-spring-boot-microservice) 

