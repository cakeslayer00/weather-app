package com.vladsv.weather_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
    private String state;

}
