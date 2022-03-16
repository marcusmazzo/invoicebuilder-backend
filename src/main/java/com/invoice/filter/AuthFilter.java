package com.invoice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.jwt.JwtTokenProvider;
import com.invoice.jwt.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebFilter
@Component
@RequiredArgsConstructor
public class AuthFilter implements Filter{


	private final JwtTokenProvider authenticationManager;

	@Value("${ignored.paths}")
	private String[] ignoredPaths;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = ((HttpServletRequest)request).getHeader("Authorization");

		if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest)request).getMethod())) {
			chain.doFilter(request, response);
		} else if(Arrays.stream(ignoredPaths).anyMatch(s -> s.contains(((HttpServletRequest)request).getRequestURI()))){
			chain.doFilter(request, response);
		} else {
			if(token == null || token.isBlank()) {
				throw new ServletException("Invalid Token");
			}
			TokenResponse tokenResponse = authenticationManager.checkToken(token.replace("Bearer ", ""));
			MDC.put("Authorization", token);
			MDC.put("tokenResponse", new ObjectMapper().writeValueAsString(tokenResponse));
			MDC.put("empresa", tokenResponse.getCuid().toString());

			chain.doFilter(request, response);
		}
	}
}
