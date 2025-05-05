package com.example.RestService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements IApiKeyService {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyServiceImpl.class);

    private final IApiCredentialService apiCredentialService;

    @Override
    public boolean validKey(String key) { 
        LOGGER.info("validKey() key: {}", key);
        return this.apiCredentialService.existApiKey(key);
    }
}
