package com.vladsv.weather_app.exception;

public class NonMatchingPasswordsException extends RuntimeException{
    public NonMatchingPasswordsException(String wrongPassword) {
        super(wrongPassword);
    }
}
