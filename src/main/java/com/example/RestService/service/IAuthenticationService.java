package com.example.RestService.service;


import com.example.RestService.entity.User;
import com.example.RestService.web.request.LoginRequest;
import com.example.RestService.web.request.RegistrationRequest;
import com.example.RestService.web.response.JwtAuthResponse;

public interface IAuthenticationService {

   JwtAuthResponse login(LoginRequest loginRequest);
    JwtAuthResponse refreshToken(String refreshToken);
    JwtAuthResponse register(RegistrationRequest request);
    User verifyEmail(String token);
    User sendPasswordResetEmail(String email);
    User resetPassword(String token, String newPassword);
}
