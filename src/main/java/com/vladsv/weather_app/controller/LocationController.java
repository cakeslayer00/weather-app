package com.vladsv.weather_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/locations")
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class LocationController {

    private static final String WEATHER_DATA_URL = "https://api.openweathermap.org/data/2.5";
    private static final String WEATHER_GEO_URL = "https://api.openweathermap.org/geo/1.0";

    private final ClientHttpConnector connector;

    @Value("${bean.appid}")
    private String api;

    @GetMapping("/search")
    public String getSeveralLocationsByName(@RequestParam(value = "q") String city) {
        WebClient webClient = getConfiguredWebClientBasedOnWeatherGeo();

        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder.path("/direct")
                                .queryParam("q", city)
                                .queryParam("appid", api).build()
                )
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    @GetMapping("/{city}")
    public String getWeatherByCityName(@PathVariable(value = "city") String city) {
        WebClient webClient = getConfiguredWebClientBasedOnWeatherData();

        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder.path("/weather")
                                .queryParam("q", city)
                                .queryParam("appid", api).build()
                )
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class)
                .onErrorResume(e -> Mono.empty())
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
