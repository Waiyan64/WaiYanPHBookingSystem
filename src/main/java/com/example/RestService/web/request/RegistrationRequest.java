package com.example.RestService.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    @NotNull
    private String fullName;

    @Email
    @NotNull
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Long countryCode;
}
