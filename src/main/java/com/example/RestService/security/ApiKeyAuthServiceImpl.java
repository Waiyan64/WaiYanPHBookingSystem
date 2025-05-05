// package com.example.RestService.security;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.authority.AuthorityUtils;
// import org.springframework.stereotype.Service;

// import com.example.RestService.constant.AuthUser;
// import com.example.RestService.service.IApiCredentialService;
// import com.example.RestService.service.IApiKeyService;

// import lombok.AllArgsConstructor;
// import lombok.RequiredArgsConstructor;


// @Service
// @RequiredArgsConstructor
// public class ApiKeyAuthServiceImpl implements IApiKeyAuthService {
    
//     private final IApiKeyService apiKeyService;

//     @Override
//     public Authentication getAuthentication(String apiKey) {
//         return new ApiKeyAuthentication(apiKey, AuthorityUtils.createAuthorityList(AuthUser.API_CONSUMER.toString()));
//     }

//     @Override
//     public boolean validKey(String apiKey) {
//         return this.apiKeyService.validKey(apiKey);
//     }


// }
