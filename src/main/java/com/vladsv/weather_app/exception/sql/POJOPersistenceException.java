package com.vladsv.weather_app.exception.sql;

public class POJOPersistenceException extends SQLException {
    public POJOPersistenceException(String message) {
        super(message);
    }
}
