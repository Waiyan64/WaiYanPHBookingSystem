package com.example.RestService.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryRequest {
    @NotNull
    private Long code;

    @NotNull
    private String name;

}
