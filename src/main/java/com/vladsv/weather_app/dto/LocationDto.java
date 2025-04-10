package com.vladsv.weather_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDto {

    private String name;
    @JsonProperty("lat")
    private BigDecimal latitude;
    @JsonProperty("lon")
    private BigDecimal longitude;
    private String country;
    private String state;

}
