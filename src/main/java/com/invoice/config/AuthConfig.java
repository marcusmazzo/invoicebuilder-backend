package com.invoice.config;

import com.invoice.jwt.JwtAuthenticationEntryPoint;
import com.invoice.jwt.JwtConfigurer;
import com.invoice.jwt.JwtTokenProvider;
import com.invoice.util.SHA512CryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class AuthConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Bean
	public SHA512CryptPasswordEncoder passwordEncoder() {
		return SHA512CryptPasswordEncoder.builder().build();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.requiresChannel(channel -> channel.anyRequest().requiresSecure())
		   .httpBasic().disable()
		   .csrf().disable()
		   .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
		   .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		   .and()
		        .authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
		        .antMatchers("/login").permitAll()
				.antMatchers("/empresa/salvar/login").permitAll()
				.antMatchers("/checkToken").permitAll()
				.mvcMatchers("/refreshToken").permitAll()
		        .anyRequest().authenticated()
		   .and()
		   .apply(new JwtConfigurer(jwtTokenProvider)).and().authenticationProvider(jwtTokenProvider);
	}



}
