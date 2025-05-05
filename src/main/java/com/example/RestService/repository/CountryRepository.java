package com.example.RestService.repository;

import java.util.Optional;
import com.example.RestService.entity.Country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    // Custom query methods can be defined here if needed
    // For example, to find a country by its name:
    // Optional<Country> findByName(String name);

    Optional<Country> findByCode(Long code);
    
}
