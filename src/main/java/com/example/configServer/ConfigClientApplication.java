package com.example.configServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableCaching 
public class ConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}

//	@RestController
//	@RefreshScope
//	public static class Controller {
//		@Value("${example.phase}")
//		private String config;
//
////		@GetMapping
//		@RequestMapping(value="/configClient")
//		public String test() {
//			return config;
//		}
//	}

}
