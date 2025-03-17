package com.vladsv.weather_app.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String wrongPassword) {
        super(wrongPassword);
    }
}
