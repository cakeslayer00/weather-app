package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.LocationDao;
import com.vladsv.weather_app.dto.WeatherCardDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final WeatherService weatherService;

    private final LocationDao locationDao;

    @GetMapping
    public ModelAndView index(@RequestAttribute("user") User user) {
        List<Location> locations = locationDao.findAllByUser(user);
        List<WeatherCardDto> weatherCards = locations.stream().map(weatherService::mapLocationToWeatherCardDto).toList();

        return new ModelAndView("index").addObject("weatherCards", weatherCards);
    }

}
