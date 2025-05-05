package com.example.RestService.web.response;
import com.example.RestService.entity.Package;

import lombok.Getter;

@Getter
public class PackageResponse {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Integer validityDays;
    private String countryName;
    private Long countryCode;
    
    public PackageResponse(Package packageEntity) {
        this.id = packageEntity.getId();
        this.name = packageEntity.getName();
        this.description = packageEntity.getDescription();
        this.price = packageEntity.getPrice();
        this.validityDays = packageEntity.getValidityDays();
        this.countryName = packageEntity.getCountry().getName();
        this.countryCode = packageEntity.getCountry().getCode();
    }
}
