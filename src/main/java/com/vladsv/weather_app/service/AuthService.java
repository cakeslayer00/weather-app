package com.vladsv.weather_app.service;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SessionDao sessionDao;

    public Session obtainSessionByUser(User user) {
        return sessionDao.findByUser(user).map(value -> obtainIfExpired(user, value))
                .orElseGet(() -> new Session(
                        UUID.randomUUID(),
                        LocalDateTime.now().plus(Duration.ofHours(1)),
                        user
                ));
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
