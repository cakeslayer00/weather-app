package com.vladsv.weather_app.service;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final short COOKIE_EXPIRY_TIME_IN_SECONDS = 3600;

    private final SessionDao sessionDao;

    public Session obtainSessionByUser(User user) {
        return sessionDao.findByUser(user)
                .map(this::obtainIfExpired)
                .orElseGet(() -> new Session(
                        UUID.randomUUID(),
                        LocalDateTime.now().plus(Duration.ofHours(1)),
                        user
                ));
    }

    public Cookie generateCookie(String sessionId) {
        Cookie cookie = new Cookie("SESSIONID", sessionId);
        cookie.setMaxAge(COOKIE_EXPIRY_TIME_IN_SECONDS);
        return cookie;
    }

    private Session obtainIfExpired(Session session) {
        if (session.getLocalDateTime().isBefore(LocalDateTime.now())) {
            session.setLocalDateTime(LocalDateTime.now().plus(Duration.ofHours(1)));
            sessionDao.update(session);
        }
        return session;
    }
}
