package com.vladsv.weather_app.exception;

public class POJOPersistenceException extends SQLException {
    public POJOPersistenceException(String message) {
        super(message);
    }
}
