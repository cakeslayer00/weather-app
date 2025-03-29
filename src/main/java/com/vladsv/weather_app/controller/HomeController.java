package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.exception.InvalidSessionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final SessionDao sessionDao;

    @GetMapping
    public ModelAndView index(@CookieValue(name = "SESSIONID") String sessionId) {
        Session session = sessionDao.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new InvalidSessionException("Session with current UUID doesn't exist"));

        return new ModelAndView("index")
                .addObject("username", session.getUser().getUsername());
    }

}
