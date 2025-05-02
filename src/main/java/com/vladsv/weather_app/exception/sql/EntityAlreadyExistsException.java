package com.vladsv.weather_app.exception.sql;

public class EntityAlreadyExistsException extends SQLException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
