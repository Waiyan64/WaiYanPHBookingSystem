package com.example.RestService.web.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageCreateRequest {
    @Schema(description = "Package name", example = "Basic Package")
    private String name;
    
    @Schema(description = "Package description", example = "Basic package with 5 credits")
    private String description;
    
    @Schema(description = "Package price", example = "100")
    private Long price;
    
    @Schema(description = "Credits included in package", example = "5")
    private Long credit;
    
    @Schema(description = "Validity period in days", example = "30")
    private Integer validityDays;
    
    @NotNull(message = "Country is required")
    @Schema(description = "Country ID (Get IDs from /api/countries endpoint)", example = "95")
    private Long countryCode;
    
    @Schema(description = "Whether package is active", example = "true")
    private Boolean isActive;
}
