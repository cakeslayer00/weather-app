package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.InvalidCredentialsException;
import com.vladsv.weather_app.exception.POJOPersistenceException;
import com.vladsv.weather_app.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    private final AuthService authService;

    @GetMapping
    public String auth() {
        return "sign-in";
    }

    @GetMapping("/reg")
    public String reg() {
        return "sign-up";
    }

    @PostMapping
    public String auth(@RequestParam(value = "username") String username,
                       @RequestParam(value = "password") String password,
                       HttpServletResponse response) {

        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));

        if (user.getPassword().equals(password)) {
            Session session = authService.obtainSessionByUser(user);

            response.addCookie(authService.generateResetCookie(session.getId().toString()));
            response.addCookie(authService.generateCookie(session.getId().toString()));
        } else {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return "redirect:/";
    }

    @PostMapping(value = "/reg")
    public String registration(@RequestParam(value = "username") String username,
                               @RequestParam(value = "password") String password,
                               @RequestParam(value = "repeat-password") String repeatPassword,
                               HttpServletResponse response) {

        if (!password.equals(repeatPassword)) {
            throw new InvalidCredentialsException("Passwords do not match");
        }

        User user = User.builder().username(username).password(password).build();
        Session session = new Session(
                UUID.randomUUID(),
                LocalDateTime.now().plus(Duration.ofHours(1)),
                user
        );

        userDao.persist(user);
        sessionDao.persist(session);

        response.addCookie(authService.generateResetCookie(session.getId().toString()));
        response.addCookie(authService.generateCookie(session.getId().toString()));
        return "redirect:/";
    }

    @PostMapping(value = "/logout")
    public String logout(@CookieValue(name = "SESSIONID") String sessionId, HttpServletResponse response) {
        response.addCookie(authService.generateResetCookie(sessionId));
        return "redirect:/auth";
    }

    @ExceptionHandler
    public ModelAndView handleException(InvalidCredentialsException e) {
        ModelAndView mav = new ModelAndView("sign-in");

        return switch (e.getMessage()) {
            case "Invalid username" -> mav.addObject("usernameError", e.getMessage());
            case "Passwords do not match" -> {
                mav.setViewName("sign-up");
                yield mav.addObject("error", e.getMessage());
            }
            default -> mav.addObject("error", e.getMessage());
        };
    }

    @ExceptionHandler
    public ModelAndView handleException(POJOPersistenceException e) {
        ModelAndView mav = new ModelAndView("sign-up");

        return e.getMessage().contains("already exists")
                ? mav.addObject("usernameError", "Account with this username already exists.")
                : mav.addObject("error", "Unknown persistence error");

    }
}
