package com.example.RestService.web.response;

import java.time.LocalDateTime;

import com.example.RestService.entity.Booking;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BookingResponse {

    private Long id;
    private String className;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Integer creditsUsed;
    private boolean checkedIn;
    private Integer waitlistPosition;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.className = booking.getClassSchedule().getTitle();
        this.startTime = booking.getClassSchedule().getStartTime();
        this.endTime = booking.getClassSchedule().getEndTime();
        this.status = booking.getStatus().name();
        this.creditsUsed = booking.getCreditsUsed();
        this.checkedIn = booking.isCheckedIn();
        this.waitlistPosition = booking.getWaitlistPosition();
    }

    
}
