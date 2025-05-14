package com.example.RestService.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.RestService.web.response.ApiResponse;
import com.example.RestService.web.response.JwtAuthResponse;
import com.example.RestService.web.response.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.RestService.common.exception.ResourceNotFoundException;
import com.example.RestService.entity.RefreshToken;
import com.example.RestService.entity.User;
import com.example.RestService.repository.UserRepository;
import com.example.RestService.security.JwtTokenProvider;
import com.example.RestService.service.RefreshTokenServiceImpl;
import com.example.RestService.service.UserService;
import com.example.RestService.web.request.LoginRequest;
import com.example.RestService.web.request.PasswordChangeRequest;
import com.example.RestService.web.request.PasswordResetRequest;
import com.example.RestService.web.request.RegistrationRequest;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;




@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

   private final UserService userService;
   private final AuthenticationManager authenticationManager;
   private final JwtTokenProvider tokenProvider;
   private final RefreshTokenServiceImpl refreshTokenService;
   private final UserRepository userRepository;

   
    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            RefreshTokenServiceImpl refreshTokenService,
            UserRepository userRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }
    
    @Operation(summary = "Register a new user", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegistrationRequest request) {
        User user = userService.registerUser(request);
        return new ResponseEntity<>(
                new ApiResponse(true, "User registered successfully. Please check email for verification."),
                HttpStatus.CREATED);
    }

    @GetMapping("/test-token")
public ResponseEntity<Map<String, String>> getTestToken() {
    Map<String, String> response = new HashMap<>();
    response.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWQiOjk5OSwiZW1haWxfdmVyaWZpZWQiOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
    return ResponseEntity.ok(response);
}

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        try {
        // Normalize email
        System.out.println("Login attempt for email: " + request.getEmail());

        String email = request.getEmail().trim().toLowerCase();
        System.out.println("Normalized email: " + email);

        boolean userExists = userRepository.existsByEmail(email);
        System.out.println("User exists in repository: " + userExists);
      

        if (!userExists) {
            System.out.println("User with email " + email + " not found in repository");
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        // First locate the user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        System.out.println("User found: " + user.getId() + ", " + user.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate token
            String jwt = tokenProvider.generateToken(authentication);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
            
            return ResponseEntity.ok(new JwtAuthResponse(jwt, refreshToken.getToken(), new UserResponse(user)));
        } catch (BadCredentialsException ex) {
            System.out.println("Bad credentials for user: " + email);
            throw new ResourceNotFoundException("Invalid email or password");
        }
    } catch (ResourceNotFoundException ex) {
        System.out.println("ResourceNotFoundException: " + ex.getMessage());
        throw ex;
    } catch (Exception ex) {
        System.err.println("Login error: " + ex.getMessage());
        ex.printStackTrace();
        throw new RuntimeException("Authentication failed: " + ex.getMessage());
    }
}
    
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        try {
            
            System.out.println("Verification request received with token: " + token);
            
            User user = userService.verifyEmail(token);
            return ResponseEntity.ok(new ApiResponse(true, 
                    "Email verified successfully for " + user.getEmail()));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Verification error: " + ex.getMessage()));
        }
    }
   
    @PostMapping("/password/change")
    public ResponseEntity<ApiResponse> changePassword(
            @Valid @RequestBody PasswordChangeRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println("Changing password for user: " + authentication); 
        User user = userService.getUserByEmail(email);
        
        userService.changePassword(user.getId(), request);
        return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
    }
    
    @PostMapping("/password/reset/request")
    public ResponseEntity<ApiResponse> requestPasswordReset(@RequestParam("email") String email) {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok(
                new ApiResponse(true, "Password reset email sent successfully"));
    }
    
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
    }
}
