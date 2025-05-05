package com.example.RestService.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.RestService.common.exception.TokenRefreshException;
import com.example.RestService.entity.RefreshToken;
import com.example.RestService.repository.RefreshTokenRepository;
import com.example.RestService.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements IRefreshTokenService {
   private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

   @Value("${app.jwt.refresh-expiration-days:7}")
    private Long refreshTokenDurationDays;
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setTokenId(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationDays * 24 * 60 * 60));
        
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(String token) {
        return refreshTokenRepository.findByTokenId(token)
                .filter(refreshToken -> !isRefreshTokenExpired(refreshToken))
                .orElseThrow(() -> new TokenRefreshException("Refresh token is expired or invalid"));
    }

    private boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }
    
}
