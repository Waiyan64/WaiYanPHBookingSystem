package com.example.RestService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.RestService.common.exception.BookingException;
import com.example.RestService.common.exception.ResourceNotFoundException;
import com.example.RestService.entity.Booking;
import com.example.RestService.entity.ClassSchedule;
import com.example.RestService.entity.Country;
import com.example.RestService.entity.User;
import com.example.RestService.entity.UserPackage;
import com.example.RestService.repository.BookingRepository;
import com.example.RestService.repository.ClassScheduleRepository;
import com.example.RestService.web.request.BookingRequest;

import jakarta.transaction.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final UserService userService;
    private final PackageService packageService;
    
    // For handling concurrent bookings
    private final Lock bookingLock = new ReentrantLock();
    
    
    public BookingService(
            BookingRepository bookingRepository,
            ClassScheduleRepository classScheduleRepository,
            UserService userService,
            PackageService packageService) {
        this.bookingRepository = bookingRepository;
        this.classScheduleRepository = classScheduleRepository;
        this.userService = userService;
        this.packageService = packageService;
    }
    
    public List<ClassSchedule> getUpcomingClasses() {
        return classScheduleRepository.findUpComingClasses(LocalDateTime.now());
    }
    
    public List<ClassSchedule> getUpcomingClassesByCountry(Long countryId) {
        Country country = new Country();
        country.setId(countryId);
        return classScheduleRepository.findUpcomingClassesByCountry(
                country, LocalDateTime.now());
    }
    
    public List<Booking> getUserBookings(Long userId) {
        User user = userService.getUserById(userId);
        return bookingRepository.findByUserAndStatusIn(
                user, 
                List.of(Booking.BookingStatus.BOOKED, Booking.BookingStatus.WAITLISTED));
    }

    @Transactional
    @CacheEvict(value = "classAvailability", key = "#request.classId")
    public Booking bookClass(Long userId, BookingRequest request) {
        User user = userService.getUserById(userId);
        ClassSchedule classSchedule = classScheduleRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        UserPackage userPackage = packageService.getValidUserPackagesByCountry(userId, classSchedule.getCountry().getCode())
                .stream()
                .filter(up -> up.getId().equals(request.getUserPackageId()))
                .findFirst()
                .orElseThrow(() -> new BookingException("Invalid or expired package"));
        
        // Validate country match
        if (!userPackage.getPackageInfo().getCountry().getId().equals(classSchedule.getCountry().getId())) {
            throw new BookingException("Package country does not match class country");
        }
        
        // Check if class is in the future
        if (classSchedule.isStarted() || classSchedule.isEnded()) {
            throw new BookingException("Cannot book a class that has already started or ended");
        }
        
        // Check for overlapping bookings
        if (hasOverlappingBooking(user, classSchedule)) {
            throw new BookingException("You already have a booking that overlaps with this class time");
        }
        
        // Check if user has enough credits
        if (userPackage.getRemainingCredits() < classSchedule.getRequiredCredits()) {
            throw new BookingException("Not enough credits in the package");
        }
        bookingLock.lock();
        try {
            // Get the latest count of booked seats
            int bookedSeats = bookingRepository.countBookedSeats(classSchedule);
            
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setClassSchedule(classSchedule);
            booking.setUserPackage(userPackage);
            booking.setCreditsUsed(classSchedule.getRequiredCredits());
            
            if (bookedSeats < classSchedule.getTotalSlots()) {
                // Regular booking
                booking.setStatus(Booking.BookingStatus.BOOKED);
                
                // Update class booked slots
                classSchedule.setBookedSlots(bookedSeats + 1);
                classScheduleRepository.save(classSchedule);
                
                // Deduct credits
                packageService.deductCredits(userPackage.getId(), classSchedule.getRequiredCredits());
            } else {
                // Waitlist booking
                booking.setStatus(Booking.BookingStatus.WAITLISTED);
                
                // Calculate waitlist position
                int waitlistPosition = bookingRepository.findByClassScheduleAndStatusOrderByBookingTimeAsc(
                        classSchedule, Booking.BookingStatus.WAITLISTED).size() + 1;
                booking.setWaitlistPosition(waitlistPosition);
            }
            return bookingRepository.save(booking);
        } finally {
            bookingLock.unlock();
        }
    }

    @Transactional
    @CacheEvict(value = "classAvailability", key = "#classId")
    public void cancelBooking(Long userId, Long bookingId) {
        User user = userService.getUserById(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        // Verify the booking belongs to the user
        if (!booking.getUser().getId().equals(userId)) {
            throw new BookingException("This booking doesn't belong to the user");
        }
        
        // Check if the class has already started
        if (booking.getClassSchedule().isStarted()) {
            throw new BookingException("Cannot cancel a booking for a class that has already started");
        }
        
        ClassSchedule classSchedule = booking.getClassSchedule();
        
        if (booking.getStatus() == Booking.BookingStatus.BOOKED) {
            // For booked classes, we need to handle credits refund based on cancellation time
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            booking.setCancellationTime(LocalDateTime.now());
            
            // Update class booked slots
            classSchedule.setBookedSlots(classSchedule.getBookedSlots() - 1);
            classScheduleRepository.save(classSchedule);
            
            if (booking.canRefund()) {
                packageService.refundCredits(
                        booking.getUserPackage().getId(), booking.getCreditsUsed());
            }
            
            // Process any waitlist entries
            processWaitlist(classSchedule.getId());
        } else if (booking.getStatus() == Booking.BookingStatus.WAITLISTED) {
            // For waitlisted bookings, just cancel
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            booking.setCancellationTime(LocalDateTime.now());
            
            // Update waitlist positions for everyone behind this booking
            List<Booking> waitlistBookings = bookingRepository.findByClassScheduleAndStatusOrderByBookingTimeAsc(
                    classSchedule, Booking.BookingStatus.WAITLISTED);
            
            for (Booking waitlistBooking : waitlistBookings) {
                if (waitlistBooking.getWaitlistPosition() > booking.getWaitlistPosition()) {
                    waitlistBooking.setWaitlistPosition(waitlistBooking.getWaitlistPosition() - 1);
                    bookingRepository.save(waitlistBooking);
                }
            }
        }
        
        bookingRepository.save(booking);
    }

    @Transactional
    public void checkInToClass(Long userId, Long bookingId) {
        User user = userService.getUserById(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        // Verify the booking belongs to the user
        if (!booking.getUser().getId().equals(userId)) {
            throw new BookingException("This booking doesn't belong to the user");
        }
        
        // Verify the booking is in BOOKED status
        if (booking.getStatus() != Booking.BookingStatus.BOOKED) {
            throw new BookingException("Cannot check in to a non-booked class");
        }
        
        // Verify the class has started but not ended
        ClassSchedule classSchedule = booking.getClassSchedule();
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isBefore(classSchedule.getStartTime())) {
            throw new BookingException("Cannot check in before class starts");
        }
        
        if (now.isAfter(classSchedule.getEndTime())) {
            throw new BookingException("Cannot check in after class ends");
        }

         // Mark as checked in
         booking.setCheckedIn(true);
         booking.setCheckinTime(LocalDateTime.now());
         bookingRepository.save(booking);
     }
     
     @Cacheable(value = "classAvailability", key = "#classId")
     public int getAvailableSeats(Long classId) {
         ClassSchedule classSchedule = classScheduleRepository.findById(classId)
                 .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
         
         int bookedSeats = bookingRepository.countBookedSeats(classSchedule);
         return classSchedule.getTotalSlots() - bookedSeats;
     }

     @Transactional
     @CacheEvict(value = "classAvailability", key = "#classId")
     public void processWaitlist(Long classId) {
         ClassSchedule classSchedule = classScheduleRepository.findById(classId)
                 .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
         
         // Check if there's room for a waitlisted booking
         if (classSchedule.getBookedSlots() < classSchedule.getTotalSlots()) {
             Optional<Booking> nextWaitlistedBooking = bookingRepository
                     .findFirstByClassScheduleAndStatusOrderByWaitlistPositionAsc(
                             classSchedule, Booking.BookingStatus.WAITLISTED);
             
             if (nextWaitlistedBooking.isPresent()) {
                 Booking booking = nextWaitlistedBooking.get();
                 UserPackage userPackage = booking.getUserPackage();
                 
                 // Verify user package still has enough credits
                 if (userPackage.getRemainingCredits() >= classSchedule.getRequiredCredits() &&
                         !userPackage.isExpired()) {
                     
                     // Move from waitlist to booked
                     booking.setStatus(Booking.BookingStatus.BOOKED);
                     booking.setWaitlistPosition(null);
                     
                     // Update class booked slots
                     classSchedule.setBookedSlots(classSchedule.getBookedSlots() + 1);
                     classScheduleRepository.save(classSchedule);
                     
                     // Deduct credits
                     packageService.deductCredits(userPackage.getId(), classSchedule.getRequiredCredits());
                     
                     bookingRepository.save(booking);
                    // Notify user (in a real app, this would send an email/push notification)
                    System.out.println("Notifying user " + booking.getUser().getEmail() + 
                    " that they've been moved from waitlist to booked for class " + 
                    classSchedule.getTitle());
                } else {
                    // If not enough credits or package expired, cancel the waitlist and try next person
                    booking.setStatus(Booking.BookingStatus.CANCELLED);
                    booking.setCancellationTime(LocalDateTime.now());
                    bookingRepository.save(booking);

                    // Process next in waitlist
                    processWaitlist(classId);
                }
            }  
        }
    }    
    
     @Scheduled(cron = "0 */15 * * * *") // Run every 15 minutes
    @Transactional
    public void processCompletedClasses() {
        List<ClassSchedule> completedClasses = classScheduleRepository.findCompletedClasses(LocalDateTime.now());
        
        for (ClassSchedule classSchedule : completedClasses) {
            List<Booking> waitlistedBookings = bookingRepository.findByClassScheduleAndStatusOrderByBookingTimeAsc(
                    classSchedule, Booking.BookingStatus.WAITLISTED);
            
            // Mark as inactive to avoid processing again
            classSchedule.setActive(false);
            classScheduleRepository.save(classSchedule);
            
            // No action needed for waitlisted bookings as they don't have credits deducted
            // Just mark as cancelled
            for (Booking booking : waitlistedBookings) {
                booking.setStatus(Booking.BookingStatus.CANCELLED);
                booking.setCancellationTime(LocalDateTime.now());
                bookingRepository.save(booking);
            }
            
            // Mark booked classes as completed
            List<Booking> bookedBookings = bookingRepository.findByClassScheduleAndStatusOrderByBookingTimeAsc(
                    classSchedule, Booking.BookingStatus.BOOKED);
            
            for (Booking booking : bookedBookings) {
                booking.setStatus(Booking.BookingStatus.COMPLETED);
                bookingRepository.save(booking);
            }
        }
    }
    private boolean hasOverlappingBooking(User user, ClassSchedule newClass) {
        return bookingRepository.countOverlappingBookings(
                user, newClass.getStartTime(), newClass.getEndTime()) > 0;
    }
    
}
