package com.vladsv.weather_app.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
import com.vladsv.weather_app.dto.UserRegistrationRequestDto;
import com.vladsv.weather_app.entity.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<UserRegistrationRequestDto, User> typeMap = modelMapper.createTypeMap(UserRegistrationRequestDto.class, User.class);
        Converter<String, String> converter = context -> context.getSource() == null
                ? null
                : BCrypt.with(LongPasswordStrategies.hashSha512(BCrypt.Version.VERSION_2A))
                .hashToString(6, context.getSource().toCharArray());

        typeMap.addMappings(mapper ->
                mapper.using(converter).map(UserRegistrationRequestDto::getPassword, User::setPassword)
        );

        return modelMapper;
    }

}
