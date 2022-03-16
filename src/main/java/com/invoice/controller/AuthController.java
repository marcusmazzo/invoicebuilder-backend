package com.invoice.controller;

import com.invoice.jwt.JwtTokenProvider;
import com.invoice.jwt.JwtTokenUtil;
import com.invoice.jwt.AuthResponse;
import com.invoice.model.Permission;
import com.invoice.jwt.TokenResponse;
import com.invoice.model.User;
import com.invoice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class AuthController {

	@Autowired
	private UserService service;

	@Autowired
	private JwtTokenProvider authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthResponse> createToken(@RequestBody User user) {
			HttpStatus status = HttpStatus.OK;
		Authentication auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		return ResponseEntity.status(status)
				.body(authenticationManager.createToken(user.getUsername(), (List<Permission>) auth.getAuthorities()));
	}

	@GetMapping("/checkToken")
	public ResponseEntity<TokenResponse> checkToken(String token){
		return ResponseEntity.status(HttpStatus.OK).body(authenticationManager.checkToken(token));
	}

	@PostMapping(value="/refreshToken")
	public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String token,@RequestHeader("refreshToken") String refreshToken){
		Jws<Claims> claims = authenticationManager.refreshToken(refreshToken);
		return ResponseEntity.status(HttpStatus.OK)
				.body(authenticationManager.createToken(jwtTokenUtil.getUsername(claims), jwtTokenUtil.getPermissions(claims)));
	}

	@GetMapping(value="/teste")
	public String teste() {
		return "testado";
	}

}
