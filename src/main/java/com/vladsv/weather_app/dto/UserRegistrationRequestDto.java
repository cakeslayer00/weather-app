package com.vladsv.weather_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequestDto {

    @NotBlank(message = "Must not be blank!")
    private String username;
    @NotBlank(message = "Must not be blank!")
    @Size(min = 6, max = 20, message = "Password should be in between 6 to 20")
    private String password;
    @NotBlank(message = "Must not be blank!")
    private String confirmPassword;

}
