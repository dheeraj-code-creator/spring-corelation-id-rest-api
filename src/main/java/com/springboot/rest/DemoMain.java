package com.springboot.rest;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.springboot.rest", "com.springboot.rest.config",
		"com.springboot.rest.interceptor"})
public class DemoMain {
	public static void main(String[] args) {
		SpringApplication.run(DemoMain.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
