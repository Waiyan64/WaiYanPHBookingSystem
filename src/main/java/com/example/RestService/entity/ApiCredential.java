package com.example.RestService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "api_credential")
public class ApiCredential extends BaseEntity {
    
    @Column(name = "channel", nullable = false, length = 50, unique = true)
    private String channel;

    @Column(name = "api_key", nullable = false, length = 50)
    private String apiKey;

    @OneToOne(mappedBy = "apiCredential")
    private Merchant merchant;

}
