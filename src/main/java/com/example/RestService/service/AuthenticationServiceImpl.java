package com.example.RestService.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.RestService.common.exception.ResourceNotFoundException;

import com.example.RestService.entity.RefreshToken;
import com.example.RestService.entity.User;
import com.example.RestService.repository.UserRepository;
import com.example.RestService.security.JwtTokenProvider;
import com.example.RestService.security.TokenPayload;
import com.example.RestService.web.request.LoginRequest;
import com.example.RestService.web.request.RegistrationRequest;
import com.example.RestService.web.response.JwtAuthResponse;
import com.example.RestService.web.response.UserResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final IRefreshTokenService refreshTokenService;
    private final UserService userService;
    
    @Override
    public JwtAuthResponse login(LoginRequest loginRequest) {
        LOGGER.info("Authentication attempt for email: {}", loginRequest.getEmail());
        
        // Authenticate with Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), 
                        loginRequest.getPassword()
                )
        );
        
        // Get user from repository
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Generate JWT token
        TokenPayload payload = TokenPayload.fromUser(user);
        String token = jwtTokenProvider.generateToken(payload);
        
        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        
        // Build and return response
        UserResponse userResponse = new UserResponse(user);
        return new JwtAuthResponse(token, refreshToken.getTokenId(), userResponse);
    }

    @Override
    public JwtAuthResponse refreshToken(String refreshToken) {
        LOGGER.info("Refresh token attempt");
        
        // Validate and get refresh token
        RefreshToken token = refreshTokenService.validateRefreshToken(refreshToken);
        
        // Get user from refresh token
        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Generate new JWT token
        TokenPayload payload = TokenPayload.fromUser(user);
        String jwtToken = jwtTokenProvider.generateToken(payload);
        
        // Create new refresh token
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
        
        // Build and return response
        UserResponse userResponse = new UserResponse(user);
        return new JwtAuthResponse(jwtToken, newRefreshToken.getTokenId(), userResponse);
    }

     @Override
    public JwtAuthResponse register(RegistrationRequest request) {
        LOGGER.info("Registration attempt for email: {}", request.getEmail());
        
        // Register user - UserService handles validation, password encoding, etc.
        User user = userService.registerUser(request);
        
        // Send verification email - Mock implementation
        sendVerificationEmail(user);
        
        // Generate JWT token
        TokenPayload payload = TokenPayload.fromUser(user);
        String token = jwtTokenProvider.generateToken(payload);
        
        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        
        // Build and return response
        UserResponse userResponse = new UserResponse(user);
        return new JwtAuthResponse(token, refreshToken.getTokenId(), userResponse);
    }

    @Override
    public User verifyEmail(String token) {
        LOGGER.info("Email verification attempt");
        return userService.verifyEmail(token);
    }
    
    @Override
    public User sendPasswordResetEmail(String email) {
        LOGGER.info("Password reset request for email: {}", email);
        return userService.initiatePasswordReset(email);
    }
    
    @Override
    public User resetPassword(String token, String newPassword) {
        LOGGER.info("Password reset attempt");
        return userService.resetPassword(token, newPassword);
    }
    
    // Mock email verification - in a real system, this would send an actual email
    private boolean sendVerificationEmail(User user) {
        LOGGER.info("Sending verification email to: {}", user.getEmail());
        // Mock implementation - always returns true
        return true;
    }
 
 }
