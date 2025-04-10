package com.vladsv.weather_app.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OpenWeatherApiClient {
    private static final String WEATHER_DATA_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String WEATHER_GEO_URL = "https://api.openweathermap.org/geo/1.0/direct";

    private static final String MEASURE_UNIT_METRIC = "metric";
    private static final int SEARCH_QUERY_LOCATIONS_AMOUNT_LIMIT = 10;

    private final WebClient webClient;

    @Value("${appid}")
    private String api;

    public String getLocationsByNameInJson(String location) {
        return webClient.mutate().baseUrl(WEATHER_GEO_URL).build().get()
                .uri(
                        uriBuilder -> uriBuilder
                                .queryParam("q", location)
                                .queryParam("limit", SEARCH_QUERY_LOCATIONS_AMOUNT_LIMIT)
                                .queryParam("appid", api)
                                .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                .onStatus(HttpStatusCode::is5xxServerError, ClientResponse::createException)
                .bodyToMono(String.class)
                .block();
    }

    public String getWeatherByGeoCoordinatesInJson(String lat, String lon) {
        return webClient.mutate().baseUrl(WEATHER_DATA_URL).build().get()
                .uri(
                        uriBuilder -> uriBuilder
                                .queryParam("lat", lat)
                                .queryParam("lon", lon)
                                .queryParam("appid", api)
                                .queryParam("units", MEASURE_UNIT_METRIC).build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                .onStatus(HttpStatusCode::is5xxServerError, ClientResponse::createException)
                .bodyToMono(String.class)
                .block();
    }

}
