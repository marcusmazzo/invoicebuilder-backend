package com.invoice.jwt;

import com.invoice.model.*;
import com.invoice.service.EmpresaService;
import com.invoice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider implements AuthenticationProvider{
	
	@Value("${security.jwt.token.expire-length}")
	private long expire;
	
	@Value("${security.jwt.token.refresh-expire-length}")
	private long refreshExpire;

	private UserService userService;

	private EmpresaService empresaService;

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Autowired
	public JwtTokenProvider(UserService service, EmpresaService empresaService){
		this.userService = service;
		this.empresaService = empresaService;

	}


	public AuthResponse createToken(String username, List<Permission> roles){
 		User user = userService.loadUserByUsername(username);
		Empresa empresa = empresaService.findByUserId(user.getId());
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("roles", roles);
		claims.put("uid", user.getId());
		claims.put("cuid", empresa.getId());
		
		String token = jwtUtil.createToken(claims, expire);
		String refreshToken = jwtUtil.createRefreshToken(claims, refreshExpire);
		
		return new AuthResponse("Bearer", token, jwtUtil.getExpirationTime(token), refreshToken);
	}
	
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		return jwtUtil.resolveToken(bearerToken);
	}

	public String resolveRefreshToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("refreshToken");
		return jwtUtil.resolveToken(bearerToken);
	}
	
	public boolean validateToken(String token) {
		Jws<Claims> claims = jwtUtil.getClaims(token);

		if(jwtUtil.getAudience(claims)!= null && jwtUtil.getAudience(claims).equals(JwtTokenUtil.REFRESH_SCOPE)) {
			return false;
		}

		if(jwtUtil.getExpiration(claims).before(new Date())) {
			return false;
		}
		return true;
	}

	public Authentication getAuthentication(String token) {
		User user = userService.loadUserByUsername(jwtUtil.getUsername(token));
		return new UsernamePasswordAuthenticationToken(user.getUsername(), "",user.getPermissions());
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        User user = userService.loadUserByUsername(name);
        
        if(user!= null ) {
        	return new UsernamePasswordAuthenticationToken(user.getUsername(),"", user.getPermissions());
        }
        
        throw new AuthenticationCredentialsNotFoundException("Invalid User or Password");
        
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return JwtTokenProvider.class.isAssignableFrom(authentication);
	}
	
	public TokenResponse checkToken(String token){

		try {
			Jws<Claims> claims = jwtUtil.getClaims(token);
			
			if(jwtUtil.getAudience(claims)!= null && jwtUtil.getAudience(claims).equals(JwtTokenUtil.REFRESH_SCOPE)) {
				throw new JwtException("Invalid Access Token");
			}
			Long expirationTime = jwtUtil.getExpirationTime(claims);
			Date now = new Date();
			Date validate =  new Date(expirationTime-now.getTime());
			
			return TokenResponse.builder()
					.id(jwtUtil.getUserId(claims))
					.cuid(jwtUtil.getCuId(claims))
					.username(jwtUtil.getUsername(claims))
					.expirationTime(validate.getTime())
					.roles(jwtUtil.getPermissions(claims))
					.build();
		} catch (Exception e) {
			throw new JwtException("Token Expired");
		}
		
	}

	public Jws<Claims> refreshToken(String refreshToken) {
		refreshToken = jwtUtil.resolveToken(refreshToken);
		Jws<Claims> claims = jwtUtil.getClaims(refreshToken);
		if(!jwtUtil.getAudience(claims).equals(JwtTokenUtil.REFRESH_SCOPE)) {
			throw new JwtException("Invalid Refresh Token");
		}

		if(jwtUtil.getExpiration(claims).before(new Date())) {
			throw new JwtException("Refresh Token expired");
		}

		return claims;
	}
	
}
