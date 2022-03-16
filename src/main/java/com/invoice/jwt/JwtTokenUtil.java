package com.invoice.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.invoice.model.Permission;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	public static final String REFRESH_SCOPE = "refresh_scope";
	
	public static final String ACCESS_SCOPE = "access_scope";
	
	@Value("${security.jwt.token.secret-key}")
	private String secretKey;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String createToken(Claims claims, Long addTime) {
		return Jwts.builder().setClaims(claims).setAudience(ACCESS_SCOPE)
				.setIssuedAt(getDate(null)).setExpiration(getDate(addTime))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}
	
	public String createRefreshToken(Claims claims, Long addTime) {
		return Jwts.builder().setClaims(claims).setAudience(REFRESH_SCOPE)
				.setIssuedAt(getDate(null)).setExpiration(getDate(addTime))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}
	
	public Jws<Claims> getClaims(String token){
		Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		return claims;
	}
	
	public String getUsername(String token) {
		return getClaims(token).getBody().getSubject();
	}
	
	public String getUsername(Jws<Claims> claims) {
		return claims.getBody().getSubject();
	}
	
	public String getAudience(String token) {
		return getClaims(token).getBody().getAudience();
	}
	
	public String getAudience(Jws<Claims> claims) {
		return claims.getBody().getAudience();
	}
	
	public Date getExpiration(String token) {
		return getClaims(token).getBody().getExpiration();
	}
	
	public Long getExpirationTime(String token) {
		return getExpiration(token).getTime();
	}
	
	public Date getExpiration(Jws<Claims> claims) {
		return claims.getBody().getExpiration();
	}
	
	public Long getExpirationTime(Jws<Claims> claims) {
		return getExpiration(claims).getTime();
	}
	
	public String resolveToken(String bearerToken) {
		if(bearerToken !=null) {
			if(bearerToken.startsWith("Bearer ")){
				return bearerToken.replace("Bearer ", "");
			}
			return bearerToken;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Permission> getPermissions(String token) {
		return (List<Permission>) getClaims(token).getBody().get("roles");
	}
	
	@SuppressWarnings("unchecked")
	public List<Permission> getPermissions(Jws<Claims> claims) {
		return (List<Permission>) claims.getBody().get("roles");
	}
	
	private Date getDate(Long addTime) {
		Date date = new Date();
		if(addTime == null) {
			return date;
		}
		
		return new Date(date.getTime()+addTime);
		
	}

	public String getUserId(Jws<Claims> claims) {
		return claims.getBody().get("uid").toString();
	}

	public Long getCuId(Jws<Claims> claims) {
		return Long.parseLong(claims.getBody().get("cuid").toString());
	}

}
