package com.vladsv.weather_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JdkClientHttpConnector;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class ClientHttpConnectorConfig {

    @Bean
    public ClientHttpConnector getClientHttpConnector() {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        return new JdkClientHttpConnector(httpClient);
    }

}
