package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dto.LocationDto;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final WeatherService weatherService;

    @GetMapping
    public ModelAndView getLocationsByName(@RequestParam(name = "q") String locationName) {
        List<LocationDto> locations = weatherService.getLocationsByName(locationName);

        return new ModelAndView("search-results").addObject("locations", locations);
    }

    @PostMapping
    public String addLocation(@RequestParam(name = "name") String name,
                              @RequestParam(name = "latitude") String latitude,
                              @RequestParam(name = "longitude") String longitude,
                              HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        weatherService.addLocation(user, name, latitude, longitude);

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam(name = "locationId") String locationId) {
        weatherService.deleteLocation(Long.valueOf(locationId));
        return "redirect:/";
    }

}
