package com.example.RestService.web.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryResponse {
    private Long code;
    private String name;

    public CountryResponse( Long code, String name) {
        this.code = code;
        this.name = name;
    }

}
