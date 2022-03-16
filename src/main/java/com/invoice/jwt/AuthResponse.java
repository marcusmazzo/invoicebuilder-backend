package com.invoice.jwt;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

	private String tokenType;
    private String accessToken;
    private long expire;
    private String refreshToken;

}
