package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dao.UserDao;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.UserDoesNotExistException;
import com.vladsv.weather_app.exception.WrongUserCredentialsException;
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
    public String auth(@RequestParam(value = "username") String login,
                       @RequestParam(value = "password") String password,
                       HttpServletResponse response) {

        User user = userDao.findByLogin(login)
                .orElseThrow(() -> new UserDoesNotExistException("Wrong username"));

        if (user.getPassword().equals(password)) {
            Session session = authService.obtainSessionByUser(user);

            response.addCookie(authService.generateResetCookie(session.getId().toString()));
            response.addCookie(authService.generateCookie(session.getId().toString()));
        } else {
            throw new WrongUserCredentialsException("Incorrect username or password");
        }

        return "redirect:/";
    }

    @PostMapping(value = "/reg")
    public String registration(@RequestParam(value = "username") String login,
                               @RequestParam(value = "password") String password,
                               @RequestParam(value = "repeat-password") String repeatPassword,
                               HttpServletResponse response) {

        if (password.equals(repeatPassword)) {
            throw new WrongUserCredentialsException("Passwords do not match");
        }

        User user = User.builder().login(login).password(password).build();
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

    @ExceptionHandler
    public ModelAndView handleException(WrongUserCredentialsException e) {
        ModelAndView modelAndView = new ModelAndView("sign-in");
        modelAndView.addObject("error", e.getMessage());
        return modelAndView;
    }
}
