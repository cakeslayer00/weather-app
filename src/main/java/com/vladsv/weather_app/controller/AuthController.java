package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.WrongUserCredentialsException;
import com.vladsv.weather_app.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    private final AuthService authService;

    @GetMapping
    public String auth() {
        return "sign-in.html";
    }

    @PostMapping
    public String auth(@RequestParam(value = "username") String login,
                       @RequestParam(value = "password") String password,
                       HttpServletResponse response) {

        User user = userDao.findByLogin(login)
                .orElseThrow(() -> new WrongUserCredentialsException("Wrong username or password"));

        if (user.getPassword().equals(password)) {
            Session session = authService.obtainSessionByUser(user);

            response.addCookie(authService.generateResetCookie(session.getId().toString()));
            response.addCookie(authService.generateCookie(session.getId().toString()));
        } else {
            throw new WrongUserCredentialsException("Incorrect password");
        }

        return "success";
    }

    @PostMapping(value = "/reg")
    public String registration(@RequestParam(value = "username") String login,
                               @RequestParam(value = "password") String password,
                               HttpServletResponse response) {

        User user = User.builder().login(login).password(password).build();
        Session session = new Session(
                UUID.randomUUID(),
                LocalDateTime.now().plus(Duration.ofHours(1)),
                user
        );

        userDao.persist(user);
        sessionDao.persist(session);

        response.addCookie(authService.generateResetCookie(session.getId().toString()));
        response.addCookie(authService.generateCookie(session.getId().toString()));
        return "success";
    }

}
