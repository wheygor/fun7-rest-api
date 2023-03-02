package com.outfit7.fun7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@ConfigurationPropertiesScan
@EnableAsync
public class Fun7RestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Fun7RestApiApplication.class, args);
	}

}
