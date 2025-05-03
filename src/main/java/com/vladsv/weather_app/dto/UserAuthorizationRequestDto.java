package com.vladsv.weather_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorizationRequestDto {

    @NotBlank(message = "Must not be blank!")
    private String username;
    @NotBlank(message = "Must not be blank!")
    private String password;

}
