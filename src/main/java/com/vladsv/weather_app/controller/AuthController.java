package com.vladsv.weather_app.controller;

import com.vladsv.weather_app.dto.UserAuthorizationRequestDto;
import com.vladsv.weather_app.dto.UserRegistrationRequestDto;
import com.vladsv.weather_app.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
    public String registration(@ModelAttribute("user") UserRegistrationRequestDto userDto) {
        return "sign-up";
    }

    @PostMapping
    public String authorization(@Valid @ModelAttribute UserAuthorizationRequestDto userDto,
                                BindingResult bindingResult,
                                HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "sign-in";
        }

        authService.authorize(userDto, response);

        return "redirect:/";
    }

    @PostMapping(value = "/reg")
    public String registration(@Valid @ModelAttribute("user") UserRegistrationRequestDto userDto,
                               BindingResult bindingResult,
                               HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "sign-up";
        }

        authService.register(userDto, response);

        return "redirect:/";
    }

    @PostMapping(value = "/logout")
    public String logout(@CookieValue("SESSIONID") String sessionId,
                         HttpServletResponse response) {
        authService.logout(sessionId, response);

        return "redirect:/auth";
    }

}
