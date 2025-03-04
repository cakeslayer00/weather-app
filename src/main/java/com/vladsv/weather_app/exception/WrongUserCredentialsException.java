package com.vladsv.weather_app.exception;

public class WrongUserCredentialsException extends RuntimeException {
    public WrongUserCredentialsException(String wrongPassword) {
        super(wrongPassword);
    }
}
