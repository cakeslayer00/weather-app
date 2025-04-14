package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.LocationDao;
import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dto.WeatherCardDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.Session;
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
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class MainController {

    private final WeatherService weatherService;

    private final SessionDao sessionDao;
    private final LocationDao locationDao;

    @GetMapping
    public ModelAndView index(@CookieValue(name = "SESSIONID") String sessionId) {
        Session session = sessionDao.findById(UUID.fromString(sessionId)).get();

        List<Location> locations = locationDao.findAllByUser(session.getUser());
        List<WeatherCardDto> weatherCards = locations.stream().map(weatherService::mapLocationToWeatherCardDto).toList();

        return new ModelAndView("index").addObject("weatherCards",weatherCards);
    }

    /*TODO: Ask mentor about what would be right here, to send sessionId down to the service layer to call something like
        getUserWeatherCards, or have a map method like this out there.
        and also about this suppress warnings cuz i have fucking interceptor that handles whether the session id is find or not.
     */

}
