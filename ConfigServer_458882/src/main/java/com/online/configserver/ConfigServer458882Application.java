package com.online.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServer458882Application {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServer458882Application.class, args);
	}
}
