package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.entity.SessionEntity;
import com.vladsv.weather_app.entity.UserEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    @PostMapping
    public String authorization(@RequestParam(value="login") String login,
                                @RequestParam(value="password") String password,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        HttpSession session = request.getSession();
        String id = session.getId();
        Cookie cookie = new Cookie("SESSIONID", id);

        UserEntity userEntity = UserEntity.builder().login(login).password(password).build();
        SessionEntity sessionEntity = SessionEntity.builder()
                .id(UUID.randomUUID())
                .localTime(LocalDateTime.now().plus(Duration.ofHours(1)))
                .userId(userEntity).build();

        userDao.persist(userEntity);
        sessionDao.persist(sessionEntity);

        response.addCookie(cookie);
        response.addHeader("Set-Cookie", "SESSIONID=" + id);
        return "success";
    }

}
