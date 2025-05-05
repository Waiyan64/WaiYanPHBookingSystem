package com.example.RestService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchant")
public class Merchant extends BaseEntity {

    @Column(name = "username", unique = true, length = 15)
    private String username;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Column(name = "confirm_password")
    private String confirmPassword;

    @JsonIgnore
    @Column(name = "enable", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean enable;

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "point_collection_id")
    private PointCollection PointCollection;

    @OneToOne
    @JoinColumn(name = "api_credential_id", nullable = false)
    private ApiCredential apiCredential;

    @OneToOne(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "earn_config_id")
    private EarnConfig earnConfig;
    
    
}
