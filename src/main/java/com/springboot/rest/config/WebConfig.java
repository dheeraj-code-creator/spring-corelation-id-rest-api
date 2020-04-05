package com.springboot.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springboot.rest.interceptor.PCRequestInterceptor;
import com.springboot.rest.interceptor.RequestTimeInterceptor;

/* This is configuration class which will
 * decide which interceptor needs to be called.
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

	public void addInterceptor(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestTimeInterceptor());
		registry.addInterceptor(new PCRequestInterceptor()).excludePathPatterns("/webjars/**", "/swagger-resources/**",
				"/csrf", "/", "/docs", "/swagger-ui**");
	}

	public void configureContentNegotiation(ContentNegotiationConfigurer congifurer) {
		congifurer.defaultContentType(MediaType.APPLICATION_JSON);

	}

}
