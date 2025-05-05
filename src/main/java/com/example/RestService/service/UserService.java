package com.example.RestService.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.RestService.common.exception.EmailAlreadyExistsException;
import com.example.RestService.common.exception.ResourceNotFoundException;
import com.example.RestService.entity.User;
import com.example.RestService.repository.UserRepository;
import com.example.RestService.web.request.PasswordChangeRequest;
import com.example.RestService.web.request.RegistrationRequest;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Transactional
    public User registerUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEnabled(false);
        user.setEmailVerified(false);
        
        // Generate verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusDays(1));
        
        user = userRepository.save(user);
        
        // Mock email sending
        boolean emailSent = sendVerifyEmail(user.getEmail(), token);
        if (!emailSent) {
            throw new RuntimeException("Failed to send verification email");
        }
        
        return user;
    }

    @Transactional
    public User verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token"));
        
        if (LocalDateTime.now().isAfter(user.getTokenExpiry())) {
            throw new ResourceNotFoundException("Token has expired");
        }
        
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setVerificationToken(null);
        user.setTokenExpiry(null);
        
        return userRepository.save(user);
    }
    
    @Transactional
    public User changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(4));
        user = userRepository.save(user);
        
        // Mock email sending
        boolean emailSent = sendVerifyEmail(user.getEmail(), token);
        if (!emailSent) {
            throw new RuntimeException("Failed to send password reset email");
        }
        
        return user;
    }

    @Transactional
    public User resetPassword(String token, String newPassword) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid reset token"));
        
        if (LocalDateTime.now().isAfter(user.getTokenExpiry())) {
            throw new ResourceNotFoundException("Token has expired");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationToken(null);
        user.setTokenExpiry(null);
        
        return userRepository.save(user);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private boolean sendVerifyEmail(String email, String token) {
        // This would be replaced with actual email sending logic
        System.out.println("Sending verification email to: " + email + " with token: " + token);
        return true;
    }
    
}
