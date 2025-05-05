package com.example.RestService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "earn_config")
public class EarnConfig extends BaseEntity {
    
    @Column(name = "active", columnDefinition = "boolean default true")
    private Boolean active;

    @Column(name = "name")
    private String name;

    @Column(name= "poin_value")
    private Integer pointValue;

    @Column(name = "earn_percent")
    private Double earnPercent;

    @Column(name = "title")
    private String title;

    @OneToOne
    @JoinColumn(name = "merchant_id")
    @JsonIgnore
    private Merchant merchant;

}
