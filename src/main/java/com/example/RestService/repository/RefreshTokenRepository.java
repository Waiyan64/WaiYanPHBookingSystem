package com.example.RestService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.RestService.entity.RefreshToken;

import jakarta.transaction.Transactional;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String tokenId);
    Optional<RefreshToken> findByUserId(Long userId);
    

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
    
} 
