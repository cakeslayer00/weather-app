package com.vladsv.weather_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponseDto {

    private String name;
    @JsonProperty("lat")
    private BigDecimal latitude;
    @JsonProperty("lon")
    private BigDecimal longitude;
    private String country;
    private String state;

}
