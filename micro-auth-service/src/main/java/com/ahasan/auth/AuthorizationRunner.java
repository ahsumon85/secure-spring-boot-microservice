package com.ahasan.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AuthorizationRunner {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationRunner.class, args);
        System.out.println("Authorization server started....");
    }

}
