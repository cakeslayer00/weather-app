package com.vladsv.weather_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherCardDto {

    private String weather; // Clouds, Rain etc.
    private String location;// location name + country
    private String temperature;
    private String feelsLikeTemperature;
    private String humidity;

}
