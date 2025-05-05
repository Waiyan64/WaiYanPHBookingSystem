// package com.example.RestService.security;

// import org.springframework.security.core.GrantedAuthority;

// import lombok.Getter;
// import org.springframework.security.authentication.AbstractAuthenticationToken;


// @Getter
// public class ApiKeyAuthentication AbstractAuthenticationToken{

//     private final String apiKey;

//     public ApiKeyAuthentication(String apiKey, Collection<? extends GrantedAuthority> authorities) {
//         super(authorities);
//         this.apiKey = apiKey;
//         setAuthenticated(true);
//     }

//     @Override
//     public Object getCredentials() {
//         return null;
//     }

//     @Override
//     public Object getPrincipal() {
//         return apiKey;
//     }
    
// }
