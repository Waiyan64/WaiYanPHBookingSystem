package com.example.RestService.security;

import org.springframework.security.core.Authentication;

public interface JwtTokenProvider {
    String generateToken(TokenPayload tokenPayload);
    String generateToken(Authentication authentication);

    TokenPayload getPayloadFromToken(String token);

    boolean validateToken(String token);
    
}
