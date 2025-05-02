package com.vladsv.weather_app.exception;

public class InvalidPasswordCredentials extends InvalidCredentialsException{
    public InvalidPasswordCredentials(String wrongPassword) {
        super(wrongPassword);
    }
}
