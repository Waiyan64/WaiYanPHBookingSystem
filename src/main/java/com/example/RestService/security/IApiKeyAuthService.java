package com.example.RestService.security;

import org.springframework.security.core.Authentication;

public interface IApiKeyAuthService {
    
    Authentication getAuthentication(String apiKey);

    boolean validKey(String apiKey);
}
