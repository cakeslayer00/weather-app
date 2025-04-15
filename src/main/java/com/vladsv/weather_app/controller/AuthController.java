package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dto.UserDto;
import com.vladsv.weather_app.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

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
        authService.authorize(new UserDto(username, password), response);

        return "redirect:/";
    }

    @PostMapping(value = "/reg")
    public String registration(@RequestParam(value = "username") String username,
                               @RequestParam(value = "password") String password,
                               HttpServletResponse response) {
        authService.register(new UserDto(username, password), response);

        return "redirect:/";
    }

    @PostMapping(value = "/logout")
    public String logout(@CookieValue("SESSIONID") String sessionId,
                         HttpServletResponse response) {
        authService.logout(sessionId, response);

        return "redirect:/auth";
    }
}
