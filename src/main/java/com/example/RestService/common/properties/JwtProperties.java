package com.example.RestService.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.app.jwt")
public class JwtProperties {

    private String password;
    
    private Integer expirationTime;
    
}
