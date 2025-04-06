package com.vladsv.weather_app.interceptor;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.exception.InvalidSessionException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionDao sessionDao;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        try {
            Cookie cookie = Objects.requireNonNull(WebUtils.getCookie(request, "SESSIONID"),
                    "Cookie missing");

            UUID id = UUID.fromString(cookie.getValue());
            Session session = sessionDao.findById(id)
                    .orElseThrow(() -> new InvalidSessionException("No session with provided id"));

            if (session.getExpiryAt().isBefore(LocalDateTime.now())) {
                throw new InvalidSessionException("Session expired");
            }
        } catch (NullPointerException e) {
            throw new InvalidSessionException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new InvalidSessionException("Invalid session UUID");
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {

        String sessionId = Objects.requireNonNull(WebUtils.getCookie(request, "SESSIONID")).getValue();
        String[] uris = {"/", "/location"};

        if (Arrays.asList(uris).contains(request.getRequestURI())) {

            Session session = sessionDao.findById(UUID.fromString(sessionId)).get();

            modelAndView.addObject("username", session.getUser().getUsername());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
