package com.vladsv.weather_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/locations")
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class LocationController {


}
