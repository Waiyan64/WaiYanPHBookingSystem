package com.example.RestService.web.response;

import com.example.RestService.entity.User;

import lombok.Getter;

@Getter
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private boolean emailVerified;
    private boolean enabled;
    private Long countryId;
    private String countryName;
    
    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.emailVerified = user.isEmailVerified();
        this.enabled = user.isEnabled();
        if ( user.getCountry() != null) { 
            this.countryId = user.getCountry().getId();
            this.countryName = user.getCountry().getName();
        }
    }
}
