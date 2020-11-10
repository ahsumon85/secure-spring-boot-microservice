package com.ahasan.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SalesApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SalesApplicationRunner.class, args);
		System.out.println("Sales service started...!!!");
	}

}
