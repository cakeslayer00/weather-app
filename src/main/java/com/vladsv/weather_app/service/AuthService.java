package com.vladsv.weather_app.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.dto.UserDto;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.InvalidCredentialsException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final int COOKIE_EXPIRY_TIME_IN_SECONDS = 3600;
    private static final int COOKIE_RESET_TIME_IN_SECONDS = 0;
    private static final int SESSION_EXPIRY_TIME_IN_SECONDS = 3600;

    private final SessionDao sessionDao;
    private final UserDao userDao;

    private final ModelMapper modelMapper;

    public void authorize(UserDto userDto, HttpServletResponse response) {

        User user = userDao.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));

        if (!isPasswordsIdentical(userDto, user)) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        Session session = obtainSessionByUser(user);
        sessionDao.update(session);

        response.addCookie(generateResetCookie(session.getId().toString()));
        response.addCookie(generateCookie(session.getId().toString()));
        log.info("Authorization complete for user: {}", user);
    }

    public void register(UserDto userDto, HttpServletResponse response) {
        User user = modelMapper.map(userDto, User.class);
        Session session = getBuiltSession(user);

        userDao.persist(user);
        sessionDao.persist(session);

        response.addCookie(generateResetCookie(session.getId().toString()));
        response.addCookie(generateCookie(session.getId().toString()));
        log.info("Registration complete for user: {}", user);
    }

    public void logout(String sessionId, HttpServletResponse response) {
        response.addCookie(generateResetCookie(sessionId));
        log.info("User logged out for session id: {}", sessionId);
    }

    private Session obtainSessionByUser(User user) {
        return sessionDao.findSessionByUser(user)
                .map(this::obtainIfExpired)
                .orElseGet(() -> new Session(
                        UUID.randomUUID(),
                        LocalDateTime.now().plusSeconds(SESSION_EXPIRY_TIME_IN_SECONDS),
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
        if (session.getExpiryAt().isBefore(LocalDateTime.now())) {
            session.setExpiryAt(LocalDateTime.now().plusSeconds(SESSION_EXPIRY_TIME_IN_SECONDS));
            sessionDao.update(session);
        }
        return session;
    }

    private Session getBuiltSession(User user) {
        return new Session(
                UUID.randomUUID(),
                LocalDateTime.now().plusSeconds(SESSION_EXPIRY_TIME_IN_SECONDS),
                user);
    }

    private boolean isPasswordsIdentical(UserDto userDto, User user) {
        return BCrypt.verifyer().verify(userDto.getPassword().toCharArray(), user.getPassword().toCharArray()).verified;
    }

}
