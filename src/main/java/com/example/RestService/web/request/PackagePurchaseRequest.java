package com.example.RestService.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackagePurchaseRequest {
    @NotNull
    private Long packageId;

    @NotNull
    private String paymentMethod; 
    
}
