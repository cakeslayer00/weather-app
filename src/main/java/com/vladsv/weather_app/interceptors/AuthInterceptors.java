package com.vladsv.weather_app.interceptors;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.exception.SessionDoesNotExistException;
import com.vladsv.weather_app.exception.UnauthorizedUserException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthInterceptors implements HandlerInterceptor {

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
                            sessionDao.findById(id)
                                    .orElseThrow(() -> new SessionDoesNotExistException("There's no session with provided SESSIONID"));
                        }
                );

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
