package com.example.RestService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "countries")
@Getter
@Setter
public class Country extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long code;

    @Column(nullable = false)
    private String name;
    
}
