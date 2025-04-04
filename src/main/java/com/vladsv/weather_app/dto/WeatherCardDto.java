package com.vladsv.weather_app.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vladsv.weather_app.deserializer.WeatherCardDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = WeatherCardDeserializer.class)
public class WeatherCardDto {

    private Long id;
    private String weather;
    private String location;
    private String temperature;
    private String feelsLikeTemperature;
    private String humidity;
    private String icon;
}
