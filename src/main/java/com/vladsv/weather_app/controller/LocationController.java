package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/location")
@RequiredArgsConstructor
public class LocationController {

    @GetMapping
    public String getWeatherByLocationName(@RequestParam(value = "city") String city) {
        return city;
    }

}
