package com.example.RestService.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.RestService.entity.Package;


import com.example.RestService.entity.UserPackage;
import com.example.RestService.service.PackageService;
import com.example.RestService.web.request.PackageCreateRequest;
import com.example.RestService.web.request.PackagePurchaseRequest;
import com.example.RestService.web.response.PackageResponse;
import com.example.RestService.web.response.UserPackageResponse;



import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;
    
    
    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }
    @GetMapping
    public ResponseEntity<List<PackageResponse>> getAllPackages() {
        List<com.example.RestService.entity.Package> packages = packageService.getAllPackages();
        List<PackageResponse> response = packages.stream()
                .map(PackageResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<PackageResponse> createPackage(@Valid @RequestBody PackageCreateRequest packageRequest) {
        Package createPackage = packageService.createPackage(packageRequest);
        PackageResponse response = new PackageResponse(createPackage);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<PackageResponse>> getPackagesByCountry(@PathVariable Long countryId) {
        List<Package> packages = packageService.getPackagesByCountry(countryId);
        List<PackageResponse> response = packages.stream()
                .map(PackageResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/purchase")
    public ResponseEntity<UserPackageResponse> purchasePackage(
            @Valid @RequestBody PackagePurchaseRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        
        UserPackage userPackage = packageService.purchasePackage(userId, request);
        return new ResponseEntity<>(new UserPackageResponse(userPackage), HttpStatus.CREATED);
    }
    
    @GetMapping("/my-packages")
    public ResponseEntity<List<UserPackageResponse>> getUserPackages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        
        List<UserPackage> userPackages = packageService.getUserPackages(userId);
        List<UserPackageResponse> response = userPackages.stream()
                .map(UserPackageResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/my-packages/country/{countryId}")
    public ResponseEntity<List<UserPackageResponse>> getValidUserPackagesByCountry(
            @PathVariable Long countryId) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        
        List<UserPackage> userPackages = packageService.getValidUserPackagesByCountry(userId, countryId);
        List<UserPackageResponse> response = userPackages.stream()
                .map(UserPackageResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
}
