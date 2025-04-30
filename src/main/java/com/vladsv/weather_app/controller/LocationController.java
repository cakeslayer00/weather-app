package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dto.LocationRequestDto;
import com.vladsv.weather_app.dto.LocationResponseDto;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final WeatherService weatherService;

    @GetMapping
    public ModelAndView getLocationsByName(@RequestParam(name = "q") String locationName) {
        List<LocationResponseDto> locations = weatherService.getLocationsByName(locationName);

        return new ModelAndView("search-results").addObject("locations", locations);
    }

    @PostMapping
    public String addLocation(@ModelAttribute LocationRequestDto locationRequestDto,
                              @RequestAttribute("user") User user) {
        weatherService.addLocation(user, locationRequestDto);

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam(name = "locationId") Long locationId ) {
        weatherService.deleteLocation(locationId);
        return "redirect:/";
    }

}
