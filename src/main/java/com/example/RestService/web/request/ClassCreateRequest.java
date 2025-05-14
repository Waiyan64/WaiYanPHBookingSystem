package com.example.RestService.web.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassCreateRequest {
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    @Schema(description = "Start Time", example = "2025-05-08T13:44:24Z")
    private LocalDateTime startTime;

    @NotNull
    @Schema(description = "End Time", example = "2025-05-08T13:44:24Z")
    private LocalDateTime endTime;

    @NotNull
    private Integer requiredCredits;

    @NotNull
    private Integer totalSlots;

    @NotNull
    private Integer bookedSlots;

    @NotNull
    private Long countryCode;

    
}
