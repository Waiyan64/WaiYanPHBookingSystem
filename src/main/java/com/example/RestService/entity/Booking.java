package com.example.RestService.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassSchedule classSchedule;
    
    @ManyToOne
    @JoinColumn(name = "user_package_id", nullable = false)
    private UserPackage userPackage;
    
    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime = LocalDateTime.now();
    
    @Column(name = "credits_used", nullable = false)
    private Integer creditsUsed;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.BOOKED;
    
    @Column(name = "checked_in")
    private boolean checkedIn = false;
    
    @Column(name = "checkin_time")
    private LocalDateTime checkinTime;
    
    @Column(name = "cancellation_time")
    private LocalDateTime cancellationTime;
    
    @Column(name = "waitlist_position")
    private Integer waitlistPosition;

    public enum BookingStatus {
        BOOKED, WAITLISTED, CANCELLED, COMPLETED
    }
    
    public boolean canRefund() {
        return classSchedule.getStartTime().minusHours(4).isAfter(LocalDateTime.now());
    }
}
