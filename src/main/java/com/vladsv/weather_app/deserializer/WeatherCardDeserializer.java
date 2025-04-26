package com.vladsv.weather_app.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vladsv.weather_app.dto.WeatherCardDto;
import com.vladsv.weather_app.exception.json.WeatherCardDeserializationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WeatherCardDeserializer extends StdDeserializer<WeatherCardDto> {

    private static final String ICON_URL_FORMAT = "https://openweathermap.org/img/wn/%s@4x.png";

    public WeatherCardDeserializer() {
        this(null);
    }

    public WeatherCardDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public WeatherCardDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);

            String weather = node.get("weather").findValue("main").asText();
            String location = String.format("%s, %s", node.get("name").asText(), node.get("sys").get("country").asText());
            String temperature = String.valueOf(node.get("main").get("temp").asInt());
            String feelsLike = String.valueOf(node.get("main").get("feels_like").asInt());
            String humidity = node.get("main").get("humidity").asText();
            String iconValue = node.get("weather").findValue("icon").asText();
            String iconUrl = String.format(ICON_URL_FORMAT, iconValue) ;

            return new WeatherCardDto(null, weather, location, temperature, feelsLike, humidity, iconUrl);

        } catch (IOException e) {
            throw new WeatherCardDeserializationException(e.getMessage());
        }
    }
}
