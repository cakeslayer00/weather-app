package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/reg")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    @PostMapping
    public String registration(@RequestParam(value="login") String login,
                               @RequestParam(value="password") String password,
                               HttpServletResponse response) {

        UUID sessionId = UUID.randomUUID();
        Cookie cookie = new Cookie("SESSIONID", sessionId.toString());
        cookie.setMaxAge(3600);

        User user = User.builder().login(login).password(password).build();
        Session session = Session.builder()
                .id(sessionId)
                .localTime(LocalDateTime.now().plus(Duration.ofHours(1)))
                .user(user).build();

        userDao.persist(user);
        sessionDao.persist(session);

        response.addCookie(cookie);
        return "success";
    }

}
