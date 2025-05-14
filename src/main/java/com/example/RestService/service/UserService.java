package com.example.RestService.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.RestService.common.exception.EmailAlreadyExistsException;
import com.example.RestService.common.exception.ResourceNotFoundException;
import com.example.RestService.entity.Country;
import com.example.RestService.entity.User;
import com.example.RestService.repository.CountryRepository;
import com.example.RestService.repository.UserRepository;
import com.example.RestService.web.request.PasswordChangeRequest;
import com.example.RestService.web.request.RegistrationRequest;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CountryRepository countryRepository;
    
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.countryRepository = countryRepository;
    }
    
    @Transactional
    public User registerUser(RegistrationRequest request) {

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Country country = countryRepository.findByCode(request.getCountryCode())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCountry(country);
        
        // Generate verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusDays(1));
        user.setCreatedDate(Instant.now());
        user.setUpdatedDate(Instant.now());
        
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
        System.out.println("Attempting to verify with token: " + token);
    
    User user = userRepository.findByVerificationToken(token)
            .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token"));
    
    // Log found user for debugging
    System.out.println("Found user: " + user.getEmail() + " with token expiry: " + user.getTokenExpiry());
    
    if (LocalDateTime.now().isAfter(user.getTokenExpiry())) {
        throw new ResourceNotFoundException("Token has expired");
    }
    
    user.setEmailVerified(true);
    user.setEnabled(true);
    user.setVerificationToken(null); // Clear token after verification
    user.setTokenExpiry(null);
    user.setUpdatedDate(Instant.now());
    
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
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        
        // Normalize email to lowercase
        String normalizedEmail = email.trim().toLowerCase();

        
        try {
            return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        } catch (Exception ex) {
            System.err.println("Error finding user by email: " + email);
            ex.printStackTrace();
            throw ex;
        }
    }

    private boolean sendVerifyEmail(String email, String token) {
        System.out.println("Sending verification email to: " + email + " with token: " + token);
        return true;
    }
    
}
