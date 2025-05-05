package com.example.RestService.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
