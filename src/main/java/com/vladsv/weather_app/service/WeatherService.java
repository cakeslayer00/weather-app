package com.vladsv.weather_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsv.weather_app.client.OpenWeatherApiClient;
import com.vladsv.weather_app.dao.LocationDao;
import com.vladsv.weather_app.dto.LocationDto;
import com.vladsv.weather_app.dto.WeatherCardDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.json.JsonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final ObjectMapper objectMapper;
    private final LocationDao locationDao;
    private final OpenWeatherApiClient client;

    public List<LocationDto> getLocationsByName(String locationName) {
        String locationsJsonString = client.getLocationsByNameInJson(locationName);

        try {
            return objectMapper.readValue(locationsJsonString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new JsonException(e.getMessage());
        }
    }

    public WeatherCardDto mapLocationToWeatherCardDto(Location location) {
        String weatherCardJsonString = client.getWeatherByGeoCoordinatesInJson(
                location.getLatitude().toString(),
                location.getLongitude().toString()
        );

        try {
            WeatherCardDto weatherCardDto = objectMapper.readValue(weatherCardJsonString, WeatherCardDto.class);
            weatherCardDto.setLocation(
                    String.format("%s,%s", location.getName(), weatherCardDto.getLocation().split(",")[1]));
            weatherCardDto.setId(location.getId());
            return weatherCardDto;
        } catch (JsonProcessingException e) {
            throw new JsonException(e.getMessage());
        }
    }

    public void addLocation(User user, String name, String latitude, String longitude) {
        Location location = getBuiltLocation(name, latitude, longitude, user);

        locationDao.persist(location);
    }

    public void deleteLocation(Long locationId) {
        locationDao.delete(locationId);
    }

    private Location getBuiltLocation(String name, String latitude, String longitude, User user) {
        return Location.builder()
                .name(name)
                .user(user)
                .latitude(BigDecimal.valueOf(Double.parseDouble(latitude)))
                .longitude(BigDecimal.valueOf(Double.parseDouble(longitude)))
                .build();
    }

}
