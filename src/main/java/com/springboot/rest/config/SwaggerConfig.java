package com.springboot.rest.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	private static final String BASE_PACKAGE = "com.rest.mongo.controller";
	private static final String SEARCH = "spring-rest-swagger";
	private static final String GROUP_NAME_VERSION_DEFAULT = "search.springboot";
	private static final String GROUP_NAME_VERSION_V1 = "search.springboot-v1";	
	
	@Bean
	public Docket swaggerDocketDefault() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName(GROUP_NAME_VERSION_DEFAULT)
				.select()
				  .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
					.apis(p -> {
						if (null != p.produces()) {
							for (MediaType mt : p.produces()) {
								if (mt.toString().equals("application/json")) {
									return true;
								}
							}
						}
						return false;
					})
				.build()
				.produces(Collections.singleton("application/json"))
				.apiInfo(apiInfo());
				
	}
	
	@Bean
	public Docket swaggerDocketV1() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName(GROUP_NAME_VERSION_V1)
				.select()
					.apis(p -> {
						if (null != p.produces()) {
							for (MediaType mt : p.produces()) {
								if (mt.toString().equals("application/vnd.search.springboot-v1+json")) {
									return true;
								}
							}
						}
						return false;
					})
				.build()
				.produces(Collections.singleton("application/vnd.search.springboot-v1+json"))
				.apiInfo(apiInfo());
				
	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(SEARCH).build();
	}

}
