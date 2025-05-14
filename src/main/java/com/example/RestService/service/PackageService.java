package com.example.RestService.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.RestService.common.exception.PaymentFailedException;
import com.example.RestService.common.exception.ResourceNotFoundException;
import com.example.RestService.entity.Country;
import com.example.RestService.entity.User;
import com.example.RestService.entity.Package;
import com.example.RestService.entity.UserPackage;
import com.example.RestService.repository.CountryRepository;
import com.example.RestService.repository.PackageRepository;
import com.example.RestService.repository.UserPackageRepository;
import com.example.RestService.web.request.PackageCreateRequest;
import com.example.RestService.web.request.PackagePurchaseRequest;

import jakarta.transaction.Transactional;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final UserPackageRepository userPackageRepository;
    private final CountryRepository countryRepository;
    private final UserService userService;
    
    
    public PackageService(
            PackageRepository packageRepository, 
            UserPackageRepository userPackageRepository,
            CountryRepository countryRepository,
            UserService userService) {
        this.packageRepository = packageRepository;
        this.userPackageRepository = userPackageRepository;
        this.countryRepository = countryRepository;
        this.userService = userService;
    }
    
    public List<Package> getAllPackages() {
        return packageRepository.findByActiveTrue();
    }
    
    public List<Package> getPackagesByCountry(Long countryId) {
        Country country = countryRepository.findByCode(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
        
        return packageRepository.findByCountryAndActiveTrue(country);
    }


    @Transactional
    public Package createPackage(PackageCreateRequest packageRequest) {
        // Validate the package request
        if (packageRequest.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (packageRequest.getCredit() <= 0) {
            throw new IllegalArgumentException("Credit must be greater than zero");
        }
        
        if (packageRequest.getValidityDays() <= 0) {
            throw new IllegalArgumentException("Validity days must be greater than zero");
        }

        Country country = countryRepository.findByCode(packageRequest.getCountryCode())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));

        Package newPackage = new Package();
        newPackage.setName(packageRequest.getName());
        newPackage.setDescription(packageRequest.getDescription());
        newPackage.setPrice(packageRequest.getPrice());
        newPackage.setCredit(packageRequest.getCredit());
        newPackage.setValidityDays(packageRequest.getValidityDays());
        newPackage.setCountry(country);
        newPackage.setCreatedDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        newPackage.setUpdatedDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        newPackage.setActive(packageRequest.getIsActive());
        
        return packageRepository.save(newPackage);
    }
    @Transactional
    public UserPackage purchasePackage(Long userId, PackagePurchaseRequest request) {
        User user = userService.getUserById(userId);
        Package packageToBuy = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        

        // Mock payment processing
        boolean paymentSuccess = processPayment(request.getPaymentMethod(), packageToBuy.getPrice());
        if (!paymentSuccess) {
            throw new PaymentFailedException("Payment failed");
        }
        UserPackage userPackage = new UserPackage();
        userPackage.setUser(user);
        userPackage.setPackageInfo(packageToBuy);
        userPackage.setRemainingCredits(packageToBuy.getCredit()); 
        userPackage.setPurchaseDate(LocalDateTime.now());
        userPackage.setCreatedDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        userPackage.setUpdatedDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        userPackage.setExpiryDate(LocalDateTime.now().plusDays(packageToBuy.getValidityDays()));
        userPackage.setActive(true);
        userPackage.setPaymentReference(UUID.randomUUID().toString());
        return userPackageRepository.save(userPackage);
    }
    
    public List<UserPackage> getUserPackages(Long userId) {
        User user = userService.getUserById(userId);
        return userPackageRepository.findByUserAndActiveTrue(user);
    }

    public List<UserPackage> getValidUserPackagesByCountry(Long userId, Long countryId) {
        User user = userService.getUserById(userId);
        Country country = countryRepository.findByCode(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
        
        return userPackageRepository.findValidPackagesByUserAndCountry(
                user, country , LocalDateTime.now());
    }
    
    @Transactional
    public boolean deductCredits(Long userPackageId, int creditsToDeduct) {
        UserPackage userPackage = userPackageRepository.findById(userPackageId)
                .orElseThrow(() -> new ResourceNotFoundException("User package not found"));
        
        if (userPackage.getRemainingCredits() < creditsToDeduct) {
            return false;
        }
        
        userPackage.setRemainingCredits(userPackage.getRemainingCredits() - creditsToDeduct);
        userPackageRepository.save(userPackage);
        return true;
    }


    @Transactional
    public boolean refundCredits(Long userPackageId, int creditsToRefund) {
        UserPackage userPackage = userPackageRepository.findById(userPackageId)
                .orElseThrow(() -> new ResourceNotFoundException("User package not found"));
        
        userPackage.setRemainingCredits(userPackage.getRemainingCredits() + creditsToRefund);
        userPackageRepository.save(userPackage);
        return true;
    }
    
    // Mock payment processing method
    private boolean processPayment(String paymentMethod, Long amount) {
        System.out.println("Processing payment: " + amount + " using " + paymentMethod);
        return true;
    }
}
