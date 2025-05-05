package com.example.RestService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.RestService.constant.ActivationKey;
import com.example.RestService.entity.ActivationCode;

import jakarta.transaction.Transactional;

public interface ActivationCodeRepository extends BaseRepository<ActivationCode, Long> {
    Optional<ActivationCode> findByCustomerIdAndActiveAndCodeType(Long customerId, boolean active, ActivationKey codeType);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ActivationCode a SET a.active = false WHERE a.codeType = :code_type AND a.customer.id = :customer_id")
    void disableOldActivationCode(@Param("code_type") ActivationKey codeType, @Param("customer_id") Long customerId); 

}
