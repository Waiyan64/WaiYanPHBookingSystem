package com.example.RestService.service;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.RestService.common.exception.TokenRefreshException;
import com.example.RestService.entity.RefreshToken;
import com.example.RestService.repository.RefreshTokenRepository;
import com.example.RestService.repository.UserRepository;


@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {
   private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

   @Value("${app.jwt.refresh-expiration-days:7}")
    private Long refreshTokenDurationDays;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public RefreshTokenServiceImpl() { 

    }

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(Long userId) {

        userRepository.findById(userId)
            .orElseThrow(() -> new TokenRefreshException("User not found"));


        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationDays * 24 * 60 * 60));

        Instant now = Instant.now();
        refreshToken.setCreatedDate(now);
        refreshToken.setUpdatedDate(now);
        
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(refreshToken -> !isRefreshTokenExpired(refreshToken))
                .orElseThrow(() -> new TokenRefreshException("Refresh token is expired or invalid"));
    }

    private boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
    
}
