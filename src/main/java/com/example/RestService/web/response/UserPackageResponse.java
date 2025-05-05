package com.example.RestService.web.response;

import java.time.LocalDateTime;

import com.example.RestService.entity.UserPackage;

import lombok.Getter;

@Getter
public class UserPackageResponse {
    private Long id;
    private String packageName;
    private Long remainingCredits;
    private LocalDateTime purchaseDate;
    private LocalDateTime expiryDate;
    private boolean active;
    private boolean expired;
    private String countryName;
    
    public UserPackageResponse(UserPackage userPackage) {
        this.id = userPackage.getId();
        this.packageName = userPackage.getPackageInfo().getName();
        this.remainingCredits = userPackage.getRemainingCredits();
        this.purchaseDate = userPackage.getPurchaseDate();
        this.expiryDate = userPackage.getExpiryDate();
        this.active = userPackage.isActive();
        this.expired = userPackage.isExpired();
        this.countryName = userPackage.getPackageInfo().getCountry().getName();
    }
}
