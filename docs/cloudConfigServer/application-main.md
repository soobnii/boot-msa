package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class CloudConfnigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudConfnigServerApplication.class, args);
	}

}
 