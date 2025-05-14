package com.example.RestService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.RestService.entity.Country;
import com.example.RestService.entity.User;
import com.example.RestService.entity.UserPackage;

@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {
    List<UserPackage> findByUserAndActiveTrue(User user);

    @Query("SELECT up FROM UserPackage up WHERE up.user = :user AND up.active = true " +
           "AND up.expiryDate > :now AND up.remainingCredits > 0 " +
           "AND up.packageInfo.country = :country")
    List<UserPackage> findValidPackagesByUserAndCountry(User user, Country country, LocalDateTime now);
}
