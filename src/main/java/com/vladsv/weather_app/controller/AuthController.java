package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.UserDoesntExistException;
import com.vladsv.weather_app.exception.WrongUserCredentialsException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    @PostMapping
    public String auth(@RequestParam(value = "login") String login,
                       @RequestParam(value = "password") String password,
                       HttpServletResponse response) {

        User user = userDao.findByLogin(login)
                .orElseThrow(() -> new UserDoesntExistException("User not found"));

        //TODO: finish session managing
        Cookie cookie = new Cookie("SESSIONID", "");
        if (user.getPassword().equals(password)) {
            Optional<Session> session = sessionDao.findByUser(user)
                    .or(() -> {
                        Session res = new Session(
                                UUID.randomUUID(),
                                LocalDateTime.now().plus(Duration.ofHours(1)),
                                user);
                        sessionDao.persist(res);
                        return Optional.of(res);
                    }
            );
            cookie.setValue(session.orElseThrow().getId().toString());
        } else {
            throw new WrongUserCredentialsException("Incorrect password");
        }

        response.addCookie(cookie);
        return "success";
    }

}
