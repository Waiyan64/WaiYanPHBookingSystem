package com.example.RestService.web.response;

import java.time.LocalDateTime;

import com.example.RestService.entity.ClassSchedule;

import lombok.Getter;

@Getter 
public class ClassScheduleResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer requiredCredits;
    private Integer totalSlots;
    private Integer bookedSlots;
    private String countryName;
    private Long countryCode;
    private boolean isFull;
    
    public ClassScheduleResponse(ClassSchedule classSchedule) {
        this.id = classSchedule.getId();
        this.title = classSchedule.getTitle();
        this.description = classSchedule.getDescription();
        this.startTime = classSchedule.getStartTime();
        this.endTime = classSchedule.getEndTime();
        this.requiredCredits = classSchedule.getRequiredCredits();
        this.totalSlots = classSchedule.getTotalSlots();
        this.bookedSlots = classSchedule.getBookedSlots();
        this.countryName = classSchedule.getCountry().getName();
        this.countryCode = classSchedule.getCountry().getCode();
        this.isFull = classSchedule.isFull();
    }
}
