package com.vladsv.weather_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class WebClientConfig {

    private static final String WEATHER_DATA_URL = "https://api.openweathermap.org/data/2.5";
    private static final String WEATHER_GEO_URL = "https://api.openweathermap.org/geo/1.0";

    //TODO: Бля заебало!
    @Bean()
    public WebClient webClientForLocationSearch() {
        return getConfiguredWebClient(WEATHER_GEO_URL);
    }

    @Bean()
    public WebClient webClientForWeatherSearch() {
        return getConfiguredWebClient(WEATHER_DATA_URL);
    }

    private ClientHttpConnector getClientHttpConnector() {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        return new JdkClientHttpConnector(httpClient);
    }

    private WebClient getConfiguredWebClient(String url) {
        return WebClient.builder()
                .baseUrl(url)
                .clientConnector(getClientHttpConnector()).build();
    }

}
