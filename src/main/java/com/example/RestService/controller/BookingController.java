package com.example.RestService.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.RestService.entity.Booking;
import com.example.RestService.entity.ClassSchedule;
import com.example.RestService.service.BookingService;
import com.example.RestService.web.request.BookingRequest;
import com.example.RestService.web.response.ApiResponse;
import com.example.RestService.web.response.BookingResponse;
import com.example.RestService.web.response.ClassScheduleResponse;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    
    
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    @GetMapping("/classes")
    public ResponseEntity<List<ClassScheduleResponse>> getUpcomingClasses() {
        List<ClassSchedule> classes = bookingService.getUpcomingClasses();
        List<ClassScheduleResponse> response = classes.stream()
                .map(ClassScheduleResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

        @GetMapping("/classes/country/{countryId}")
    public ResponseEntity<List<ClassScheduleResponse>> getUpcomingClassesByCountry(
            @PathVariable Long countryId) {
        
        List<ClassSchedule> classes = bookingService.getUpcomingClassesByCountry(countryId);
        List<ClassScheduleResponse> response = classes.stream()
                .map(ClassScheduleResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getUserBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        
        List<Booking> bookings = bookingService.getUserBookings(userId);
        List<BookingResponse> response = bookings.stream()
                .map(BookingResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
        @PostMapping("/book")
    public ResponseEntity<BookingResponse> bookClass(@Valid @RequestBody BookingRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        
        Booking booking = bookingService.bookClass(userId, request);
        return new ResponseEntity<>(new BookingResponse(booking), HttpStatus.CREATED);
    }
    
    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long bookingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        
        bookingService.cancelBooking(userId, bookingId);
        return ResponseEntity.ok(new ApiResponse(true, "Booking cancelled successfully"));
    }
    
    @PostMapping("/{bookingId}/check-in")
    public ResponseEntity<ApiResponse> checkInToClass(@PathVariable Long bookingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        
        bookingService.checkInToClass(userId, bookingId);
        return ResponseEntity.ok(new ApiResponse(true, "Checked in successfully"));
    }

    @GetMapping("/classes/{classId}/availability")
    public ResponseEntity<Integer> getAvailableSeats(@PathVariable Long classId) {
        int availableSeats = bookingService.getAvailableSeats(classId);
        return ResponseEntity.ok(availableSeats);
    }
    
    
}
