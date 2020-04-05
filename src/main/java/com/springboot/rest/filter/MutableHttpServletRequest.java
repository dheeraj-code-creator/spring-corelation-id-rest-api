package com.springboot.rest.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// wrapper class to wrap HttpServletRequest to expose method to add a new header

@Primary
@Component
public class MutableHttpServletRequest extends HttpServletRequestWrapper{
	
	private final Map<String, String> customHeaders;
	
	public MutableHttpServletRequest(HttpServletRequest request) {
		super(request);
		this.customHeaders = new HashMap<>();
	}
	
	public void putHeader(String name, String value) {
		this.customHeaders.put(name, value);
	}
	
	public String getHeader(String name) {
		String headerValue = customHeaders.get(name);
		if(null != headerValue) {
			return headerValue;
		}
		return ((HttpServletRequest) getRequest()).getHeader(name);
	}
	
	public Enumeration<String> getHeaderNames(){
		Set<String> set = new HashSet<>(customHeaders.keySet());
		Enumeration<String> headerNames = ((HttpServletRequest) getRequest()).getHeaderNames();
		while(headerNames.hasMoreElements()) {
			set.add(headerNames.nextElement());
		}
		return Collections.enumeration(set);
	}

}
