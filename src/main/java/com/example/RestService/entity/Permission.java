package com.example.RestService.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity {

    @Column(name = "permission_type", nullable = false)
    private String permissionType;

    @ManyToMany(mappedBy = "permissions")
    private Set<Admin> admins = new HashSet<>();

    @ManyToMany(mappedBy = "permissions")
    private Set<Group> groups = new HashSet<>();
    
}
