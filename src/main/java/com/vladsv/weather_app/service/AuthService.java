package com.vladsv.weather_app.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.dto.UserAuthorizationRequestDto;
import com.vladsv.weather_app.dto.UserRegistrationRequestDto;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.InvalidCredentialsException;
import com.vladsv.weather_app.exception.InvalidPasswordCredentials;
import com.vladsv.weather_app.exception.NonMatchingPasswordsException;
import com.vladsv.weather_app.exception.UserAlreadyExistsException;
import com.vladsv.weather_app.exception.sql.EntityAlreadyExistsException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final String WRONG_CREDENTIALS = "Invalid username or password";
    private static final String INVALID_USERNAME = "Invalid username";
    private static final String USER_ALREADY_EXISTS = "User with current name already exists";

    private static final int COOKIE_EXPIRY_TIME_IN_SECONDS = 3600;
    private static final int COOKIE_RESET_TIME_IN_SECONDS = 0;
    private static final int SESSION_EXPIRY_TIME_IN_SECONDS = 3600;

    private final SessionDao sessionDao;
    private final UserDao userDao;

    private final ModelMapper modelMapper;

    public void authorize(UserAuthorizationRequestDto userDto, HttpServletResponse response) {
        User user = userDao.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_USERNAME));

        if (!isPasswordsIdentical(userDto, user)) {
            throw new InvalidPasswordCredentials(WRONG_CREDENTIALS);
        }
        Session session = getBuiltSession(user);

        sessionDao.persist(session);

        applyCookies(response, session);
        log.info("Authorization complete for user: {}", user);
    }

    public void register(UserRegistrationRequestDto userDto, HttpServletResponse response) {
        try {
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                 throw new NonMatchingPasswordsException("Password do not match");
            }

            User user = modelMapper.map(userDto, User.class);
            Session session = getBuiltSession(user);

            userDao.persist(user);
            sessionDao.persist(session);

            applyCookies(response, session);
            log.info("Registration complete for user: {}", user);
        } catch (EntityAlreadyExistsException e) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS);
        }
    }

    public void logout(String sessionId, HttpServletResponse response) {
        response.addCookie(generateResetCookie(sessionId));
        log.info("User logged out for entity: {}", sessionId);
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

    private Session getBuiltSession(User user) {
        return Session.builder()
                .user(user)
                .expiryAt(LocalDateTime.now().plusSeconds(SESSION_EXPIRY_TIME_IN_SECONDS))
                .build();
    }

    private boolean isPasswordsIdentical(UserAuthorizationRequestDto userDto, User user) {
        return BCrypt.verifyer().verify(userDto.getPassword().toCharArray(), user.getPassword().toCharArray()).verified;
    }

    private void applyCookies(HttpServletResponse response, Session session) {
        response.addCookie(generateResetCookie(session.getId().toString()));
        response.addCookie(generateCookie(session.getId().toString()));
    }

}
