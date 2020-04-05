package com.springboot.rest.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/* Interceptor class responsible for intercepting request and 
 * capturing the start time of the request..
 * This Interceptor class is used to capture reqeust time.
 */

public class RequestTimeInterceptor extends HandlerInterceptorAdapter {
	
	public static final String REQUEST_TIME_KEY = "requestTime";
	
	/*
	 * This method is used to capture start time of the request
	 */
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		request.setAttribute(REQUEST_TIME_KEY, new Date());
		return true;
	}

}
