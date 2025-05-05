package com.example.RestService.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.RestService.entity.Admin;

@Repository
public interface AdminRepository extends BaseRepository<Admin, Long> {
 
    Optional<Admin> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByName(String name);
}
