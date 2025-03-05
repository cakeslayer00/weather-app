package com.vladsv.weather_app.service;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final short COOKIE_EXPIRY_TIME_IN_SECONDS = 3600;

    private final SessionDao sessionDao;

    public Session obtainSessionByUser(User user) {
        return sessionDao.findByUser(user)
                .map(value -> obtainIfExpired(user, value))
                .orElseGet(() -> new Session(
                        UUID.randomUUID(),
                        LocalDateTime.now().plus(Duration.ofHours(1)),
                        user
                ));
    }

    public Cookie generateCookie(String sessionId) {
        return new Cookie("SESSIONID", sessionId);
    }

    private Session obtainIfExpired(User user, Session session) {
        if (session.getLocalDateTime().isBefore(LocalDateTime.now())) {
            sessionDao.delete(session);
            session = new Session(
                    UUID.randomUUID(),
                    LocalDateTime.now().plus(Duration.ofHours(1)),
                    user
            );
        }
        return session;
    }
}
