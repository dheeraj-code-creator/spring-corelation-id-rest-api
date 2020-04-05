package com.springboot.rest.interceptor;

import java.time.Duration;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springboot.rest.controller.UserController;

import lombok.extern.slf4j.Slf4j;

/* class responsible for intercepting request
 * and logging below request information of the customer in log
 */
@ConditionalOnProperty(value = "config.synapse-interceptor", havingValue = "true", matchIfMissing = false)
@Component
@Slf4j
public class PCRequestInterceptor implements HandlerInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	public static final String REQUEST_ATTRIBUTES = PCRequestInterceptor.class.getName() + ".REQUEST_ATTRIBUTES";
	public static final String REQ_START_TIME = "requestStartTime";
	public static final String CORRELATION_ID = "Correlation-ID";
	public static final String SESSIONID = "sessionId";

	/*
	 * method invoke on each request to set custom attributes like correlationId,
	 * sessionId, requestTime, requestAttrubutes.
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		ServletRequestAttributes attributes = new ServletRequestAttributes(request);
		request.setAttribute(REQUEST_ATTRIBUTES, attributes);
		request.setAttribute(CORRELATION_ID, request.getHeader(CORRELATION_ID));
		request.setAttribute(REQ_START_TIME, Instant.now());
		request.setAttribute(SESSIONID, request.getSession().getId());

		// Associate the given locale with the current thread
		LocaleContextHolder.setLocale(request.getLocale());

		// Bind the given requestAttributes to the current thread
		RequestContextHolder.setRequestAttributes(attributes);
		return true;
	}

	// Method invoke after each request to process and extract request data to add
	// in log.

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		ServletRequestAttributes attributes = (ServletRequestAttributes) request.getAttribute(REQUEST_ATTRIBUTES);
		ServletRequestAttributes threadAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (null != threadAttributes) {
			// we're assuming within the original request thread..
			if (null == attributes) {
				attributes = threadAttributes;
			}
			// reset the request attributes for the current thread..
			RequestContextHolder.resetRequestAttributes();
			// reset the LocaleContextHolders for the current thread..
			LocaleContextHolder.resetLocaleContext();
		}
		if (null != attributes) {
			attributes.requestCompleted();
		}
		printJsonReq(request, response.getStatus(), handler);
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (null != ex) {
			printJsonReq(request, response.getStatus(), handler);
		}

	}

	private void printJsonReq(HttpServletRequest request, int httpStatus, Object handler)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode on = mapper.createObjectNode();
		on.put("correlationid", request.getHeader(CORRELATION_ID));
		on.put(SESSIONID, request.getAttribute(SESSIONID).toString());
		on.put("thread", Thread.currentThread().getName());
		on.put("requestparams", request.getQueryString());
		on.put("respnsetime", Duration.between(getTime(request), Instant.now()).toMillis() + "ms");
		on.put("status", httpStatus);
		on.put("url", request.getRequestURI());
		on.put("consumer", request.getRemoteUser());
		on.put("httpmethod", request.getMethod());
		on.put("operation", getMehtodName(handler));
		LOGGER.info(mapper.writeValueAsString(on));

	}

	private String getMehtodName(final Object handler) {
		String methodName = null;
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			methodName = method.getMethod().getName();
		}
		return methodName;
	}

	private Instant getTime(final HttpServletRequest request) {
		Object timeAttr = request.getAttribute(REQ_START_TIME);
		return null != timeAttr ? (Instant) timeAttr : null;
	}

}
