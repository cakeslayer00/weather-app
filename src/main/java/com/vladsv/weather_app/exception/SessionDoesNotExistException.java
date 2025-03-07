package com.vladsv.weather_app.exception;

public class SessionDoesNotExistException extends RuntimeException {
    public SessionDoesNotExistException(String message) {
        super(message);
    }
}
