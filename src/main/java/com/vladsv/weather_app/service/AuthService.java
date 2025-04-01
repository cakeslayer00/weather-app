package com.vladsv.weather_app.service;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.dto.UserDto;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.InvalidCredentialsException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final short COOKIE_EXPIRY_TIME_IN_SECONDS = 3600;
    private static final int COOKIE_RESET_TIME_IN_SECONDS = 0;
    private static final int SESSION_EXPIRY_TIME_IN_HOURS = 1;

    private final SessionDao sessionDao;
    private final UserDao userDao;

    private final ModelMapper mapper;

    public void authorize(UserDto userDto, HttpServletResponse response) {

        User user = userDao.findByUsername(userDto.getUsername()).orElseThrow(() -> new InvalidCredentialsException("Invalid username"));

        if (user.getPassword().equals(userDto.getPassword())) {
            Session session = obtainSessionByUser(user);

            response.addCookie(generateResetCookie(session.getId().toString()));
            response.addCookie(generateCookie(session.getId().toString()));
        } else {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public void register(UserDto userDto, HttpServletResponse response) {
        User user = mapper.map(userDto, User.class);
        Session session = getBuiltSession(user);

        userDao.persist(user);
        sessionDao.persist(session);

        response.addCookie(generateResetCookie(session.getId().toString()));
        response.addCookie(generateCookie(session.getId().toString()));
    }

    public void logout(String sessionId, HttpServletResponse response) {
        response.addCookie(generateResetCookie(sessionId));
    }

    private Session obtainSessionByUser(User user) {
        return sessionDao.findSessionByUser(user)
                .map(this::obtainIfExpired)
                .orElseGet(() -> new Session(
                        UUID.randomUUID(),
                        LocalDateTime.now().plus(Duration.ofHours(SESSION_EXPIRY_TIME_IN_HOURS)),
                        user));
    }

    private Cookie generateResetCookie(String sessionId) {
        Cookie cookie = new Cookie("SESSIONID", sessionId);
        cookie.setMaxAge(COOKIE_RESET_TIME_IN_SECONDS);
        cookie.setPath("/");
        return cookie;
    }

    private Cookie generateCookie(String sessionId) {
        Cookie cookie = new Cookie("SESSIONID", sessionId);
        cookie.setMaxAge(COOKIE_EXPIRY_TIME_IN_SECONDS);
        cookie.setPath("/");
        return cookie;
    }

    private Session obtainIfExpired(Session session) {
        if (session.getLocalDateTime().isBefore(LocalDateTime.now())) {
            session.setLocalDateTime(LocalDateTime.now().plus(Duration.ofHours(SESSION_EXPIRY_TIME_IN_HOURS)));
            sessionDao.update(session);
        }
        return session;
    }

    private Session getBuiltSession(User user) {
        return new Session(
                UUID.randomUUID(),
                LocalDateTime.now().plus(Duration.ofHours(SESSION_EXPIRY_TIME_IN_HOURS)),
                user);
    }

}
