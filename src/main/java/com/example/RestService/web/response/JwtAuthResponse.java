package com.example.RestService.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtAuthResponse {
    
    private String token;
    private UserResponse userResponse;
    private String refreshToken;

    public JwtAuthResponse(String token, UserResponse userResponse) {
        this.token = token;
        this.userResponse = userResponse;
    }

    public JwtAuthResponse(String token, String refreshToken, UserResponse userResponse) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userResponse = userResponse;
    }
    
}
