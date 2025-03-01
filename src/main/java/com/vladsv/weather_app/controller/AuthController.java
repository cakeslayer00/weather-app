package com.vladsv.weather_app.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping
    public String auth(Model model) {
        model.addAttribute("username", "vladsv");
        return "auth";
    }

}
