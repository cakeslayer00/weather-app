package com.vladsv.weather_app.exception.sql;

public class EntityUpdatingException extends SQLException {
    public EntityUpdatingException(String message) {
        super(message);
    }
}
