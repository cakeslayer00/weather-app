package com.vladsv.weather_app.interceptor;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
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

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String REQUEST_COOKIE_MISSING = "Cookie missing";
    private static final String NO_SESSION_WITH_GIVEN_ID = "No session with provided id";
    private static final String SESSION_EXPIRED = "Session expired";
    private static final String INVALID_SESSION_UUID = "Invalid session UUID";

    private final SessionDao sessionDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Cookie cookie = Objects.requireNonNull(WebUtils.getCookie(request, "SESSIONID"), REQUEST_COOKIE_MISSING);

            UUID id = UUID.fromString(cookie.getValue());
            Session session = sessionDao.findById(id).orElseThrow(() -> new InvalidSessionException(NO_SESSION_WITH_GIVEN_ID));

            if (session.getExpiryAt().isBefore(LocalDateTime.now())) {
                throw new InvalidSessionException(SESSION_EXPIRED);
            }
            request.setAttribute("user", session.getUser());
        } catch (NullPointerException e) {
            throw new InvalidSessionException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new InvalidSessionException(INVALID_SESSION_UUID);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String[] uris = {"/", "/location"};

        if (Arrays.asList(uris).contains(request.getRequestURI())) {
            modelAndView.addObject("username", ((User)request.getAttribute("user")).getUsername());
        }
    }
}
