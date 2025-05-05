package com.example.RestService.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}
