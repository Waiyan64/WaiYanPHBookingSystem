package com.example.RestService.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.RestService.constant.CustomerStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Column(name = "phone_number", unique = true, length = 15)
    private String phoneNumber;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Column(name = "confirm_password")
    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CustomerStatus status;

    @JsonIgnore
    @Column(name = "enable", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean enable;

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "point_collection_id")
    private PointCollection pointCollection;

    @JsonIgnore
    @Column(name = "is_wallet_user")
    private Boolean isWalletUser;

    @Column(name = "kyc_level")
    private String kycLevel;

    @Column(name = "name")
    private String name;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "state")
    private String state;

    @Column(name = "district")
    private String district;

    @Column(name = "township")
    private String township;

    @Column(name = "onboard_date")
    private LocalDateTime onboardDate;
}
