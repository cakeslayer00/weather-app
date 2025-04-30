package com.vladsv.weather_app.exception.sql;

public class EntityPersistenceException extends SQLException {
    public EntityPersistenceException(String message) {
        super(message);
    }
}
