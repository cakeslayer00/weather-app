package com.vladsv.weather_app.interceptor;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.exception.SessionDoesNotExistException;
import com.vladsv.weather_app.exception.UnauthorizedUserException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionDao sessionDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UnauthorizedUserException("Go to hell intruder!");
        }

        Arrays.stream(cookies)
                .filter(cookie -> Objects.equals(cookie.getName(), "SESSIONID"))
                .findAny()
                .ifPresent(
                        cookie -> {
                            UUID id = UUID.fromString(cookie.getValue());
                            Session session = sessionDao.findById(id)
                                    .orElseThrow(() -> new SessionDoesNotExistException("There's no session with provided SESSIONID"));
                            if (session.getLocalDateTime().isBefore(LocalDateTime.now())) {
                                throw new UnauthorizedUserException("Session expired");
                            }
                        }
                );

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
