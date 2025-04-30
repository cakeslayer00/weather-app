package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dto.UserRequestDto;
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
    public String authorization() {
        return "sign-in";
    }

    @GetMapping("/reg")
    public String registration() {
        return "sign-up";
    }

    @PostMapping
    public String authorization(@ModelAttribute UserRequestDto userRequestDto,
                                HttpServletResponse response) {
        authService.authorize(userRequestDto, response);

        return "redirect:/";
    }

    @PostMapping(value = "/reg")
    public String registration(@ModelAttribute UserRequestDto userRequestDto,
                               HttpServletResponse response) {
        authService.register(userRequestDto, response);

        return "redirect:/";
    }

    @PostMapping(value = "/logout")
    public String logout(@CookieValue("SESSIONID") String sessionId,
                         HttpServletResponse response) {
        authService.logout(sessionId, response);

        return "redirect:/auth";
    }

}
