package com.vladsv.weather_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsv.weather_app.client.OpenWeatherApiClient;
import com.vladsv.weather_app.dao.LocationDao;
import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dto.LocationDto;
import com.vladsv.weather_app.dto.WeatherCardDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.exception.json.JsonException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class WeatherService {

    private final ObjectMapper objectMapper;
    private final LocationDao locationDao;
    private final SessionDao sessionDao;
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
            weatherCardDto.setId(location.getId());
            return weatherCardDto;
        } catch (JsonProcessingException e) {
            throw new JsonException(e.getMessage());
        }
    }

    public void addLocation(String sessionId, String name, String latitude, String longitude) {
        Session session = sessionDao.findById(UUID.fromString(sessionId)).get();

        Location location = getBuiltLocation(name, latitude, longitude, session);

        locationDao.persist(location);
    }

    public void deleteLocation(Long locationId) {
        locationDao.delete(locationId);
    }

    private Location getBuiltLocation(String name, String latitude, String longitude, Session session) {
        return Location.builder()
                .name(name)
                .user(session.getUser())
                .latitude(BigDecimal.valueOf(Double.parseDouble(latitude)))
                .longitude(BigDecimal.valueOf(Double.parseDouble(longitude)))
                .build();
    }

}
