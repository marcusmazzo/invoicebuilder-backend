package com.invoice.jwt;

import com.invoice.model.Permission;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
	
	private String id;
	private Long cuid;
	private String username;
	private long expirationTime;
	private List<Permission> roles;

}
