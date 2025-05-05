package com.example.RestService.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {
    
    @NotNull
    private Long classId;

    @NotNull
    private Long userPackageId;
}
