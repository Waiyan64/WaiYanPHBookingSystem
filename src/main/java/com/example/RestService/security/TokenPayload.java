package com.example.RestService.security;



import com.example.RestService.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayload {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;
    
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    public static TokenPayload fromUser(User user) {
        return TokenPayload.builder()
            .id(user.getId())
            .username(user.getEmail())
            .fullName(user.getFullName())
            .emailVerified(user.isEmailVerified())
            .build();
    }


    

    
    
}
