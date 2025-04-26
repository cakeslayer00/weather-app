package com.vladsv.weather_app.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class WebClientConfig {

    private static final String BASE_URL = "https://api.openweathermap.org/";

    @Bean()
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(getClientHttpConnector())
                .baseUrl(BASE_URL)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> {
                            configurer.defaultCodecs().jackson2JsonEncoder(
                                    new Jackson2JsonEncoder(getObjectMapper(), MediaType.APPLICATION_JSON)
                            );
                            configurer.defaultCodecs().jackson2JsonDecoder(
                                    new Jackson2JsonDecoder(getObjectMapper(), MediaType.APPLICATION_JSON)
                            );
                        })
                        .build())
                .build();
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private ClientHttpConnector getClientHttpConnector() {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        return new JdkClientHttpConnector(httpClient);
    }

}
