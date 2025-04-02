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

    private String weather;
    private String location;
    private String temperature;
    private String feelsLikeTemperature;
    private String humidity;
    private String icon;
//
//    @JsonProperty("main")
//    private void unpackNestedMain(Map<String, Object> main) {
//        this.temperature = String.valueOf(main.get("temp"));
//        this.feelsLikeTemperature = String.valueOf(main.get("feels_like"));
//        this.humidity = String.valueOf(main.get("humidity"));
//    }
//
//    @JsonProperty("sys")
//    private void unpackNestedSys(Map<String, Object> sys, @JsonProperty("name") String name) {
//        Object country = sys.get("country");
//        this.location = String.format("%s, %s", name, country);
//    }
//
//    @JsonProperty("weather")
//    private void unpackNestedWeather(List<Map<String, Object>> weather) {
//        this.weather = String.valueOf(weather.getFirst().get("main"));
//    }

}
