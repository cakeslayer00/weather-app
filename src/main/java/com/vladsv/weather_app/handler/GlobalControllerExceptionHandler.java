package com.vladsv.weather_app.handler;

import com.vladsv.weather_app.dto.UserRegistrationRequestDto;
import com.vladsv.weather_app.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler
    public ModelAndView handle(Exception e) {
        ModelAndView mav = new ModelAndView("error");
        log.error("Error occurred:  {}", e.getMessage());
        return mav.addObject("message", e.getMessage());
    }

    @ExceptionHandler
    public String sessionError(InvalidSessionException e) {
        log.error("Error related to session: {}", e.getMessage());
        return "redirect:/auth";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ModelAndView credentialsError(Exception e) {
        ModelAndView mav = new ModelAndView("sign-in");

        log.error("Error occurred during credentials validation: ", e);
        return mav.addObject("error", e.getMessage());
    }

    @ExceptionHandler(InvalidPasswordCredentials.class)
    public ModelAndView authPasswordError(Exception e) {
        ModelAndView mav = new ModelAndView("sign-in");

        log.error("Error occurred during authorization: ", e);
        return mav.addObject("bannerError", e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView userAlreadyExists(Exception e) {
        ModelAndView mav = new ModelAndView("sign-up");

        log.error("Error occurred during authorization: ", e);
        return mav.addObject("bannerError", e.getMessage())
                .addObject("user", new UserRegistrationRequestDto());
    }

    @ExceptionHandler(NonMatchingPasswordsException.class)
    public ModelAndView passwordNotMatchError(Exception e) {
        ModelAndView mav = new ModelAndView("sign-up");

        log.error("Error occurred password check: ", e);
        return mav.addObject("bannerError", e.getMessage());
    }

}
