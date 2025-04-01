package com.vladsv.weather_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class WeatherService {

    private static final String WEATHER_DATA_URL = "https://api.openweathermap.org/data/2.5";
    private static final String WEATHER_GEO_URL = "https://api.openweathermap.org/geo/1.0";

    private static final String MEASURE_UNIT_METRIC = "metric";
    private static final int SEARCH_QUERY_LOCATIONS_AMOUNT_LIMIT = 10;

    private final ClientHttpConnector connector;

    @Value("${appid}")
    private String api;

    public String getLocationsByName(String location) {
        WebClient webClient = getConfiguredWebClientBasedOnWeatherGeo();

        return webClient.get()
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

    public String getWeatherByGeoCoordinates(String lat, String lon) {
        WebClient webClient = getConfiguredWebClientBasedOnWeatherData();

        return webClient.get()
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

    private WebClient getConfiguredWebClientBasedOnWeatherGeo() {
        return getConfiguredWebClient(WEATHER_GEO_URL);
    }

    private WebClient getConfiguredWebClientBasedOnWeatherData() {
        return getConfiguredWebClient(WEATHER_DATA_URL);
    }

    private WebClient getConfiguredWebClient(String url) {
        return WebClient.builder()
                .baseUrl(url)
                .clientConnector(connector).build();
    }

}
