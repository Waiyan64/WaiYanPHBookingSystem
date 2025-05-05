package com.example.RestService.entity;

import java.time.LocalDateTime;

import com.example.RestService.constant.ActivationKey;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "activation_code")
public class ActivationCode extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "code_type")
    private ActivationKey codeType;

    @JsonIgnore
    @Column(name = "code")
    private String code;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "active")
    private boolean active;
    
}
