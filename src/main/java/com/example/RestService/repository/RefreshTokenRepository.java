package com.example.RestService.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.RestService.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByTokenId(String tokenId);

    Optional<RefreshToken> findByCustomerId(Long customerId);

    Optional<RefreshToken> findByMerchantId(Long merchantId);

    Optional<RefreshToken> findByAdminId(Long adminId);
    
} 
