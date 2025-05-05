package com.example.RestService.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.RestService.entity.Customer;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, Long> {
    
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    Optional<Customer> findByPhoneNumberAndStatus(String phoneNumber, Customer status);

}
