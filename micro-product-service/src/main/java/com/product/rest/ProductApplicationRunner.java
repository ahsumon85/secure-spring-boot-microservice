package com.product.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class ProductApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplicationRunner.class, args);
		System.out.println("Product service ruuning....!");
	}

}
