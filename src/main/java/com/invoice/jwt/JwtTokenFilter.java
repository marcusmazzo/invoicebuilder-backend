package com.invoice.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilterInternal(
			HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (!request.getRequestURI().contains("refreshToken") && 
				!request.getRequestURI().contains("login") &&
				!request.getRequestURI().contains("/invoice/empresa/salvar/login") &&
				!request.getRequestURI().contains("checkToken")) {
			String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
			String refreshToken = jwtTokenProvider.resolveToken((HttpServletRequest) request);

			try {
				if (token != null && jwtTokenProvider.validateToken(token)) {
					Authentication auth = jwtTokenProvider.getAuthentication(token);
					if (auth != null) {
						SecurityContextHolder.getContext().setAuthentication(auth);
					}
				}
			} catch (ExpiredJwtException ex) {
				if(jwtTokenProvider.validateToken(refreshToken)) {

				};
			}
		}

		chain.doFilter(request, response);
	}
}
