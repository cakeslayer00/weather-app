package com.vladsv.weather_app.handler;

import com.vladsv.weather_app.exception.InvalidCredentialsException;
import com.vladsv.weather_app.exception.InvalidSessionException;
import com.vladsv.weather_app.exception.json.JsonException;
import com.vladsv.weather_app.exception.sql.POJOPersistenceException;
import com.vladsv.weather_app.exception.sql.SQLException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ModelAndView handle(WebClientResponseException e) {
        ModelAndView mav = new ModelAndView("error");
        return mav.addObject("message", e.getMessage());
    }

    @ExceptionHandler
    public ModelAndView unknownSqlError(SQLException e) {
        ModelAndView mav = new ModelAndView("error");
        return mav.addObject("message", e.getMessage());
    }

    @ExceptionHandler
    public ModelAndView unknownJsonException(JsonException e) {
        ModelAndView mav = new ModelAndView("error");
        return mav.addObject("message", e.getMessage());
    }

    @ExceptionHandler(InvalidSessionException.class)
    public String sessionError() {
        return "redirect:/auth";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ModelAndView credentialsError(InvalidCredentialsException e) {
        ModelAndView mav = new ModelAndView();

        if (e.getMessage().contains("Passwords do not match")) {
            mav.setViewName("sign-up");
            return mav.addObject("error", e.getMessage());
        }

        mav.setViewName("sign-in");
        return mav.addObject("error", e.getMessage());
    }

    @ExceptionHandler(POJOPersistenceException.class)
    public ModelAndView duringRegistrationCredentialsError(POJOPersistenceException e) {
        ModelAndView mav = new ModelAndView("sign-up");

        if (e.getMessage().contains("Password does not match")) {
            return mav.addObject("error", e.getMessage());
        }

        if (e.getMessage().contains("already exists")) {
            return mav.addObject("error", "Account with this username already exists.");
        }

        mav.setViewName("error");
        return mav.addObject("message", e.getMessage());
    }

}
