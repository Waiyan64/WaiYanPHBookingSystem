package com.example.RestService.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.RestService.entity.Country;
import com.example.RestService.repository.CountryRepository;
import com.example.RestService.web.request.CountryRequest;
import com.example.RestService.web.response.CountryResponse;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/countries")
@Tag(name = "Country", description = "Country management")
public class CountryController {
    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Country>> getCountries() {
        return ResponseEntity.ok(countryRepository.findAll());
    
    }

    @PostMapping("/create")
    public ResponseEntity<CountryResponse> postMethodName(@RequestBody CountryRequest countryRequest) {
        Country country = new Country();
        country.setName(countryRequest.getName());
        country.setCode(countryRequest.getCode());
        country.setCreatedDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        country.setUpdatedDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        countryRepository.save(country);
        
        return ResponseEntity.ok(new CountryResponse(country.getCode(), country.getName()));
    }
    
    
}
