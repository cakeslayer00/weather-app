package com.vladsv.weather_app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsv.weather_app.dao.LocationDao;
import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dto.LocationDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.exception.InvalidSessionException;
import com.vladsv.weather_app.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final WeatherService weatherService;

    private final ObjectMapper objectMapper;

    private final LocationDao locationDao;
    private final SessionDao sessionDao;

    @GetMapping
    public ModelAndView getLocationsByName(@RequestParam(name = "q") String locationName) {

        String locationsJsonString = weatherService.getLocationsByName(locationName);
        //TODO: simplify, to service
        List<LocationDto> locations;
        try {
            locations = objectMapper.readValue(locationsJsonString, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new ModelAndView("search-results").addObject("locations", locations);
    }

    @PostMapping
    public String addLocation(@CookieValue(name = "SESSIONID") String sessionId,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "latitude") String latitude,
                              @RequestParam(name = "longitude") String longitude) {


        //TODO: TO service class
        Session session = sessionDao.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new InvalidSessionException("No session with provided id"));

        Location location = Location.builder()
                .name(name)
                .user(session.getUser())
                .latitude(BigDecimal.valueOf(Double.parseDouble(latitude)))
                .longitude(BigDecimal.valueOf(Double.parseDouble(longitude)))
                .build();

        locationDao.persist(location);

        return "redirect:/";
    }

}
