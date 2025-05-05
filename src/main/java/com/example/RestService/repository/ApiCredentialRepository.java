package com.example.RestService.repository;

import java.util.Optional;

import com.example.RestService.entity.ApiCredential;

public interface ApiCredentialRepository extends BaseRepository<ApiCredential, Long> {
    Optional<ApiCredential> findByApiKey(String apiKey);

    Optional<ApiCredential> findByApiKeyAndChannel(String apiKey, String channel);

    boolean existsByApiKey(String apiKey);

    boolean existsByChannel(String channel);
}
