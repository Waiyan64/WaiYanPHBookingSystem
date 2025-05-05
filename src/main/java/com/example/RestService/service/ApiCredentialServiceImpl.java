package com.example.RestService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.RestService.constant.AppConstant;
import com.example.RestService.repository.ApiCredentialRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiCredentialServiceImpl implements IApiCredentialService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCredentialServiceImpl.class);

    private final ApiCredentialRepository apiCredentialRepository;
    
    @Cacheable(cacheNames = AppConstant.API_CREDENTIAL_CACHE, key = "#apiKey")
    @Override
    public boolean existApiKey(String apiKey) {
        return this.apiCredentialRepository.findByApiKey(apiKey).isPresent();
    }
}
