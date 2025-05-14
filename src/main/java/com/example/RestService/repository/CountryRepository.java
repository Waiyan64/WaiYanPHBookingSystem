package com.example.RestService.repository;

import com.example.RestService.entity.Country;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    

   Optional<Country> findByCode(Long code);
    
}
