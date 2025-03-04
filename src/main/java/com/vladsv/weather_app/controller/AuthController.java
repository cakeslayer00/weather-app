package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    @PostMapping
    public String auth(@RequestParam(value = "login") String login,
                       @RequestParam(value = "password") String password,
                       HttpServletRequest request,
                       HttpServletResponse response) {

        User user = userDao.findByLogin(login).orElseThrow(
                () -> new UserDoesntExistException("User not found")
        );

        Cookie cookie;
        return "success";
    }

}
