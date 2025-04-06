package com.vladsv.weather_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsv.weather_app.dao.LocationDao;
import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.dto.LocationDto;
import com.vladsv.weather_app.dto.WeatherCardDto;
import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class WeatherService {

    private static final String WEATHER_DATA_URL = "https://api.openweathermap.org/data/2.5";
    private static final String WEATHER_GEO_URL = "https://api.openweathermap.org/geo/1.0";

    private static final String MEASURE_UNIT_METRIC = "metric";
    private static final int SEARCH_QUERY_LOCATIONS_AMOUNT_LIMIT = 10;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final LocationDao locationDao;
    private final SessionDao sessionDao;

    @Value("${appid}")
    private String api;

    public List<LocationDto> getLocationsByName(String locationName) {
        String locationsJsonString = getLocationsByNameInJson(locationName);

        try {
            return objectMapper.readValue(locationsJsonString, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public WeatherCardDto mapLocationToWeatherCardDto(Location location) {
        String weatherCardJsonString = getWeatherByGeoCoordinatesInJson(
                location.getLatitude().toString(),
                location.getLongitude().toString()
        );

        try {
            WeatherCardDto weatherCardDto =
                    objectMapper.readValue(weatherCardJsonString, WeatherCardDto.class);
            weatherCardDto.setId(location.getId());
            return weatherCardDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

    private String getLocationsByNameInJson(String location) {
        return webClient.mutate().baseUrl(WEATHER_GEO_URL).build().get()
                .uri(
                        uriBuilder -> uriBuilder.path("/direct")
                                .queryParam("q", location)
                                .queryParam("limit", SEARCH_QUERY_LOCATIONS_AMOUNT_LIMIT)
                                .queryParam("appid", api)
                                .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String getWeatherByGeoCoordinatesInJson(String lat, String lon) {
        return webClient.mutate().baseUrl(WEATHER_DATA_URL).build().get()
                .uri(
                        uriBuilder -> uriBuilder.path("/weather")
                                .queryParam("lat", lat)
                                .queryParam("lon", lon)
                                .queryParam("appid", api)
                                .queryParam("units", MEASURE_UNIT_METRIC).build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
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
