package com.vladsv.weather_app.client;

import com.vladsv.weather_app.dto.LocationResponseDto;
import com.vladsv.weather_app.dto.WeatherCardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenWeatherApiClient {
    private static final String WEATHER_DATA_URI_PATH = "data/2.5/weather";
    private static final String WEATHER_GEO_URI_PATH = "geo/1.0/direct";

    private static final String MEASURE_UNIT_METRIC = "metric";
    private static final int SEARCH_QUERY_LOCATIONS_AMOUNT_LIMIT = 10;

    private final WebClient webClient;

    @Value("${openweather.api}")
    private String apiKey;

    public List<LocationResponseDto> getLocationsByNameInJson(String location) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(WEATHER_GEO_URI_PATH)
                                .queryParam("q", location)
                                .queryParam("limit", SEARCH_QUERY_LOCATIONS_AMOUNT_LIMIT)
                                .queryParam("appid", apiKey)
                                .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                .onStatus(HttpStatusCode::is5xxServerError, ClientResponse::createException)
                .bodyToMono(new ParameterizedTypeReference<List<LocationResponseDto>>() {})
                .block();
    }

    public WeatherCardDto getWeatherByGeoCoordinatesInJson(String lat, String lon) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(WEATHER_DATA_URI_PATH)
                                .queryParam("lat", lat)
                                .queryParam("lon", lon)
                                .queryParam("units", MEASURE_UNIT_METRIC)
                                .queryParam("appid", apiKey)
                                .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                .onStatus(HttpStatusCode::is5xxServerError, ClientResponse::createException)
                .bodyToMono(WeatherCardDto.class)
                .block();
    }

}
