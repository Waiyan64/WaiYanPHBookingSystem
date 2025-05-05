package com.example.RestService.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginRequest {
    // @JsonProperty("username")
    // private String username;

    // @JsonProperty("password")
    // private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
