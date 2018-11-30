package com.online.Eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServer458882Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServer458882Application.class, args);
	}
}
