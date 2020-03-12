package com.blog.blogservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.blog.blogservice.controller.AdminController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Enumeration;

/* This class implements the custom filter by extending org.springframework.web.filter.GenericFilterBean.  
 * Override the doFilter method with ServletRequest, ServletResponse and FilterChain.
 * This is used to authorize the API access for the application.
 */

@Component
@Order(1)
public class JwtFilter extends GenericFilterBean {

	private static Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	
	@Value("${jwt.security}")
	private boolean securityEnabled;

	/*
	 * Override the doFilter method of GenericFilterBean. Retrieve the
	 * "authorization" header from the HttpServletRequest object. Retrieve the
	 * "Bearer" token from "authorization" header. If authorization header is
	 * invalid, throw Exception with message. Parse the JWT token and get claims
	 * from the token using the secret key Set the request attribute with the
	 * retrieved claims Call FilterChain object's doFilter() method
	 */

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		logger.info("--->Security enabled : "+securityEnabled);
		if (securityEnabled && !httpRequest.getRequestURI().equals("/blog/login")) {
			logger.info("Filter invoked");
			final String authHeader = httpRequest.getHeader("authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer")) {
				throw new ServletException("Missing or invalid authorization header");

			}
			final String compactJws = authHeader.substring(7);
			try {
				JwtParser jwtparser = Jwts.parser().setSigningKey("secretKey");
				Jwt jwt = jwtparser.parse(compactJws);
				Claims claims = (Claims) jwt.getBody();
				request.setAttribute("claims", claims);
				httpRequest.getSession().setAttribute("loggedInUserId", claims.getSubject());

			} catch (io.jsonwebtoken.ExpiredJwtException e) {
				logger.error("====>JwtException occured===>" + e.getMessage());
			}
		}
		chain.doFilter(httpRequest, response);
	}
}
