package com.vladsv.weather_app.handler;

import com.vladsv.weather_app.exception.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(UnauthorizedUserException.class)
    public String handle(UnauthorizedUserException e) {
        return "redirect:/auth";
    }

}
