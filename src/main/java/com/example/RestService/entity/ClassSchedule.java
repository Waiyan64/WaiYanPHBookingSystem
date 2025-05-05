package com.example.RestService.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "class_schedules")
@Getter
@Setter
public class ClassSchedule extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String description;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @Column(name = "required_credits", nullable = false)
    private Integer requiredCredits;
    
    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots;
    
    @Column(name = "booked_slots", nullable = false)
    private Integer bookedSlots = 0;
    
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(nullable = false)
    private boolean active = true;
    
    public boolean isFull() {
        return bookedSlots >= totalSlots;
    }
    
    public boolean isStarted() {
        return LocalDateTime.now().isAfter(startTime);
    }
    
    public boolean isEnded() {
        return LocalDateTime.now().isAfter(endTime);
    }
    
}
