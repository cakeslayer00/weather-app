package com.vladsv.weather_app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsv.weather_app.dao.LocationDao;
import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dto.WeatherCardDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.exception.InvalidSessionException;
import com.vladsv.weather_app.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final WeatherService weatherService;

    private final SessionDao sessionDao;
    private final LocationDao locationDao;

    private final ObjectMapper objectMapper;

    @GetMapping
    public ModelAndView index(@CookieValue(name = "SESSIONID") String sessionId) {

        Session session = sessionDao.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new InvalidSessionException("No session with provided id"));

        return getAllWeatherCards(session);
    }

    //TODO: Трахнуть этот метод и на части и на сервисы и пенисы
    public ModelAndView getAllWeatherCards(Session session) {
        List<Location> locations = locationDao.findAllByUser(session.getUser());

        List<WeatherCardDto> weatherCards = locations.stream()
                .map(location -> {
                    String weatherCardJsonString = weatherService.getWeatherByGeoCoordinates(
                            location.getLatitude().toString(),
                            location.getLongitude().toString()
                    );

                    try {
                        return objectMapper.readValue(weatherCardJsonString, WeatherCardDto.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        return new ModelAndView("index").addObject("weatherCards", weatherCards);
    }

}
