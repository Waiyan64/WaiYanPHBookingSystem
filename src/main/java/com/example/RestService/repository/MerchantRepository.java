package com.example.RestService.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.RestService.entity.Merchant;


@Repository
public interface MerchantRepository extends BaseRepository<Merchant, Long> {
    
    Optional<Merchant> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByApiCredential_Channel(String channel);

    Optional<Merchant> findByApiCredentialChannel(String channel);
}
