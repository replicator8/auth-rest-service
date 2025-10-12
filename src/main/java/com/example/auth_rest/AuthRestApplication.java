package com.example.auth_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(
		scanBasePackages = {"com.example.auth_rest", "com.example.demo"},
		exclude = {DataSourceAutoConfiguration.class}
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class AuthRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthRestApplication.class, args);
	}

}
