package com.vladsv.weather_app.exception.sql;

public class EntityDeletionException extends SQLException {
    public EntityDeletionException(String message) {
        super(message);
    }
}
