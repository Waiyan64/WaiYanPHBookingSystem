package com.example.RestService.entity;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @CreatedDate 
    @Column(name = "created_date", updatable = false, nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "updated_date", columnDefinition = "TIMESTAMP")
    private Instant updatedDate;


    
}
