package com.example.RestService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.RestService.entity.Country;
import com.example.RestService.entity.Package;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findByCountryAndActiveTrue(Country country);
    List<Package> findByActiveTrue(); 
    
}
