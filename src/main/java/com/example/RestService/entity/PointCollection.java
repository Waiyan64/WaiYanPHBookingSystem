package com.example.RestService.entity;


import com.example.RestService.common.exception.InsufficientPointsException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "point_collection")
public class PointCollection extends BaseEntity{

    @Column(name = "total_points")
    private Integer totalPoint;

    public synchronized void debit(int points) { 
        if (this.totalPoint < points) { 
            throw new InsufficientPointsException("Insufficient points for debit operation.");
        }
        this.totalPoint -= points;
    }

    public synchronized void credit(int points) { 
        this.totalPoint += points;
    }
    
}
