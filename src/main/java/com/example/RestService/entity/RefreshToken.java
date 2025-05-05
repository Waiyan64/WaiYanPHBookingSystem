package com.example.RestService.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {

    @Column(name="token_id", nullable = false, unique = true)
    private String tokenId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="expiry_date", nullable = false)
    private Instant expiryDate;
    
}
