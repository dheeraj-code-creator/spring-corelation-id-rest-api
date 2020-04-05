package com.springboot.rest.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

//Filter class to check and Add correlation-id header to request

@Component
public class RequestHeaderFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if(null == httpRequest.getHeader("correlation-id")) {
			MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest((HttpServletRequest) request);
			mutableRequest.putHeader("correlation-id", UUID.randomUUID().toString().concat("_".concat("Demo-Project")));
			httpRequest = mutableRequest;
		}
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("correlation-id", httpRequest.getHeader("correlation-id"));
		chain.doFilter(httpRequest, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	
}
