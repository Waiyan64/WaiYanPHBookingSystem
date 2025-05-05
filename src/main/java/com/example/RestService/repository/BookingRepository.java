package com.example.RestService.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.RestService.entity.Booking;
import com.example.RestService.entity.ClassSchedule;
import com.example.RestService.entity.User;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
 
    List<Booking> findByUserAndStatusIn(User user, List<Booking.BookingStatus> statuses);
    
    List<Booking> findByClassScheduleAndStatusOrderByBookingTimeAsc(ClassSchedule classSchedule, Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.classSchedule = :classSchedule AND b.status = 'WAITLISTED' " +
           "ORDER BY b.waitlistPosition ASC")
    List<Booking> findWaitlistedBookingsByClassSchedule(ClassSchedule classSchedule);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.classSchedule = :classSchedule AND b.status = 'BOOKED'")
    int countBookedSeats(ClassSchedule classSchedule);
    
    Optional<Booking> findFirstByClassScheduleAndStatusOrderByWaitlistPositionAsc(
            ClassSchedule classSchedule, Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.classSchedule.startTime > :now " +
           "AND b.status IN ('BOOKED', 'WAITLISTED')")
    List<Booking> findUpcomingBookings(User user, LocalDateTime now);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user = :user " +
           "AND :startTime < b.classSchedule.endTime AND :endTime > b.classSchedule.startTime " +
           "AND b.status = 'BOOKED'")
    int countOverlappingBookings(User user, LocalDateTime startTime, LocalDateTime endTime);
}

