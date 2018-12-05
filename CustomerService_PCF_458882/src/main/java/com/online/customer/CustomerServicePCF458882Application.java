package com.online.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerServicePCF458882Application {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServicePCF458882Application.class, args);
	}
}
